# Spring Boot File Storage System

This is a Web application to upload files and manage them in a Filesystem with Subfolders through a React frontend with user authentication. 



## Project Structure
springboot-file-storage/
├── frontend/             # React application (Vite)
└── backend/              # Spring Boot application

---

## Tech Stack
* **Frontend:** React JS, Tailwind CSS
* **Backend:** Spring Boot (Java 21), Maven
* **Database:** MariaDB / MySQL

---

## Installation & Setup

### Prerequisites
* **Node.js** (v18 or higher)
* **JDK 21**
* **MariaDB/MySQL** server running

# Frontend 

## 1. Backend url configuration
Go to 'src/api/axiosConfig' and enter the url of your backend under 'baseURL: "http://localhost:8080"' (replace localhost with your url).

Run 'npm install' in your terminal to install required dependancies. Afterwards run 'npm run dev' to start the application which should then start on 'localhost:5173'.

# Backend

The 'src/resources/application.properties' file holds a blueprint for all the necessary configurations for the backend app including database configurations, security and unique App settings

### 1. Database Setup
The springboot backend handles creating the database automatically if mariadb is installed through the '?createDatabaseIfNotExist=true' part. 
Enter the url, user and password for your MariaDB Database and the application will connect and configure automatically.

### 2. File Storage Folder
The App stores files on the Computer the backend is running on and automatically creates a folder called "file_storage_folder" in the home directory with a subfolder for profile pictures for users. The system prints out when starting the application if the folder was successfully created or if it already exists.
Alternativly you can enter a custom path under 'spring.files.folder.custom.folder.path' where files will then be stored under.

## 3. Frontend url configuration
Enter the url of your frontend under 'app.frontend.url'