
# QuickQuiz App 🧠

QuickQuiz is a lightweight Android quiz application built using Kotlin and Jetpack Compose. Users can test their knowledge through multiple-choice questions and receive feedback based on their score.

## 🛠️ Features

- 📝 10 random multiple-choice questions
- 🎮 Real-time score tracking
- ✨ Animated question transitions
- 📊 Personalized feedback after the quiz
- 🔄 "Try Again" and "Next Round" functionality

## 📁 Project Structure

```
com.quickquiz.app/
├── data/
│   └── model/
│       └── QuizQuestion.kt      # Data class for questions
├── ui/
│   └── QuizScreen.kt            # Main UI screen
├── assets/
│   └── questions.json           # JSON file with quiz questions
```

## 📦 Dependencies

- Jetpack Compose
- Material3 UI
- Gson for JSON parsing

## 📄 JSON Format (assets/questions.json)

```
[
  {
    "question": "A flashing red traffic light signifies that a driver should do what?",
    "A": "stop",
    "B": "speed up",
    "C": "proceed with caution",
    "D": "honk the horn",
    "answer": "A"
  },
  ...
]
```

## 🚀 Getting Started

1. Clone the repository.
2. Add your `questions.json` to `assets/` folder.
3. Build and run the app.

## 📜 License

This project is open source and available under the MIT License.
