# 📚 ReaderCity

A modern Android book reading application built with Jetpack Compose and Kotlin, designed to help users discover, track, and manage their reading journey.

## ✨ Features

- **📖 Book Discovery**: Search and browse books using Google Books API
- **🔍 Smart Search**: Advanced book search with real-time results
- **📊 Reading Progress**: Track your reading status and progress
- **🔥 Firebase Integration**: Cloud-based data storage and user authentication
- **📱 Modern UI**: Beautiful Material Design 3 interface with Jetpack Compose
- **👤 User Profiles**: Personal reading statistics and book collections
- **📝 Notes & Reviews**: Add personal notes and ratings to your books

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture with:

- **UI Layer**: Jetpack Compose screens and components
- **ViewModel Layer**: Business logic and state management
- **Repository Layer**: Data access abstraction
- **Data Layer**: Network APIs and Firebase integration

## 🛠️ Tech Stack

- **Language**: Kotlin (100%)
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Repository Pattern
- **Dependency Injection**: Dagger Hilt
- **Network**: Retrofit + Google Books API
- **Backend**: Firebase Firestore & Authentication
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose

## 📱 Screens

1. **Splash Screen** - App loading and initialization
2. **Authentication** - Login and account creation
3. **Home Screen** - Dashboard with reading overview
4. **Search Screen** - Book discovery and search
5. **Book Details** - Detailed book information
6. **Reading Stats** - Personal reading statistics
7. **Update Screen** - Manage book progress

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Kotlin 1.8+
- Android SDK 24+
- Firebase account for backend services

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/FatBoyIL/ReaderCity.git
   cd ReaderCity
   ```

2. **Firebase Setup**
   - Create a new Firebase project
   - Add your Android app to the project
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Firestore Database and Authentication

3. **Google Books API**
   - Get an API key from [Google Cloud Console](https://console.cloud.google.com/)
   - Add your API key to the constants file

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## 📂 Project Structure

```
app/src/main/java/com/jacknguyen/readerapp/
├── components/          # Reusable UI components
├── data/               # Data models and resources
├── di/                 # Dependency injection modules
├── model/              # Data classes and entities
├── navigation/         # App navigation setup
├── network/            # API interfaces and network layer
├── repository/         # Data repository implementations
├── screen/             # UI screens and ViewModels
│   ├── details/        # Book details screen
│   ├── home/           # Home dashboard
│   ├── search/         # Book search functionality
│   └── stats/          # Reading statistics
└── utils/              # Utility classes and constants
```

## 🔧 Key Components

### Data Models
- **BookReaderApp**: Main book entity with Firebase integration
- **Item**: Google Books API response model
- **Resource**: Wrapper for network responses

### Repositories
- **BookRepository**: Handles Google Books API calls
- **FireRepository**: Manages Firebase Firestore operations
- **HomeRepository**: Home screen data management

### ViewModels
- **ReaderSearchViewModel**: Book search logic
- **ReaderHomeScreenViewModel**: Home screen state management

## 🌟 Key Features Implementation

### Book Search
```kotlin
fun searchBooks(query: String) {
    viewModelScope.launch(Dispatchers.Default) {
        when(val response = bookRepository.getAllBooks(query)) {
            is Resource.Success -> {
                listOfBooks = response.data!!
                isLoading = false
            }
            is Resource.Error -> {
                isLoading = false
                Log.e("Network", "Failed getting books")
            }
        }
    }
}
```

### Firebase Integration
- Real-time data synchronization
- User authentication
- Cloud storage for book collections
- Reading progress tracking

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**FatBoyIL** - [GitHub Profile](https://github.com/FatBoyIL)

## 🙏 Acknowledgments

- Google Books API for book data
- Firebase for backend services
- Jetpack Compose team for the amazing UI toolkit
- Material Design for design guidelines

---

*Built with ❤️ using Kotlin and Jetpack Compose*
