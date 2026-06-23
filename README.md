# TaxHelper Android App

A native Android wrapper for TaxHelper accounting software.
Connects to your TaxHelper server (cloud or local network).

## How it works

The app is a WebView wrapper that loads TaxHelper.
Two modes:

1. **☁️ Cloud Mode** — connects to your Render cloud URL (works anywhere)
2. **🏠 Local Mode** — connects to your PC/Mac on the same Wi-Fi

## Build Instructions

### Prerequisites
- Android Studio (latest)
- JDK 17+
- An Android device or emulator (API 24+)

### Build
```bash
cd taxhelper-android
./gradlew assembleDebug
```
APK will be at `app/build/outputs/apk/debug/app-debug.apk`

### Release build
```bash
./gradlew assembleRelease
```

## First Run
1. Install the APK on your Android device
2. Open the app
3. Enter your TaxHelper server URL on the settings screen
4. Tap Connect
5. Log in with your TaxHelper credentials
