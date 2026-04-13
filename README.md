# NIT3213 Android Application — Final Assignment

An Android app that authenticates via the VU NIT3213 API and displays dynamic entity data across three screens: Login, Dashboard, and Details.

---

## Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM (ViewModel + StateFlow)
- **Dependency Injection:** Hilt
- **Networking:** Retrofit + Gson
- **Navigation:** Jetpack Navigation Component
- **UI:** ViewBinding, RecyclerView, Material 3
- **Testing:** JUnit4, MockK, Coroutines Test

---

## Project Structure

```
app/src/main/java/com/example/vu2/
├── data/
│   ├── api/
│   │   ├── ApiService.kt        # Retrofit endpoints (login + dashboard)
│   │   └── models.kt            # LoginRequest, LoginResponse, DashboardResponse
│   └── repository/
│       ├── LoginRepository.kt   # Handles login API call
│       └── DashboardRepository.kt # Handles dashboard API call
├── di/
│   └── NetworkModule.kt         # Hilt module — provides Retrofit + ApiService
├── ui/
│   ├── login/
│   │   ├── LoginFragment.kt
│   │   └── LoginViewModel.kt
│   ├── dashboard/
│   │   ├── DashboardFragment.kt
│   │   ├── DashboardViewModel.kt
│   │   └── EntityAdapter.kt     # RecyclerView adapter
│   └── details/
│       └── DetailsFragment.kt
└── MyApplication.kt             # @HiltAndroidApp
```

---

## How to Build and Run

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 11+
- Android SDK with API 24 minimum, API 35 target
- Internet connection (API is hosted on Render)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Mobile-Application-VU.git
   cd Mobile-Application-VU
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Click **Open**
   - Select the `Mobile-Application-VU-main` folder
   - Wait for Gradle sync to complete

3. **Run the app**
   - Connect a physical device or start an emulator (API 24+)
   - Click the green **Run** button or press `Shift + F10`

---

## Login Credentials

The app uses the Sydney campus endpoint.

| Field    | Value                  |
|----------|------------------------|
| Username | `sYourStudentID`       |
| Password | `YourFirstName`        |

Example: username `s1234567`, password `John`

> ⚠️ **Note:** The API runs on Render's free tier. The first login after inactivity may take **30–60 seconds**. This is normal — just wait for the loading spinner.

---

## API Details

| Endpoint              | Method | Description                        |
|-----------------------|--------|------------------------------------|
| `/sydney/auth`        | POST   | Authenticates user, returns keypass |
| `/dashboard/{keypass}`| GET    | Returns list of entities            |

**Base URL:** `https://nit3213api.onrender.com/`

---

## Running Unit Tests

In Android Studio:
- Open the **Terminal** tab at the bottom
- Run:
  ```bash
  ./gradlew test
  ```
- Or right-click any test file → **Run Tests**

Tests cover:
- `LoginViewModelTest` — validation errors, success, failure, network exception
- `DashboardViewModelTest` — loading state, success, error, correct keypass passed

---

## Git Commit History

```
Phase 1: Initial project setup with Hilt and Retrofit
Phase 2: Login screen with auth POST and error handling
Phase 3: Dashboard RecyclerView and Details screen complete
Phase 4: Unit tests and README
```

---

## Known Notes

- `LoginRepository.kt` uses `javax.inject.Inject` (not `jakarta`) — ensure correct import
- Dashboard entities use `Map<String, String>` to handle dynamic/unknown JSON fields at runtime
- The Details screen receives entity data via Navigation Bundle and displays all fields including description
