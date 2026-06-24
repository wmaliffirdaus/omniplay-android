# 🎮 OmniPlay

> A modern Android booking platform for gaming studios, allowing users to discover venues, switch between branches, reserve gaming stations, and manage bookings seamlessly.

---

## 📱 Overview

OmniPlay is an Android application built with **Kotlin** and **Jetpack Compose** that simplifies the process of discovering and booking gaming experiences across multiple gaming studios and branches.

Users can browse gaming venues, check real-time availability, select gaming stations such as **PlayStation 5**, **Nintendo Switch**, **Racing Simulators**, and **Private Gaming Rooms**, then complete reservations through a streamlined booking flow.

The project follows a **Single-Activity Architecture** and leverages **MVVM (Model-View-ViewModel)** principles to ensure scalability, maintainability, and separation of concerns.

---

## ✨ Features

### 🔐 Authentication

* User Registration
* User Login
* Session Management

### 🏠 Home Dashboard

* Featured Gaming Studios
* Promotions & Offers
* Popular Venues
* Quick Access Navigation

### 🔍 Explore Studios

* Browse Gaming Studios
* Search Venues
* Filter Gaming Experiences
* Multi-Branch Support

### 🎮 Studio Details

* Studio Information
* Branch Selection
* Available Gaming Stations
* Pricing Information
* Studio Gallery

### 📅 Booking System

* Select Branch
* Select Gaming Station
* Choose Date & Time
* Booking Confirmation
* Booking History

### 🔔 Notifications

* Booking Reminders
* Promotions
* System Announcements

### 👤 User Profile

* Personal Information
* Account Management
* Preferences
* Settings

---

## 🏗 Architecture

The application follows the **MVVM Architecture Pattern**.

```text
UI (Jetpack Compose)
        │
        ▼
ViewModel
        │
        ▼
Repository
        │
        ▼
Room Database
```

### Benefits

* Clear Separation of Concerns
* Easier Testing
* Scalable Codebase
* Maintainable Architecture
* Reactive UI State Management

---

## 🛠 Tech Stack

### Language

* Kotlin

### UI

* Jetpack Compose
* Material Design 3

### Architecture

* MVVM
* Repository Pattern
* Single Activity Architecture

### Database

* Room Database
* SQLite

### State Management

* ViewModel
* StateFlow
* Compose State

### Build System

* Gradle Kotlin DSL

---

## 📂 Project Structure

