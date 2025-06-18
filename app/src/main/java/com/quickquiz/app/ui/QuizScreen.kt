package com.quickquiz.app.ui

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quickquiz.app.data.model.QuizQuestion
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen() {
    val context = LocalContext.current

    var questionSet by remember { mutableStateOf<List<QuizQuestion>?>(null) }

    LaunchedEffect(Unit) {
        questionSet = loadQuizQuestions(context).shuffled().take(10)
    }

    val questions = questionSet

    if (questions == null) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }

    val feedbackMessage = when (score) {
        in 0..3 -> "😢 Bad luck! Try again to improve."
        in 4..6 -> "🙂 Not bad! You can do better."
        in 7..9 -> "🔥 Great job! Almost perfect."
        10 -> "🏆 Outstanding! You're a quiz master!"
        else -> ""
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("QuickQuiz") })
    }, content = { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isQuizFinished) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉 Quiz Finished!", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("$score/10", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(feedbackMessage, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row {
                        Button(
                            onClick = {
                                currentIndex = 0
                                selectedOption = null
                                score = 0
                                isQuizFinished = false
                            }, modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Try Again")
                        }

                        Button(
                            onClick = {
                                questionSet = loadQuizQuestions(context).shuffled().take(10)
                                currentIndex = 0
                                selectedOption = null
                                score = 0
                                isQuizFinished = false
                            }) {
                            Text("Next Round")
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text("Question ${currentIndex + 1} of 10", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    AnimatedContent(
                        targetState = currentIndex,
                        transitionSpec = {
                            slideInHorizontally(animationSpec = tween(300)) { fullWidth -> fullWidth } +
                                    fadeIn(animationSpec = tween(300)) togetherWith
                                    slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth } +
                                    fadeOut(animationSpec = tween(300))
                        },
                        label = "Slide Question"
                    ) { index ->
                        val question = questions[index]
                        val options = listOf(
                            "A" to question.A,
                            "B" to question.B,
                            "C" to question.C,
                            "D" to question.D
                        )

                        Column {
                            Text(text = question.question, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(20.dp))

                            options.forEach { (key, value) ->
                                Button(
                                    onClick = { selectedOption = key },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedOption == key) Color.LightGray else MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("$key. $value")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (selectedOption == questions[currentIndex].answer) {
                                score++
                            }

                            if (currentIndex + 1 < 10) {
                                selectedOption = null
                                currentIndex++
                            } else {
                                isQuizFinished = true
                            }
                        },
                        enabled = selectedOption != null,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(if (currentIndex == 9) "Finish" else "Next")
                    }
                }
            }

        }
    })
}


fun loadQuizQuestions(context: Context): List<QuizQuestion> {
    val json = context.assets.open("questions.json").bufferedReader().use { it.readText() }
    val gson = Gson()
    val type = object : TypeToken<List<QuizQuestion>>() {}.type
    return gson.fromJson(json, type)
}
