# CrimeTrack 2.0

**CrimeTrack** — number check + community report app. Kotlin + Jetpack Compose, dark UI with green accent, 7 screens.

> **Kya asli hai (real):** number validation aur formatting (Google libphonenumber), aapki apni reports ka real database, unse banta risk score, aur asli device actions — call, WhatsApp, SMS, contacts me add, share, copy.
>
> **Kya app khud nahi karta:** kisi bhi number ka personal data (naam, address, email, photo) khud se nahi nikalta. Wo data sirf kisi **licensed data provider** ki API se milta hai — app me uske liye jagah bani hui hai (neeche "Apna API jodein").

---

## APK

- **Latest APK:** [releases/latest/download/app-debug.apk](https://github.com/rewpal5-dotcom/CrimeTrack/releases/latest/download/app-debug.apk)
- Har push par GitHub Actions APK build karke Release me daal deta hai.
- Install se pehle phone me "Install unknown apps" allow karna hoga.

---

## Screens

| # | Screen | Kaam |
|---|--------|------|
| 1 | **Home** | Number type karte hi live validation (valid/invalid, region, type), search, recent searches |
| 2 | **Quick Overview** | Real values: status, type, region, report count, risk score, top category, last report, format |
| 3 | **Number Details** | Action row (Call / WhatsApp / Copy / Contact / Share), Overview rows, Activity = asli reports list |
| 4 | **Report** | Category (Fraud / Spam / Harassment / Wrong Number / Safe) + comment + naam → save |
| 5 | **More Info** | Type, region, formats, reports, risk score, top category, last report, validation |
| 6 | **History** | All / Recent / Saved filter, delete, clear |
| 7 | **Premium** | Demo upgrade + aapke data ke stats (reports, numbers) |
| 8 | **Settings** | Dark theme, apna API URL + key, test connection, reports export, data clear |

---

## Risk score kaise banta hai

Har report ka weight:

| Category | Points |
|----------|--------|
| Fraud | +22 |
| Harassment | +16 |
| Spam | +12 |
| Wrong Number | +4 |
| Safe | −8 |

Total 0–100 me clamp hota hai. Label: 0 reports → *No reports*, <25 → *Low Risk*, <60 → *Medium Risk*, warna *High Risk*.

---

## Apna API jodein (real lookup)

`Settings → Cloud sync / apna API` me URL daalein. App ye call karega:

```
GET {url}?number=9876543210
Authorization: Bearer {api key}   (optional)
```

Expected JSON:

```json
{
  "reports": [
    {"id": "r1", "category": "Fraud", "comment": "OTP maang raha tha", "reporter": "Ravi", "time": 1730000000000}
  ]
}
```

Yahan aap koi bhi licensed number-intelligence API laga sakte hain (carrier, location, spam score). Server se aayi reports local reports ke saath merge ho jati hain. Code: `app/src/main/java/com/crimetrack/app/data/Lookup.kt`.

**Dhyan:** personal data tabhi legal hai jab provider ke paas uski permission ho aur user ka consent ho.

---

## Build

```bash
gradle assembleDebug     # APK: app/build/outputs/apk/debug/app-debug.apk
```

JDK 17, Android SDK (compileSdk 36), Gradle 8.11.1 (wrapper included).

## Project structure

```
app/src/main/java/com/crimetrack/app/
├── MainActivity.kt
├── data/
│   ├── Models.kt        # Report, NumberInfo, HistoryItem, Risk engine
│   ├── PhoneUtil.kt     # libphonenumber — real validation/formatting
│   ├── Store.kt         # device storage (reports, history, settings)
│   └── Lookup.kt        # NumberInfo builder + optional RemoteLookup
└── ui/
    ├── AppViewModel.kt  # saara state + actions
    ├── Actions.kt       # dialer, WhatsApp, SMS, contacts, share, clipboard
    ├── CrimeTrackApp.kt
    ├── components/Common.kt
    └── screens/         # Home, Result, Details, Report, MoreInfo, History, Premium, Settings
```

Design prototype: [`docs/design-prototype.html`](docs/design-prototype.html)
