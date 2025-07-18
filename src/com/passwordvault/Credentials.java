package com.passwordvault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Credentials {
	private Connection conn;

	public Credentials(Connection conn) {
		this.conn = conn;
	}

	public boolean addCredential(String username, String serviceName, String loginUsername, String password) {
		try {
			User user = new User(conn);
			if (!user.userExists(username)) {
				return false;
			}

			String query = "INSERT INTO credentials (username, service_name, login_username, password) VALUES (?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, username);
				pstmt.setString(2, serviceName);
				pstmt.setString(3, loginUsername);
				pstmt.setString(4, password);
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error adding credential: " + e.getMessage());
			return false;
		}
	}

	public boolean updateCredential(int credentialId, String newServiceName, String newLoginUsername,
			String newPassword) {
		try {
			String query = "UPDATE credentials SET service_name = ?, login_username = ?, password = ? WHERE id = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, newServiceName);
				pstmt.setString(2, newLoginUsername);
				pstmt.setString(3, newPassword);
				pstmt.setInt(4, credentialId);
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error updating credential: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteCredential(int credentialId) {
		try {
			String query = "DELETE FROM credentials WHERE id = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setInt(1, credentialId);
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting credential: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteCredentialsByService(String username, String serviceName) {
		try {
			User user = new User(conn);
			if (!user.userExists(username)) {
				return false;
			}

			String query = "DELETE FROM credentials WHERE username = ? AND service_name = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, username);
				pstmt.setString(2, serviceName);
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting credentials by service: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteAllCredentialsForUser(String username) {
		try {
			User user = new User(conn);
			if (!user.userExists(username)) {
				return false;
			}

			String query = "DELETE FROM credentials WHERE username = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, username);
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting all credentials for user: " + e.getMessage());
			return false;
		}
	}

	public ResultSet viewCredentials(String username, String serviceName) {
		try {
			User user = new User(conn);
			if (!user.userExists(username)) {
				return null;
			}

			String query;
			PreparedStatement pstmt;

			if (serviceName == null || serviceName.trim().isEmpty()) {
				query = "SELECT service_name, login_username, password, id " + "FROM credentials " + "WHERE username = ? "
						+ "ORDER BY service_name";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, username);
			} else {
				query = "SELECT service_name, login_username, password, id " + "FROM credentials "
						+ "WHERE username = ? AND service_name = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, username);
				pstmt.setString(2, serviceName);
			}

			return pstmt.executeQuery();
		} catch (SQLException e) {
			System.err.println("Error viewing credentials: " + e.getMessage());
			return null;
		}
	}
}