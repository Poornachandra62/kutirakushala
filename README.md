# KutiraKushala: Micro-Factory Showcase & Local Directory

## 1. Project Overview
**KutiraKushala** (Sanskrit: *Kutira* - Cottage, *Kushala* - Skill/Artistry) is a specialized Android application designed to empower the **Cottage Industry (Kutira Udhyoga)** and home-based micro-enterprises. It serves as a localized digital bridge, enabling micro-entrepreneurs to showcase their craftsmanship and allowing consumers to discover authentic, locally-made products.

The project is built with an **offline-first philosophy**, ensuring that business owners in remote or rural areas can maintain their digital catalog without relying on a constant internet connection.

---

## 2. Key Features & Functionalities

### A. For Micro-Entrepreneurs (Business Owners)
*   **Rich Business Profiling:** 
    *   Capture detailed information: Owner Name, Business Name, Skill Area (e.g., Pottery, Handloom, Food processing).
    *   **Geographical Tagging:** Specifically designed for rural contexts with fields for Village and Taluk.
    *   **Visual Portfolio:** Capability to upload/capture photos of the primary business and the workspace/workshop.
*   **Inventory & Wholesale Management:**
    *   Multi-product cataloging per business.
    *   Fields for **Wholesale Price**, **Unit of Measure** (e.g., per kg, per piece), and **Minimum Order Quantity (MOQ)**.
*   **Capacity & Availability Tracking:**
    *   Dynamic status updates: `AVAILABLE`, `FULL` (at capacity), or `PAUSED`. This helps manage production pipelines and consumer expectations.

### B. For Consumers & Community
*   **Skill-Based Discovery:** Browse industries categorized by **Food, Craft, Textile, Home Care, Jewellery,** and more.
*   **Integrated Search:** A powerful search engine that queries across business names, owner names, and specific skill areas.
*   **One-Touch Connectivity:** Integrated dialer support allows users to call business owners directly from the app for inquiries or orders.

---

## 3. Technical System Design

### Tech Stack
*   **Language:** Kotlin (100%)
*   **UI Framework:** Android XML with **ViewBinding** for type-safe view interaction.
*   **Local Persistence:** **Room Database** (SQLite abstraction) for persistent, offline storage.
*   **Concurrency:** **Kotlin Coroutines** for smooth, non-blocking asynchronous operations.
*   **Image Handling:** **Glide** library for high-performance image loading, caching, and transformations.
*   **Architecture Components:** ViewModel, LiveData, and custom `ViewModelFactory`.

### Architectural Implementation (MVVM)
The project follows a clean **Model-View-ViewModel** architecture:
1.  **View (UI):** Activities (e.g., `MainActivity`, `BusinessDetailActivity`) observe LiveData streams to reflect the database state in real-time.
2.  **ViewModel:** Acts as the state container. It uses a `Repository` to fetch data and performs business logic (e.g., filtering search results).
3.  **Repository:** The single source of truth that coordinates data between the Room database and the rest of the application.
4.  **Model (Data):** 
    *   **Entities:** `Business` and `Product` data classes.
    *   **DAOs:** Data Access Objects defining SQL operations.
    *   **Data Integrity:** Implements Foreign Key constraints with `CASCADE` delete to ensure that if a business is removed, all its products are automatically purged.

---

## 4. UI/UX Design Philosophy
*   **Aesthetic Theme:** The app utilizes a **Nature-Inspired Palette**—Primary Green (#2E7D32) and Cream (#FFFDD0).
*   **Visual Context:** This design choice reflects the organic and home-grown nature of the cottage industries, providing a "grounded" feel that resonates with local artisans.
*   **User Flow:** Focused on minimal taps. The home screen provides an immediate overview of total registered businesses and a quick-access search button.

---

## 5. Use Case Scenarios
1.  **A Local Artisan:** Registers their home pottery workshop, uploads photos of their kiln, and lists "Clay Pots" with a wholesale price for bulk orders.
2.  **A Small-Scale Retailer:** Uses the app to find local textile weavers in a specific Taluk to source hand-woven fabrics directly from the maker.
3.  **A Community Volunteer:** Uses the app to map out all micro-factories in their village to help them get government recognition or market access.

---

## 6. Installation & Setup
1.  Clone the repository.
2.  Open in Android Studio (Hedgehog or newer recommended).
3.  Sync Gradle (Project uses AGP 8.5.2 and Gradle 8.7).
4.  Build and Run on an Android device or emulator (API 26+).
