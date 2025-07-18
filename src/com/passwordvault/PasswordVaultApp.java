package com.passwordvault;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PasswordVaultApp {
	private static Scanner scanner = new Scanner(System.in);
	private static Connection conn;
	private static User userManager;
	private static Credentials credentials;

	public static void main(String[] args) {
		try {
			conn = DatabaseConnection.getConnection();
			userManager = new User(conn);
			credentials = new Credentials(conn);

			System.out.println("=== Password Vault System ===");
			showMainMenu();

		} catch (SQLException e) {
			System.err.println("Database connection error: " + e.getMessage());
		}
	}

	private static void showMainMenu() {
		while (true) {
			System.out.println("\n=== MAIN MENU ===");
			System.out.println("1. User Management");
			System.out.println("2. Credential Management");
			System.out.println("3. Exit");
			System.out.print("Choose option: ");

			int choice = getIntInput();

			switch (choice) {
			case 1:
				userMenu();
				break;
			case 2:
				credentialMenu();
				break;
			case 3:
				System.out.println("Goodbye!");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private static void userMenu() {
		while (true) {
			System.out.println("\n=== USER MANAGEMENT ===");
			System.out.println("1. Register User");
			System.out.println("2. Update Username");
			System.out.println("3. Delete User");
			System.out.println("4. View All Users");
			System.out.println("5. Back to Main Menu");
			System.out.print("Choose option: ");

			int choice = getIntInput();

			switch (choice) {
			case 1:
				registerUser();
				break;
			case 2:
				updateUsername();
				break;
			case 3:
				deleteUser();
				break;
			case 4:
				viewAllUsers();
				break;
			case 5:
				return;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private static void credentialMenu() {
		while (true) {
			System.out.println("\n=== CREDENTIAL MANAGEMENT ===");
			System.out.println("1. Add Credential");
			System.out.println("2. Update Credential");
			System.out.println("3. Delete Credential");
			System.out.println("4. Delete All Credentials");
			System.out.println("5. View Credentials");
			System.out.println("6. Back to Main Menu");
			System.out.print("Choose option: ");

			int choice = getIntInput();

			switch (choice) {
			case 1:
				addCredential();
				break;
			case 2:
				updateCredential();
				break;
			case 3:
				deleteCredential();
				break;
			case 4:
				deleteAllCredentials();
				break;
			case 5:
				viewCredentialsMenu();
				break;
			case 6:
				return;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private static void registerUser() {
		System.out.print("Enter username: ");
		String username = scanner.nextLine().trim();

		if (username.isEmpty()) {
			System.out.println("Username cannot be empty!");
			return;
		}

		if (userManager.userExists(username)) {
			System.out.println("User already exists!");
			return;
		}

		if (userManager.registerUser(username)) {
			System.out.println("User registered successfully!");
		} else {
			System.out.println("Failed to register user.");
		}
	}

	private static void updateUsername() {
		System.out.print("Enter current username: ");
		String currentUsername = scanner.nextLine().trim();

		if (currentUsername.isEmpty()) {
			System.out.println("Username cannot be empty!");
			return;
		}

		if (!checkUserExists(currentUsername)) {
			return;
		}

		System.out.print("Enter new username: ");
		String newUsername = scanner.nextLine().trim();

		if (newUsername.isEmpty()) {
			System.out.println("Username cannot be empty!");
			return;
		}

		if (userManager.userExists(newUsername)) {
			System.out.println("Username '" + newUsername + "' is already taken.");
			return;
		}

		if (userManager.updateUsername(currentUsername, newUsername)) {
			System.out.println("Username updated successfully!");
		} else {
			System.out.println("Failed to update username.");
		}
	}

	private static void deleteUser() {
		System.out.print("Enter username to delete: ");
		String username = scanner.nextLine().trim();

		if (username.isEmpty()) {
			System.out.println("Username cannot be empty!");
			return;
		}

		if (!checkUserExists(username)) {
			return;
		}

		System.out.print("Are you sure? This will delete all data for this user (y/n): ");
		String confirm = scanner.nextLine();

		if (confirm.equalsIgnoreCase("y")) {
			if (userManager.deleteUser(username)) {
				System.out.println("User deleted successfully!");
			} else {
				System.out.println("Failed to delete user.");
			}
		}
	}

	private static void viewAllUsers() {
		try {
			ResultSet rs = userManager.getAllUsers();
			System.out.println("\n=== ALL USERS ===");
			boolean hasUsers = false;

			while (rs.next()) {
				hasUsers = true;
				try {
					int userId = rs.getInt("user_id");
					if (userId == 0) {
						System.out.println("Username: " + rs.getString("username"));
					} else {
						System.out.println("User ID: " + userId + ", Username: " + rs.getString("username"));
					}
				} catch (SQLException e) {
					System.out.println("Username: " + rs.getString("username"));
				}
			}

			if (!hasUsers) {
				System.out.println("No users found.");
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("Error viewing users: " + e.getMessage());
		}
	}

	private static void deleteAllCredentials() {
		String username = getValidUsername("Enter username: ");
		if (username == null) {
			return;
		}

		if (confirmAction("Are you sure? This will delete ALL credentials for user '" + username + "'")) {
			if (credentials.deleteAllCredentialsForUser(username)) {
				System.out.println("All credentials for user deleted successfully!");
			} else {
				System.out.println("Failed to delete credentials or no credentials found.");
			}
		}
	}

	// Reusable helper methods
	private static boolean checkUserExists(String username) {
		if (!userManager.userExists(username)) {
			System.out.println("Error: User '" + username + "' does not exist!");
			return false;
		}
		return true;
	}

	private static String getValidUsername(String prompt) {
		System.out.print(prompt);
		String username = scanner.nextLine().trim();

		if (username.isEmpty()) {
			System.out.println("Username cannot be empty!");
			return null;
		}

		if (!userManager.userExists(username)) {
			System.out.println("Error: User '" + username + "' does not exist!");
			System.out.println("Please register the user first.");
			return null;
		}

		return username;
	}

	private static String getValidInput(String prompt) {
		System.out.print(prompt);
		String input = scanner.nextLine().trim();

		if (input.isEmpty()) {
			System.out.println("Input cannot be empty!");
			return null;
		}

		return input;
	}

	private static String getOptionalInput(String prompt, String currentValue) {
		System.out.print(prompt + " (current: " + currentValue + ", press Enter to keep): ");
		String input = scanner.nextLine().trim();
		return input.isEmpty() ? currentValue : input;
	}

	private static boolean confirmAction(String message) {
		System.out.print(message + " (y/n): ");
		String confirm = scanner.nextLine();
		return confirm.equalsIgnoreCase("y");
	}

	private static void addCredential() {
		String username = getValidUsername("Enter username: ");
		if (username == null) {
			return;
		}

		String serviceName = getValidInput("Enter service name: ");
		if (serviceName == null) {
			return;
		}

		String loginUsername = getValidInput("Enter login username: ");
		if (loginUsername == null) {
			return;
		}

		String password = getValidInput("Enter password: ");
		if (password == null) {
			return;
		}

		if (credentials.addCredential(username, serviceName, loginUsername, password)) {
			System.out.println("Credential added successfully!");
		} else {
			System.out.println("Failed to add credential.");
		}
	}

	private static void updateCredential() {
		String username = getValidUsername("Enter username: ");
		if (username == null) {
			return;
		}

		ResultSet rs = credentials.viewCredentials(username, null);
		if (rs == null) {
			System.out.println("Failed to retrieve credentials.");
			return;
		}

		try {
			System.out.println("\n=== SELECT CREDENTIAL TO UPDATE ===");
			int credentialCount = 0;
			int[] credentialIds = new int[100];
			String[] serviceNames = new String[100];
			String[] loginUsernames = new String[100];
			String[] passwords = new String[100];

			while (rs.next()) {
				credentialCount++;
				credentialIds[credentialCount - 1] = rs.getInt("id");
				serviceNames[credentialCount - 1] = rs.getString("service_name");
				loginUsernames[credentialCount - 1] = rs.getString("login_username");
				passwords[credentialCount - 1] = rs.getString("password");
				System.out.println(credentialCount + ". Service: " + serviceNames[credentialCount - 1] + ", Login: "
						+ loginUsernames[credentialCount - 1] + ", Password: " + passwords[credentialCount - 1]);
			}
			rs.close();

			if (credentialCount == 0) {
				System.out.println("No credentials found for this user.");
				return;
			}

			System.out.print("Choose credential number: ");
			int choice = getIntInput() - 1;

			if (choice < 0 || choice >= credentialCount) {
				System.out.println("Invalid choice!");
				return;
			}

			String newServiceName = getOptionalInput("Enter new service name", serviceNames[choice]);
			String newLoginUsername = getOptionalInput("Enter new login username", loginUsernames[choice]);
			String newPassword = getOptionalInput("Enter new password", passwords[choice]);

			if (credentials.updateCredential(credentialIds[choice], newServiceName, newLoginUsername, newPassword)) {
				System.out.println("Credential updated successfully!");
			} else {
				System.out.println("Failed to update credential.");
			}
		} catch (SQLException e) {
			System.err.println("Error processing result set: " + e.getMessage());
		}
	}

	private static void deleteCredential() {
		String username = getValidUsername("Enter username: ");
		if (username == null) {
			return;
		}

		ResultSet rs = credentials.viewCredentials(username, null);
		if (rs == null) {
			System.out.println("Failed to retrieve credentials.");
			return;
		}

		try {
			System.out.println("\n=== SELECT CREDENTIAL TO DELETE ===");
			int credentialCount = 0;
			int[] credentialIds = new int[100];

			while (rs.next()) {
				credentialCount++;
				credentialIds[credentialCount - 1] = rs.getInt("id");
				System.out.println(credentialCount + ". Service: " + rs.getString("service_name") + ", Login: "
						+ rs.getString("login_username") + ", Password: " + rs.getString("password"));
			}
			rs.close();

			if (credentialCount == 0) {
				System.out.println("No credentials found for this user.");
				return;
			}

			System.out.print("Choose credential number: ");
			int choice = getIntInput() - 1;

			if (choice < 0 || choice >= credentialCount) {
				System.out.println("Invalid choice!");
				return;
			}

			if (confirmAction("Are you sure you want to delete this credential?")) {
				if (credentials.deleteCredential(credentialIds[choice])) {
					System.out.println("Credential deleted successfully!");
				} else {
					System.out.println("Failed to delete credential.");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error processing result set: " + e.getMessage());
		}
	}

	private static void viewCredentialsMenu() {
		String username = getValidUsername("Enter username: ");
		if (username == null) {
			return;
		}

		System.out.print("Enter service name (or press Enter for all services): ");
		String serviceName = scanner.nextLine().trim();

		if (serviceName.isEmpty()) {
			serviceName = null;
		}

		ResultSet rs = credentials.viewCredentials(username, serviceName);
		if (rs == null) {
			System.out.println("Failed to retrieve credentials.");
			return;
		}

		try {
			System.out.println("\n=== CREDENTIALS ===");
			boolean hasCredentials = false;

			while (rs.next()) {
				hasCredentials = true;
				System.out.println("Service: " + rs.getString("service_name") + ", Login: "
						+ rs.getString("login_username") + ", Password: " + rs.getString("password"));
			}

			if (!hasCredentials) {
				System.out.println("No credentials found.");
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println("Error processing result set: " + e.getMessage());
		}
	}

	private static int getIntInput() {
		while (true) {
			try {
				return Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.print("Invalid input. Please enter a number: ");
			}
		}
	}
}