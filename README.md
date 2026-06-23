# TaxHelper Android App

A native Android wrapper for TaxHelper accounting software.

## 📲 How to get the APK (no coding needed)

### Option 1: GitHub Actions (easiest)

1. Push this project to a **public GitHub repository**
2. Go to your repo → **Actions** tab
3. The workflow auto-starts — wait ~3 minutes
4. When done, click the workflow run → **Artifacts** section
5. Download **TaxHelper-Android-APK.zip**
6. Extract the APK file inside
7. Transfer it to your Android phone (email, USB, Google Drive, etc.)
8. Open the APK on your phone → tap **Install**
9. You may need to enable "Install from unknown sources" in Settings

### Option 2: Online APK Builder (even easier)

Use an online service that wraps websites into APKs:

**PWABuilder** (Microsoft, free):
1. Go to https://pwabuilder.com
2. Enter your TaxHelper cloud URL (e.g. `https://taxhelper.onrender.com`)
3. Click **Start**
4. Click **Package for Stores** → **Android**
5. Download the APK

**AppMaker online:**
- Search "PWA to APK online" for alternatives

## ⚙️ What the app does

- First launch: asks for your TaxHelper server URL
- Enter your cloud URL (`https://taxhelper.onrender.com`) or local network address
- The app loads TaxHelper full-screen
- Pull down to refresh, back button navigates pages
- Works on any Android phone or tablet (Android 7+)

## 🔧 Build from source (for developers)

### Prerequisites
- Android Studio
- JDK 17+

### Build
```bash
git clone https://github.com/YOUR_USER/taxhelper-android.git
cd taxhelper-android
./gradlew assembleDebug
```
APK at: `app/build/outputs/apk/debug/app-debug.apk`
