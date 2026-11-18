# CrimeTrack

**CrimeTrack** — number info finder ka Android app. Kotlin + Jetpack Compose me bana hua, dark UI with green accent, 6 screens — bilkul design prototype jaisa.

> Ye app abhi **offline demo database** par chalta hai (dummy records). Asli lookup API jodne ki jagah `app/src/main/java/com/crimetrack/app/data/Lookup.kt` me ready hai.

---

## APK download

Har push par GitHub Actions APK build karta hai:

- **Latest APK:** [releases/latest/download/app-debug.apk](https://github.com/rewpal5-dotcom/CrimeTrack/releases/latest/download/app-debug.apk)
- Ya **Actions** tab → latest run → `crimetrack-apk` artifact.

Install karne se pehle phone me "Unknown sources / Install unknown apps" allow karna hoga.

---

## Screens

| # | Screen | Kya karta hai |
|---|--------|----------------|
| 1 | **Home** | +91 ke saath 10-digit number search, recent searches, security note |
| 2 | **Quick Overview** | Profile card + 8 info tiles (Name, Email, Location, Operator, Circle, WhatsApp, Risk Score, Caller ID) |
| 3 | **Number Details** | 11 detail rows, Overview / More Info / Activity tabs, Save to Contacts |
| 4 | **History** | All / Viewed / Saved filter, delete entry, clear history |
| 5 | **More Info** | Alternate Numbers, Social Profiles, Address, Family, Business, Education, Vehicle, Voter ID, Aadhaar — tap par detail dialog |
| 6 | **Premium** | Go Premium card, 7 features, demo upgrade |
| 7 | **Settings** | Dark theme, demo mode, data clear, about |

Bottom navigation: Home · History · Premium · Settings.

---

## Features

- Kotlin + Jetpack Compose (Material 3), single-activity
- Dark theme (green `#22C55E` accent) + light theme toggle
- Search history aur saved contacts `SharedPreferences` me save (app band karne par bhi rehte hain)
- Number validation (10 digit, 6-9 se shuru)
- Loading state, Toast messages, back-navigation handling
- Custom launcher icon (shield + magnifier)
- Poora offline — internet ki zaroorat nahi

---

## Project structure

```
app/src/main/java/com/crimetrack/app/
├── MainActivity.kt          # entry point, theme + back handling
├── data/
│   ├── Models.kt            # NumberRecord, HistoryItem, MoreInfoItem
│   ├── DemoData.kt          # demo database (dummy records)
│   ├── Lookup.kt            # LookupSource interface — real API yahan jodo
│   └── Store.kt             # SharedPreferences (history, saved, settings)
└── ui/
    ├── AppViewModel.kt      # saara state + actions
    ├── CrimeTrackApp.kt     # scaffold + screen switching
    ├── theme/Theme.kt       # colors, dark/light scheme
    ├── components/Common.kt # Avatar, InfoTile, ListRow, TopBar, BottomBar, Tabs
    └── screens/             # Home, Result, Details, MoreInfo, History, Premium, Settings
```

Design ka original HTML prototype: [`docs/design-prototype.html`](docs/design-prototype.html)

---

## Khud build karna

```bash
# Android Studio me kholo, ya:
gradle assembleDebug        # APK: app/build/outputs/apk/debug/app-debug.apk
```

Requirements: JDK 17, Android SDK (compileSdk 36), Gradle 8.11.1 (wrapper included).

---

## Real lookup kaise jodein

`data/Lookup.kt` me `LookupSource` interface hai:

```kotlin
class MyApiLookupSource(private val apiKey: String) : LookupSource {
    override suspend fun lookup(number: String): NumberRecord {
        // apni legal API call karo (Retrofit / HttpURLConnection)
        // response ko NumberRecord me map karo
    }
}
```

Phir `AppViewModel` me `Lookup.current` ki jagah apna source de do. `INTERNET` permission manifest me pehle se hai.

**Dhyan:** kisi bhi number ka personal data sirf tab legal hai jab data source ke paas uski permission ho aur user ka consent ho. Isliye default source demo data hai.

---

## Note

App ke saare naam, numbers aur records **kalpanik (dummy)** hain — kisi asli vyakti ka data nahi. Ye ek UI + app skeleton hai; aage features aap add kar sakte hain.
