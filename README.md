# Password Vault

A secure command-line application for managing user credentials, built with Java and MySQL.

## Features

### User Management
- Register new users
- Update usernames
- Delete users (with all associated credentials)
- View all users with auto-generated user IDs

### Credential Management
- Add credentials for services (service name, login username, password)
- Update credentials with optional field editing (press Enter to keep existing values)
- Delete individual credentials
- Delete all credentials for a user
- View credentials by user (all services or specific service)

### Security & Data
- MySQL database integration with JDBC
- Foreign key constraints ensure data integrity
- Automatic table creation and schema updates
- User existence validation before operations

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- MySQL JDBC Driver (mysql-connector-java)

## Dependencies Setup

### Download MySQL JDBC Driver

1. Download the MySQL Connector/J JAR file:
   - Visit [MySQL Connector/J Downloads](https://dev.mysql.com/downloads/connector/j/)
   - Select "Platform Independent" and download the ZIP file
   - Extract and locate `mysql-connector-java-x.x.x.jar` (or `mysql-connector-j-x.x.x.jar` for newer versions)

2. Alternative direct download:
   ```bash
   # Using wget (Linux/macOS)
   wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
   
   # Using curl
   curl -O https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
   ```

### Eclipse IDE Setup

1. **Add External JAR to Project:**
   - Right-click your project in Package Explorer
   - Select `Properties`
   - Go to `Java Build Path` > `Libraries` tab
   - Click `Add External JARs...`
   - Navigate to and select your downloaded MySQL JDBC JAR file
   - Click `Apply and Close`

2. **Module Path Configuration (for Java 9+ projects):**
   - In the same `Java Build Path` > `Libraries` tab
   - Expand `Module path` or `Class path` section
   - Ensure the MySQL JAR appears under the appropriate path
   - If using modules, add the JAR to `Module path`

3. **Verify Setup:**
   - The JAR should appear under `Referenced Libraries` in Package Explorer
   - No import errors should remain in your Java files

## Database Setup

1. Install MySQL Server
2. Create a database named `mydatabase`
3. Update database credentials in `DatabaseConnection.java`:
   - URL: `jdbc:mysql://localhost:3306/mydatabase`
   - Username: `root` (or your MySQL username)
   - Password: `rohith123` (or your MySQL password)

## Installation & Running

### Method 1: Eclipse IDE
1. Import project: `File > Import > Existing Projects into Workspace`
2. Select project root directory
3. Run: Right-click `PasswordVaultApp.java` > `Run As > Java Application`

### Method 2: Command Line
```bash
# Compile
javac -cp ".:mysql-connector-java.jar" src/com/passwordvault/*.java

# Run
java -cp ".:mysql-connector-java.jar:src" com.passwordvault.PasswordVaultApp
```

## Usage

### Main Menu
1. User Management
2. Credential Management
3. Exit

### User Management Options
1. Register User
2. Update Username
3. Delete User
4. View All Users
5. Back to Main Menu

### Credential Management Options
1. Add Credential
2. Update Credential (optional field updates)
3. Delete Credential
4. Delete All Credentials
5. View Credentials
6. Back to Main Menu

## Database Schema

### Users Table
- `username` (VARCHAR(100), PRIMARY KEY)
- `user_id` (INT, AUTO_INCREMENT, UNIQUE)

### Credentials Table
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `username` (VARCHAR(100), FOREIGN KEY)
- `service_name` (VARCHAR(100))
- `login_username` (VARCHAR(100))
- `password` (VARCHAR(100))

## Error Handling

- User existence validation
- Empty input validation
- Database connection error handling
- Graceful handling of missing database columns
 