# ⛵ Sailing Master Quiz

A premium, cross-platform educational application built with **Compose Multiplatform**. Test your nautical knowledge across multiple categories, track your high scores, and master the art of seamanship.

## 🌟 Features

- **120+ High-Quality Questions**: Curated questions covering everything from basic boat parts to advanced navigation and safety.
- **Four Learning Categories**:
  - Seamanship & Basics
  - Sailing & Maneuvers
  - Navigation & Rules
  - Safety & Knots
- **Dynamic Quiz Lengths**: Choose your journey with 5, 10, 20, or 25-question sprints.
- **Local High Scores**: Your personal bests are saved locally for every category and quiz length.
- **50/50 Lifeline**: Use the nautical "magic wand" to hide two incorrect options when you're in doubt.
- **Instant Educational Feedback**: Detailed explanations and mnemonics for every question.
- **Premium UI/UX**: 
  - Smooth slide-fade-scale transitions.
  - Animated "Sailing Boat" progress bar.
  - Responsive design for Mobile (Android) and Web (Wasm-JS).

## 🛠 Tech Stack

- **Language**: Kotlin 2.1.0
- **UI Framework**: Compose Multiplatform (Material 3)
- **Dependency Injection**: Koin
- **Persistence**: Multiplatform Settings
- **Concurrency**: Kotlinx Coroutines
- **Serialization**: Kotlinx Serialization (JSON)
- **Target Platforms**: Android & Web (WebAssembly/JS)

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17 or higher

### Android
1. Open the project in Android Studio.
2. Select the `composeApp` run configuration.
3. Choose your emulator or physical device.
4. Click **Run**.

**Via Terminal:**
```powershell
./gradlew :composeApp:installDebug
```

### Web (Wasm-JS)
Run the following command to launch the development server:
```powershell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## 🌍 Deployment

This project is configured for **GitHub Pages**. 

1. Push your code to the `main` branch.
2. GitHub Actions will automatically build the Wasm-JS distribution.
3. Ensure your Repository Settings > Pages > Source is set to **GitHub Actions**.

The site will be live at `https://waliaharsh.github.io/SailingQuizApp/`

---
*Fair winds and following seas!* 🌊
