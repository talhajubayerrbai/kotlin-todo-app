# Kotlin Todo App

A clean, minimal **Android Todo application** built with Kotlin, Room, LiveData, and Material Design 3. The repository ships a fully automated GitHub Actions pipeline that lints, tests, builds, and publishes the debug APK as a downloadable artifact.

---

## Features

| Feature | Detail |
|---|---|
| ✅ Add todos | Type in the input field and tap **Add** or press **Done** |
| ✔️ Complete todos | Tap the checkbox — completed items get a strike-through |
| 🗑️ Delete todos | Tap the red trash icon on any row |
| 🧹 Clear completed | Overflow menu → **Clear completed** |
| 💾 Persistence | Room (SQLite) — todos survive app restarts |
| 🎨 Material Design | Cards, colour scheme, adaptive launcher icon |

---

## Architecture

```
┌─────────────────────────────────────────────┐
│ UI Layer                                    │
│  MainActivity  ←→  TodoAdapter (RecyclerView)│
└──────────────┬──────────────────────────────┘
               │ observes LiveData
┌──────────────▼──────────────────────────────┐
│ ViewModel Layer                             │
│  TodoViewModel  (viewModelScope coroutines) │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│ Data Layer                                  │
│  TodoRepository → TodoDao → AppDatabase     │
│                              (Room / SQLite) │
└─────────────────────────────────────────────┘
```

**Stack:** Kotlin · Room · LiveData · ViewModel · Coroutines · Material Components · RecyclerView

---

## CI/CD Pipeline

The pipeline runs automatically on every push to `main` and on manual dispatch.

| Stage | What it does |
|---|---|
| **lint** | Runs Android Lint and uploads the HTML lint report |
| **test** | Runs all JUnit4 unit tests (ViewModel + data layer) |
| **build (configure)** | Assembles `app-debug.apk` with `./gradlew assembleDebug` |
| **verify** | Confirms the APK exists and prints its size |

### Downloading the Debug APK

1. Go to **Actions** → select the latest successful workflow run.
2. Scroll to the **Artifacts** section at the bottom of the summary page.
3. Click **kotlin-todo-app-debug** to download the zip containing `app-debug.apk`.
4. Artifacts are retained for **30 days**.

> **Install on device:** enable _Install from Unknown Sources_ in Android settings, copy the APK to your device, and open it to install.

---

## Local Development

### Prerequisites

| Tool | Version |
|---|---|
| Android Studio | Hedgehog (2023.1.1) or later |
| JDK | 17 |
| Android SDK | API 34 (compile) · API 24 (min) |
| Gradle | 8.6 (via wrapper — no install needed) |

### Build & Run

```bash
# Clone
git clone https://github.com/<your-org>/kotlin-todo-app.git
cd kotlin-todo-app

# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run lint
./gradlew lint

# Open in Android Studio and run on emulator / device via the IDE
```

The compiled APK is placed at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## Project Structure

```
kotlin-todo-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/kotlintodo/
│   │   │   │   ├── data/          # Room entity, DAO, Database, Repository
│   │   │   │   ├── viewmodel/     # TodoViewModel + Factory
│   │   │   │   └── ui/            # MainActivity, TodoAdapter
│   │   │   └── res/               # Layouts, drawables, strings, themes
│   │   └── test/                  # JUnit4 unit tests
│   └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml         # Version catalog
│   └── wrapper/
├── .udap/
│   ├── architecture.d2            # Architecture diagram (source of truth)
│   └── pipeline.yaml              # Pipeline spec (renders GitHub Actions)
└── README.md
```

---

## Running Tests

```bash
# All unit tests with report
./gradlew test

# View report
open app/build/reports/tests/testDebugUnitTest/index.html
```

Test coverage:
- `TodoViewModelTest` — add, toggle, delete, deleteCompleted, blank-input guard, insertResult LiveData
- `TodoItemTest` — data class equality, copy/toggle, default values

---

## License

MIT
