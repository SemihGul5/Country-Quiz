# 🌍 Country - Flag & Map Quiz Game

[![Get it on Google Play](https://upload.wikimedia.org/wikipedia/commons/7/78/Google_Play_Store_badge_EN.svg)](https://play.google.com/store/apps/developer?id=Abrebo+Studio)

Country Quiz is a fun, engaging, and educational app that allows users to test and expand their knowledge of countries, flags, maps, and capitals worldwide. Perfect for geography enthusiasts, students, and anyone interested in learning more about the world around us, Country Quiz offers various game modes, scoring features, and an immersive for a visually comfortable experience.

## 📲 Key Features
 - Flag & Map Recognition: Identify country flags, locate countries on a map, and test your knowledge of world capitals.
 - Multiple Game Modes:
 - Classic Mode: Test your general knowledge in a relaxed, standard quiz format.
   - Ranking Mode: Compete to achieve a high score and place on the global leaderboard.
   - Capital Quiz: Test your memory and learn country capitals in an engaging format.
   - Country Locator: Locate countries on a map to improve your geographical awareness.
   - Population Guess: Challenge yourself by estimating the population of various countries.
 - Score Tracking & Global Leaderboard: Track your scores across various quizzes, compete globally, and see where you rank among players worldwide.
 - Dark Mode: Enjoy a comfortable viewing experience with an eye-friendly dark theme.
 - Ad-Supported Free Gameplay: The app is free to use, supported by AdMob for non-intrusive advertisements that allow us to keep content accessible to all users.

## 📱 Screenshots
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/c80e6322-3419-478c-9640-fb2981443ab4" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/ec339cd1-909c-4412-b7f5-8c81d8719fa1" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/dfb03150-9c5a-4424-bd08-2ef947151c60" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/3c87bf58-2b31-45fc-a51e-6827b3dc136d" width="250"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/9ed5661e-021b-466a-ae55-73216e29002a" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/e6bc1320-3f80-4355-b8fa-a1915dfe993b" width="250"/></td>
    <td><img src="https://github.com/user-attachments/assets/ef221469-ec11-4290-8174-d99f54bb16a6" width="250"/></td>
  </tr>
</table>

## 🛠️ Technologies Used
 - Kotlin - Main programming language
 - Firebase Database: For data storage and synchronization across users.
 - Firebase Authentication - For user login and authentication
 - AdMob - Integrated advertisements for revenue generation
 - Hilt (Dagger) - Dependency injection for efficient resource management
 - MVVM Architecture - Using ViewModel and LiveData to manage UI-related data

## 📂 Detailed Structure
The project is organized into several key packages, each serving a distinct purpose to ensure clean architecture and maintainability.

 - data:

   - datasource:
This package contains classes responsible for data retrieval, serving as the primary interface for accessing data. 
The Repository pattern is implemented here, where each repository abstracts the complexities of data operations. The data source classes provide a seamless way to interact with data, enabling easy integration and testing.
Example files:
DataSource.kt: Manages user network calls and fetches data from APIs. (or Firebase)
GameDataSource.kt: Manages game call and fetches data from firebase database.

 - model:
This package defines data models representing objects such as leagues and teams, using data classes in Kotlin for easy data handling.

 - di (Dependency Injection):
This package configures Hilt for dependency injection, simplifying the management of app-wide components and their lifecycles.
   - Example files:
     - AppModule.kt: Provides singleton instances for core dependencies, such as database and network clients.
       
 - ui:

   - adapter:
Contains RecyclerView adapters responsible for displaying lists of game and ranks, handling user interactions efficiently.
   - Example files:

   - fragment:
Houses fragments that encapsulate the UI components and navigation logic for different screens in the application.

   - viewmodel:
Contains ViewModel classes that manage business logic and provide data to the UI, ensuring a reactive and responsive user experience.

## 🛠️ Repository Pattern
The repository pattern centralizes data operations and decouples data sources from the UI layer, enhancing testability and maintainability. By using repositories, the application can easily switch between local and remote data sources, ensuring a consistent and efficient data retrieval process.
