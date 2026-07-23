# kotlin-todo-app — Working Notes

## Project Summary
CI-only Android Kotlin Todo app. No cloud infrastructure provisioned.
Pipeline: lint → test → provision (no-op) → configure (build APK) → verify (check APK exists + upload artifact).

## Stack
- Language: Kotlin 1.9.23
- Android Gradle Plugin: 8.3.2
- Gradle: 8.6
- Room 2.6.1 (SQLite persistence)
- LiveData + ViewModel (lifecycle 2.8.3)
- RecyclerView + Material Design 3
- Unit tests: JUnit4 + MockK + arch-core-testing + coroutines-test
- viewBinding enabled

## Pipeline Decisions
- Backbone is mandatory (provision/configure/verify). Provision stage is a no-op echo since this is CI-only.
- Configure stage does the APK build (assembleDebug) and uploads artifact.
- Verify stage checks APK exists on disk (file was just built in configure — same runner job).
- Lint report uploaded as artifact (if-no-files-found: warn, so it doesn't fail if lint produces no HTML).
- APK artifact retained 30 days.
- JDK 17 + temurin + gradle cache on all stages.

## Architecture Decisions
- Repository pattern separating DAO from ViewModel.
- TodoViewModelFactory for DI-free injection of repository.
- DiffUtil (ListAdapter) for efficient RecyclerView updates.
- viewBinding only (no Jetpack Compose — keeps the scope minimal, no extra build complexity).

## Known Quirks
- gradle-wrapper.jar is NOT in the repo (not a binary blob). GitHub Actions downloads Gradle from the distributionUrl in gradle-wrapper.properties. This is standard and fine.
- The lint output path is configured in build.gradle.kts (lint { htmlOutput }) to match what the pipeline uploads.

## Status
- [x] Architecture written
- [x] Pipeline written
- [x] Design approved
- [x] Plan approved
- [x] All source files written
- [ ] validate_project
- [ ] create_repo_and_push
- [ ] deploy
