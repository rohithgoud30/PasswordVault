# Password Vault

A simple command-line application for managing usernames and credentials, built with Java and JDBC.

## Features

*   **User Management**: Register, update, and delete users.
*   **Credential Management**: Add, update, view, and delete credentials for each user.
*   **Database Interaction**: Connects to a local database to securely store user and credential information.

## Getting Started

### Prerequisites

*   Java Development Kit (JDK)
*   A configured database and the corresponding JDBC driver.

### Running the Application

1.  **Compile the source code:**
    ```sh
    javac -d bin src/com/passwordvault/*.java
    ```
2.  **Run the application:**
    ```sh
    java -cp bin com.passwordvault.PasswordVaultApp
    ``` 