# 🚀 WAFA QUICKEATS ⚡
**Fast Food. Live Tracking. Smart Delivery.**

> **Brand:** Wafa Zone by Mehedi364  
> **Created & Developed by:** Md. Mehedi Hasan / Mehedi364  
> **Currency:** ৳ BDT | **Languages:** বাংলা (Bangla) & English

---

## 📱 Project Overview

**Wafa QuickEats ⚡** is a production-grade Android food delivery platform engineered with a modern Jetpack Compose architecture, Android Foreground Service for real-time GPS tracking, offline Room database queue, and a lightweight, InfinityFree-compatible PHP 8.x + MySQL REST backend.

---

## 🌟 Key Features

### 👤 Customer Experience
- **Smart Browsing:** Search food items, filter by categories (Burgers, Biryani, Pizza, Fried Chicken, Shawarma, Drinks, Desserts), and explore nearby restaurants.
- **Customizable Menu:** Support for meal variants (Regular, Monster Patty) and optional add-ons (Extra cheese, beef bacon, dips).
- **Cart & Discounts:** Real-time cost calculation with BDT currency, delivery fee, 5% VAT, and voucher coupons (`WAFA20` for 20% discount).
- **Checkout & Location:** Multiple saved addresses (Home, Office, Other), map pin coordinates, floor/flat instructions, and payment methods (bKash, Nagad, Rocket, Cash on Delivery, Cards).
- **Live Road Tracking:** Visual map canvas rendering restaurant pin, live moving rider pin with bearing angle and speed telemetry, customer destination, and polyline route.
- **OTP Verification:** Secure 4-digit OTP shown to customer for safe hand-over verification.
- **Digital Tax Invoices:** Official branded digital receipts with itemized breakdown (`WQE-INV-YYYYMMDD-XXXXXX`).
- **Points & Loyalty Ledger:** Earn points on orders and reviews; track ledger history.

### 🛵 Rider System & Real GPS
- **Live Fleet Telemetry:** Android `ForegroundService` with `location` type captures real latitude, longitude, accuracy, speed (km/h), bearing, and timestamps.
- **Offline GPS Resilience:** When connection drops, coordinates are saved in local Room database (`gps_queue`) and synced in batches upon reconnection.
- **Rider Operational Notes:** Log road traffic, construction delays, or delivery issues tagged with real GPS coordinates.
- **OTP Delivery Completion:** Rider validates delivery only when the customer's 4-digit OTP matches backend records.

### 🍽️ Restaurant Kitchen Portal
- **Real-time Order Workflow:** Incoming orders -> Accept & Cook -> Mark Food Ready for Rider.
- **Menu Management:** Toggle item availability and update estimated kitchen preparation times.

### 🛡️ Admin Master Control
- **System Health Monitor:** Live status of REST API, database, GPS service, Room cache, and AI providers.
- **Emergency Order Pause:** Immediate switch to pause incoming orders during peak congestion or adverse weather.
- **Verification Center:** Review and approve restaurants and delivery riders.
- **AI Provider Manager & Live Test:** Configure and test free (OpenRouter, Gemini, Groq) and premium (OpenAI, Anthropic) AI endpoints.
- **Audit Logs:** Immutable audit trail of administrative and operational actions.

---

## 🏗️ Technical Architecture

```text
WafaQuickEats/
├── app/                      # Android Jetpack Compose Application
│   ├── src/main/java/com/example/
│   │   ├── WafaQuickEatsApp.kt     # Application class & notification channels
│   │   ├── MainActivity.kt         # Edge-to-edge entry point & permission flow
│   │   ├── data/
│   │   │   ├── model/              # User, Order, Restaurant, GPS, Notes models
│   │   │   ├── local/              # Room database (Daos, Entities)
│   │   │   ├── remote/             # Retrofit & OkHttp REST client
│   │   │   ├── repository/         # Unified repository & state machine
│   │   │   └── location/           # RiderLocationService (Foreground Service)
│   │   └── ui/
│   │       ├── theme/              # Color, Theme, Typography
│   │       ├── components/         # LiveTrackingMapCanvas, TopBar, BottomBar, Modals
│   │       └── screens/            # Customer, Rider, Restaurant, Admin screens
│   └── build.gradle.kts
├── backend/                  # PHP 8.x REST API (InfinityFree & cPanel ready)
│   ├── config/               # database.php, cors.php
│   ├── api/                  # health, orders, gps sync, ai live test
│   └── .htaccess
├── database/                 # MySQL Database
│   └── schema.sql            # Full 30+ tables schema with seed data
├── .github/workflows/        # Automated Build Pipeline
│   └── android-build.yml     # GitHub Actions workflow (Debug APK, AAB, size check)
├── .build-outputs/           # Build output artifact directory
│   └── app-debug.apk         # 23 MB fully compiled, installable APK
├── APK_DOWNLOAD/             # Direct download folder
│   └── app-debug.apk
└── README.md
```

---

## ⚡ Automated GitHub Actions Pipeline

The included GitHub Actions workflow (`.github/workflows/android-build.yml`):
1. Sets up Java 17 and Gradle environment.
2. Compiles the Android debug APK using Gradle.
3. Validates that the APK exists and exceeds 1 MB (actual size ~23 MB).
4. Copies the output to `.build-outputs/app-debug.apk` and `APK_DOWNLOAD/app-debug.apk`.
5. Uploads artifacts to GitHub Actions for direct APK and AAB downloads.

---

## 🌐 InfinityFree Backend Deployment

1. Create a free account on [InfinityFree](https://www.infinityfree.com/).
2. Create a MySQL database via the vPanel control panel.
3. Import `database/schema.sql` via phpMyAdmin.
4. Upload all files from the `backend/` folder into your `htdocs/` directory.
5. In `backend/config/database.php`, enter your InfinityFree MySQL hostname, database name, username, and password.
6. Verify deployment by visiting `https://your-domain.infinityfreeapp.com/api/health.php`.

---

## 🤖 OpenRouter & AI Provider Setup

1. Sign up on [OpenRouter](https://openrouter.ai/) to obtain an API key.
2. In the Admin Master Control screen, navigate to **AI Providers**.
3. Select OpenRouter and run **API Live Test** to confirm connectivity.
4. If AI is toggled OFF, the entire application operates smoothly with core services intact.

---

## 📜 Credits & License

**Wafa QuickEats ⚡**  
Designed, Built, and Maintained by **Md. Mehedi Hasan (Mehedi364)**.  
Wafa Zone — All Rights Reserved.
