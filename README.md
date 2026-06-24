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
├── assets/
├── gradle/
├── app/
│   ├── src/
│   │   ├── androidTest/
│   │   ├── test/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/example/
│   │       │       ├── data/
│   │       │       ├── repository/
│   │       │       ├── ui/
│   │       │       │   ├── screens/
│   │       │       │   └── theme/
│   │       │       ├── OmniPlayViewModel.kt
│   │       │       └── MainActivity.kt
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── metadata.json
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
