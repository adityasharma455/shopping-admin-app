🛠️ Shopping Admin App (Android)

A modern Android Admin Panel for managing an e-commerce platform, built entirely with Jetpack Compose, Firebase, and Supabase Storage.
This app allows admins to manage products, categories, banners, and real-time orders, all powered by Clean Architecture, MVVM, and Koin Dependency Injection.

📱 This app works together with the Shopping Customer App, sharing the same backend and real-time database.


📸 Preview

| AdminLogIn | AdminSignUp | AdminDashboardScreen | AdminProfileScreen |
|-----|------------|--------|------------|
| ![AdminLogIn](screenshots./AdminLogIn.jpeg) | ![AdminSignUp](screenshots./AdminSignUp.jpeg) | ![AdminDashboardScreen](screenshots./AdminDashboardScreen.jpeg) | ![AdminProfileScreen](screenshots./AdminProfileScreen.jpeg) | 

|  AdminSignOutScreen  | AdminOrdersStateScreen | AdminOrderStateAfterConfirmed | SuccessfullyDeliveredState | 
|-----|------------|--------|------------|
| ![AdminSignOutScreen](screenshots./AdminSignOutScreen.jpeg) | ![AdminOrdersStateScreen](screenshots./AdminOrdersStateScreen.jpeg) | ![AdminOrderStateAfterConfirmed](screenshots./AdminOrderStateAfterConfirmed.jpeg) | ![SuccessfullyDeliveredState](screenshots./SuccessfullyDeliveredState.jpeg) |

| AllProductScreen | ProductSearchState | ProductDetailsScreen | EditProductScreen | DeleteProduct |   
|-----|------------|--------|------------|--------|
| ![AllProductScreen](screenshots./AllProductScreen.jpeg) | ![ProductSearchState](screenshots./ProductSearchState.jpeg) | ![ProductDetailsScreen](screenshots./ProductDetailsScreen.jpeg) | ![EditProductScreen](screenshots./EditProductScreen.jpeg) | ![DeleteProduct](screenshots./DeleteProduct.jpeg) |

| CategoryManageScreen | AddCategoryScreen | ManageBannersScreen | AddBannerScreen | DeleteProduct |   
|-----|------------|--------|------------|--------|
| ![CategoryManageScreen](screenshots./CategoryMangaeScreen.jpeg) | ![AddCategoryScreen](screenshots./AddCategoryScreen.jpeg) | ![ManageBannersScreen](screenshots./MangaeBannersScreen.jpeg) | ![AddBannerScreen](screenshots./AddBannerScreen.jpeg) | ![DeleteProduct](screenshots./DeleteProduct.jpeg) |


🚀 Features

🔐 Authentication

* Admin Login & Signup using Firebase Authentication
* Secure session handling
* Admin profile management & logout

📊 Admin Dashboard

* Real-time order summary:
  🕒 Pending Orders
  ✅ Confirmed Orders
  🚚 Delivered Orders

* Notification indicator for new incoming orders

* Clean, scrollable dashboard UI

📦 Product Management

* Add new products with images

* Edit product details

* Delete products

* View all products

* View products by category

* Real-time product updates across customer app

🗂️ Category Management

* Add new categories

* View all categories

* Category-based product filtering

🖼️ Banner Management

* Add promotional banners

* Upload banner images to Supabase Storage

* Edit banner data

* Delete banners (Supabase image + Firebase data sync)

* Real-time banner updates on customer home screen

📬 Order Management (Real-Time)

* View all customer orders live

* Update order status:
   Pending → Confirmed → Delivered

* Order updates instantly reflected in customer app

* Built using Firebase Realtime Database

🎨 UI & UX

* Built 100% with Jetpack Compose

* Material 3 design system

* Supports Light & Dark Theme

* Animated Bottom Navigation

* Clean, scalable navigation using Compose Navigation


🧠 Architecture

This app follows Clean Architecture + MVVM:

Presentation (Compose UI + Navigation)
│
├── ViewModel (Single ViewModel handling multiple features)
│
├── Domain
│   ├── Models
│   └── UseCases
│
└── Data
    ├── Firebase (Auth + Realtime DB)
    └── Supabase (Image Storage)


✅ Key Architectural Decisions

* Single ViewModel (MyViewModel) for centralized state handling

* UseCase per feature (Product, Banner, Order, Auth, Category)

* StateFlow for reactive UI

* Koin for dependency injection


🔗 Admin ↔ Customer App Connection 
Both the Admin App and Customer App share the same Firebase backend.
However, only the Admin App directly connects to Supabase Storage.

Firebase Authentication:
- Admins and users authenticate separately

Admin App Responsibilities:
- Upload product and banner images to Supabase Storage
- Receive public image URLs from Supabase
- Store those image URLs in Firebase Realtime Database

Customer App Responsibilities:
- Fetch product, banner, and category data from Firebase
- Load images using the Supabase URLs stored in Firebase
- Does NOT connect to Supabase directly


🔁 Real-Time Synchronization
Action in Admin App     Effect in Customer App
Add/Edit Product  	    Product appears instantly
Add Banner           	  Banner shows on home screen
Update Order Status	    User sees live status update
Delete Product	        Removed from customer listing

➡️ No manual refresh required — everything is real-time.


🧾 Firebase Database Structure
const val USER_PATH = "users"
const val CATEGORY_PATH = "Categories"
const val PRODUCT_PATH = "Products"
const val BANNER_MODEL = "BannerModel"
const val ORDERS_PATH = "Orders"
const val USER_ORDERS_SUBCOLLECTION = "UserOrders"


☁️ Image Storage & Data Flow

* The Admin App uploads product and banner images to Supabase Storage.

* Supabase generates a public image URL after upload.

* This image URL is saved in Firebase Realtime Database along with product/banner data.

* The Customer App only reads data from Firebase and loads images using the stored URLs.

* The Customer App does not connect to Supabase.

Data Flow:
    Admin App → Supabase Storage → Image URL → Firebase Database → Customer App


🛠 Tech Stack

* Language: Kotlin

* UI: Jetpack Compose, Material 3

* Architecture: MVVM + Clean Architecture

* Dependency Injection: Koin

* Authentication: Firebase Auth

* Database: Firebase Realtime Database

* Storage: Supabase

* State Management: StateFlow

* Navigation: Compose Navigation

* Async: Kotlin Coroutines & Flow



📦 Related Repositories

  *   🛍️ Customer App:
      👉 https://github.com/adityasharma455/shopping-customer-app

  Both apps together form a complete real-world e-commerce system.



🎯 Learning Outcomes

* Real-time data handling with Firebase

* Admin panel architecture

* Clean Architecture implementation

* Supabase + Firebase hybrid backend

* Scalable Jetpack Compose navigation

* Order lifecycle management


👤 Author

Aditya Sharma
🎓 3rd Year Computer Science Student
📱 Android Developer 🧠 Building practical Android projects for internships

🔗 GitHub: https://github.com/adityasharma455
