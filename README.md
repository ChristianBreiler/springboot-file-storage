# Spring Boot File Storage System

This is a Web application to upload files and manage them in a Filesystem with Subfolders through a React frontend with user authentication. 



## Features
* **Hierarchical Storage:** Create and manage subfolders dynamically.
* **Multi-format Upload:** Support for various file types.
* **Relational Mapping:** File metadata is stored in MariaDB for fast indexing and retrieval.
* **Responsive UI:** Built with React for a smooth user experience.

---

## Tech Stack
* **Frontend:** React JS
* **Backend:** Spring Boot (Java 21), Maven
* **Database:** MariaDB / MySQL

---

## Installation & Setup

### Prerequisites
* **Node.js** (v18 or higher)
* **JDK 21**
* **MariaDB/MySQL** server running

### 1. Database Setup
The springboot backend handles creating the database automatically if mariadb is installed

### 2. File Storage Folder
The App stores files on the Computer the backend is running on and automatically creates a folder called "file_storage_folder" in the home directory with a subfolder for profile pictures for users. The system prints out when starting the application if the folder was successfully created or if it already exists.