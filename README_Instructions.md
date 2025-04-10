# SkySync

SkySync is a smart outdoor activity planner and wardrobe assistant that integrates real-time weather data to help users make informed decisions about their daily plans. The system provides weather forecasts, personalized activity recommendations, and outfit suggestions based on current conditions. Whether the weather is ideal for outdoor activities or unfavorable for going outside, SkySync ensures that users are always prepared, offering both outdoor and indoor activity recommendations.

---

## Features

1. **Weather Forecast Dashboard (Web Application)**:
   - Displays real-time and forecasted weather conditions with interactive visualizations.

2. **Activity Recommendation (Web Application)**:
   - Suggests outdoor or indoor activities based on current weather conditions.

3. **Schedule Activity (Web Application)**:
   - Allows users to schedule selected recommended activities.

4. **Wardrobe Recommendation (Mobile Application)**:
   - Suggests appropriate clothing based on the current weather, including UV protection alerts.

5. **Login Authentication (Mobile Application)**:
   - Secure access through password authentication with encrypted credentials.

6. **Daily Weather Notification (Mobile Application)**:
   - Sends notifications of daily weather updates and reminders for scheduled activities.

---

## Prerequisites

Before setting up the project, ensure you have the following installed:

1. **Backend Requirements**:
   - Java 17 or higher
   - Maven 3.9.9 or higher
   - MySQL 8.0 or higher

2. **Frontend Requirements**:
   - Android Studio (for the mobile application)
   - Node.js and npm (if a web frontend is added later)

---

## Setup Instructions

### **1. Backend Setup**

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/IT342-G3-SKYSYNC.git
   cd IT342-G3-SKYSYNC/backend

2. Configure the database:
    Open src/main/resources/application.properties and update the database credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/dbskysync?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

3. Run the backend:
./mvnw spring-boot:run

Dependencies: 
    Backend Dependencies
    Spring Boot (Web, Security, Data JPA)
    MySQL Connector
    JSON Web Token (JWT)
    Lombok
    
Mobile Frontend Dependencies
    AndroidX Libraries
    Retrofit (for API calls)
    Gson (for JSON parsing)
    Material Design Components
