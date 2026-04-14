# Spring Boot File Storage System

This is a web application to upload files and manage them in a file system with subfolders through a React frontend with user authentication.



## Project Structure
* **springboot-file-storage/** 
* **frontend/**           # React application (Vite)
* **backend/**            # Spring Boot application

---

## Tech Stack
* **Frontend:** React JS, Tailwind CSS
* **Backend:** Spring Boot (Java 21), Maven
* **Database:** MariaDB / MySQL

---

## Authentication
* Uses JWT-based authentication

---

## Installation & Setup

### Prerequisites
* **Node.js** (v18 or higher)
* **JDK 21**
* **MariaDB/MySQL** server running

# Frontend 

## 1. Backend url configuration
Go to 'src/api/axiosConfig' and enter the URL of your backend under 'baseURL: "http://localhost:8080"' (replace localhost with your URL).

Run 'npm install' in your terminal to install required dependencies. Afterward, run 'npm run dev' to start the application, which should then start on 'localhost:5173.'

# Backend
The backend will start on localhost:8080.
The 'src/resources/application.properties' file holds a blueprint for all the necessary configurations for the backend app, including database configurations, security, and unique app settings.

### 1. Database Setup
The Spring Boot backend handles creating the database automatically if MariaDB is installed through the '?createDatabaseIfNotExist=true' part. 
Enter the URL, user, and password for your MariaDB database, and the application will connect and configure automatically.

### 2. File Storage Folder
The app stores files on the computer the backend is running on and automatically creates a folder called "file_storage_folder" in the home directory with a subfolder for profile pictures for users. The system prints out when starting the application if the folder was successfully created or if it already exists.
Alternatively, you can enter a custom path under 'spring.files.folder.custom.folder.path' where files will then be stored.
Default location:
- Linux/Mac: /home/<user>/file_storage_folder
- Windows: C:\Users\<user>\file_storage_folder

## 3. Frontend url configuration
Enter the URL of your frontend under 'app.frontend.url'

# Deployment
* 1. Start database
* 2. Configure backend (application.properties)
* 3. Run the backend (./mvnw spring-boot:run or run it through your IDE).
* 4. Configure frontend (axiosConfig)
* 5. Run the frontend (npm install + npm run dev).

