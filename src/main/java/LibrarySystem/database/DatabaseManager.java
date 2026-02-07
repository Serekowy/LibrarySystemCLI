package LibrarySystem.database;

import LibrarySystem.model.Book;
import LibrarySystem.model.User;
import LibrarySystem.ui.Display;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:library.db";
    private final Display display = new Display();

    public void connectAndCreateTables() {
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            if(conn != null) {
                display.showConnectionSucces();
                createTables(conn);
            }

        } catch (SQLException e) {
            display.showConnectionError();
        }
    }

    public void createTables(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT NOT NULL,
                    role TEXT NOT NULL
                );
                """;

        statement.execute(createUsersTable);

        String createBooksTable = """
                CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                year INTEGER NOT NULL,
                available BOOLEAN NOT NULL,
                borrowedBy TEXT,
                borrowDate TEXT,
                returnDeadLine TEXT
                );
                """;

        statement.execute(createBooksTable);
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO users VALUES (NULL, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            display.showSQLError();
        }
    }

    public void insertBook(Book book) {
        String sql = "INSERT INTO books VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            pstmt.setBoolean(4, book.isAvailable());
            pstmt.setString(5, book.getBorrowedBy());

            if(book.getBorrowDate() != null) pstmt.setString(6, book.getBorrowDate().toString());
            else pstmt.setString(6, "null");

            if(book.getDeadLine() != null) pstmt.setString(7, book.getDeadLine().toString());
            else pstmt.setString(7, "null");

            pstmt.executeUpdate();

        } catch (SQLException e) {
            display.showSQLError();
        }
    }
}
