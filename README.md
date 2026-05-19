# Shale-Namma Pride

Android app for Project Title 95: **Android App Development using GenAI - Shale-Namma Pride (Education)**.

## Features

- Login page first, using demo school admin credentials.
- Native Android version converted from the referenced React/Vite `index.html` app flow.
- Daily Meal Update with a one-update-per-day rule.
- Bottom navigation for Home, Daily Meals, Facility Tour, Student Stars, and Feedback.
- Student Stars section for weekly achievers and sports winners.
- Anonymous Feedback Box for parent suggestions.
- Kannada/English style toggle button.
- Editorial school dashboard UI inspired by the referenced web app.

## Open in Android Studio

1. Open Android Studio.
2. Choose **File > Open**.
3. Select this folder:
   `C:\Users\vikas\Documents\Codex\2026-05-07\files-mentioned-by-the-user-screenshot`
4. Wait for Gradle sync.
5. Run the app on an emulator or Android phone.

## Main Files

- `app/src/main/java/com/example/shalenammapride/MainActivity.java`
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle`

## Demo Login

```text
Username: headmaster
Password: 1234
```

or

```text
Username: sdmc
Password: 1234
```

## Firebase Upgrade

This project currently stores demo updates locally so it can run immediately. For the final live version, connect Firebase Realtime Database and replace the local list updates in:

- `postMeal()`
- `postFeedback()`
- `addUpdate()`

Use Firebase paths like:

```text
schools/{schoolId}/dailyMeals/{date}
schools/{schoolId}/feedback/{feedbackId}
schools/{schoolId}/studentStars/{starId}
```