```text
.
├── assets/                          # Static assets and design resources
├── gradle/                          # Gradle wrapper files
├── app/                             # Main application module
│   ├── src/
│   │   ├── androidTest/             # Instrumented unit tests running on physical devices/emulators
│   │   ├── test/                    # Local JVM unit tests
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── example/
│   │       │           ├── data/                      # Data layer (Local DB, Room, Entities, DAOs)
│   │       │           │   ├── AppDatabase.kt         # Room Database configuration
│   │       │           │   ├── Daos.kt                # Database access objects
│   │       │           │   └── Entities.kt            # Room database table definitions
│   │       │           │
│   │       │           ├── repository/                # Repository layer (Data source coordinator)
│   │       │           │   └── OmniPlayRepository.kt  # Single source of truth for app data
│   │       │           │
│   │       │           ├── ui/                        # Presentation layer (UI & Theme)
│   │       │           │   ├── screens/               # Compose Screens (Views)
│   │       │           │   │   ├── AuthScreens.kt             # Authentication flow screens
│   │       │           │   │   ├── BookingProcessScreens.kt   # Step-by-step booking screens
│   │       │           │   │   ├── BookingsScreen.kt          # User bookings list/history
│   │       │           │   │   ├── ExploreScreen.kt           # Search & Discovery page
│   │       │           │   │   ├── HomeScreen.kt              # App dashboard dashboard/landing
│   │       │           │   │   ├── NotificationScreen.kt      # User alerts and updates
│   │       │           │   │   ├── ProfileScreen.kt           # Account and settings details
│   │       │           │   │   └── StudioDetailsScreen.kt     # Detail page for booking entities
│   │       │           │   │
│   │       │           │   └── theme/                 # Styling and theme definitions
│   │       │           │       ├── Color.kt           # Color definitions
│   │       │           │       ├── Theme.kt           # Jetpack Compose Theme configuration
│   │       │           │       └── Type.kt            # Typography configurations
│   │       │           │
│   │       │           ├── OmniPlayViewModel.kt       # Viewmodel handling UI state & business logic
│   │       │           └── MainActivity.kt            # Application entry point (Single-Activity host)
│   │       │
│   │       ├── res/                         # Android application resources
│   │       │   ├── drawable/                # Vector assets & static images
│   │       │   ├── mipmap-anydpi-v26/       # Adaptive launcher icons XML
│   │       │   ├── mipmap-*/                # Launcher icons for various screen densities
│   │       │   ├── values/                  # Native styling XML values
│   │       │   │   ├── colors.xml           # Legacy / System UI color definitions
│   │       │   │   ├── strings.xml          # Localization and static text definitions
│   │       │   │   └── themes.xml           # System theme parent styles
│   │       │   └── xml/                     # Miscellaneous XML configuration files
│   │       │
│   │       └── AndroidManifest.xml          # App manifest declaring permissions, activities, etc.
│   │
│   ├── .gitignore                   # App-level Git ignore list
│   ├── build.gradle.kts             # App-level build configuration & dependencies
│   └── proguard-rules.pro           # ProGuard rules for code shrinking and obfuscation
│
├── .env.example                     # Reference environment variables template
├── .gitignore                       # Project-level Git ignore list
├── build.gradle.kts                 # Project-level build configuration (plugins definition)
├── gradle.properties                # Project-wide JVM, memory, and compiler flags
├── local.properties                 # Local SDK and environment configurations (ignored by Git)
├── metadata.json                    # Configuration or metadata deployment file
└── settings.gradle.kts              # Module declaration and plugin repository settings
```

---

## 📸 Screens

### Authentication

* Login Screen
* Registration Screen

### Home

* Dashboard
* Promotions
* Featured Studios

### Explore

* Studio Discovery
* Search & Filter

### Studio Details

* Branch Information
* Gaming Station Availability

### Booking Flow

* Branch Selection
* Station Selection
* Date & Time Selection
* Booking Review
* Booking Confirmation

### User Area

* My Bookings
* Notifications
* Profile

---

## 🚀 Getting Started

### Prerequisites

* Android Studio Meerkat or newer
* Android SDK 24+
* JDK 17
* Gradle 8+

### Installation

```bash
git clone https://github.com/your-username/omniplay-android.git

cd omniplay-android
```

Open the project in Android Studio.

Run Gradle Sync.

Build and launch the application:

```bash
./gradlew assembleDebug
```

---

## 🔮 Future Enhancements

* Firebase Authentication
* Push Notifications (FCM)
* Online Payments
* QR Code Check-In
* Loyalty Rewards System
* Membership Plans
* Tournament Registration
* Multi-Branch Analytics
* Cloud Backend Integration
* Dark Mode Support

---

## 📈 Roadmap

### Phase 1 — MVP

* Authentication
* Studio Discovery
* Booking System
* Booking Management

### Phase 2

* Notifications
* Reviews & Ratings
* Favorites

### Phase 3

* Membership System
* Rewards Program
* Tournament Events

### Phase 4

* Real-Time Availability
* Payment Gateway Integration
* Admin Dashboard

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome.

Feel free to fork the repository and submit a pull request.

---

## 📄 License

This project is intended for educational, portfolio, and demonstration purposes.

---

## 👨‍💻 Author

**Alif Firdaus**

Mobile Developer • Kotlin • Flutter • Full Stack Development

Built with ❤️ using Kotlin and Jetpack Compose.
