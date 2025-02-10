import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/signupsignin?useSSL=false"; // MYSQL DB
    private static final String DB_USER = "root"; // MySQL username
    private static final String DB_PASSWORD = "9689324198"; // MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Newline

            switch (choice) {
                case 1:
                    signUp(scanner);
                    break;
                case 2:
                    signIn(scanner);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void signUp(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Checking if username already exists
            String checkUserQuery = "SELECT * FROM info WHERE username = ?";
            try (PreparedStatement checkUserStmt = conn.prepareStatement(checkUserQuery)) {
                checkUserStmt.setString(1, username);
                ResultSet rs = checkUserStmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Username already exists. Please choose a different username.");
                    return;
                }
            }

            // Inserting new user
            String insertUserQuery = "INSERT INTO info (username, password) VALUES (?, ?)";
            try (PreparedStatement insertUserStmt = conn.prepareStatement(insertUserQuery)) {
                insertUserStmt.setString(1, username);
                insertUserStmt.setString(2, password);
                insertUserStmt.executeUpdate();
                System.out.println("Sign up successful!");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void signIn(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM info WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Sign in successful!");
                } else {
                    System.out.println("Invalid username or password.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}