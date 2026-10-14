# Larp Sensitivity Lab

Native Android Kotlin/XML starter app for device scanning and illustrative sensitivity recommendations.

## Build using only a phone
1. Create a GitHub account if needed and create a new **public** repository.
2. Upload all files in this project to the repository, keeping their folder paths.
3. Open the **Actions** tab and enable Actions if asked.
4. Select **Build Larp Sensitivity Lab APK** and press **Run workflow** (or push files to `main`).
5. Wait for the workflow to finish successfully.
6. Open the completed run, find **Artifacts**, and download `Larp-Sensitivity-Lab-debug-apk`.
7. Extract the downloaded artifact ZIP; it contains `app-debug.apk`.
8. Tap the APK and follow Android's installer prompts. Only install builds you trust.

The workflow builds with Java 17 and Gradle 8.9 in GitHub's cloud runner. No PC is required, but uploading the project folders through a phone browser may be fiddly.

## Limitations
- Touch sampling rate is marked unavailable because Android has no dependable universal public API for it.
- Older Android versions may only report a hardware identifier instead of a friendly SoC name.
- Generated values are heuristics, not guaranteed headshot settings.
- Mouse DPI and keymapper internal scaling are not automatically read or changed.
- Copy values and enter them manually unless a supported import API exists.
