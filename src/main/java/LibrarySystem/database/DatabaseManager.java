package LibrarySystem.database;

import LibrarySystem.model.*;
import LibrarySystem.ui.Display;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:library.db";
    private final Display display = new Display();

    public void connectAndCreateTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
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

            if (book.getBorrowDate() != null) pstmt.setString(6, book.getBorrowDate().toString());
            else pstmt.setString(6, null);

            if (book.getDeadLine() != null) pstmt.setString(7, book.getDeadLine().toString());
            else pstmt.setString(7, null);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<User> selectUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int id;
            String username, password, email, role;
            Role roleObj;

            while (rs.next()) {
                id = rs.getInt("id");
                username = rs.getString("username");
                password = rs.getString("password");
                email = rs.getString("email");
                role = rs.getString("role");

                roleObj = Role.valueOf(role);

                if (roleObj == Role.USER) {
                    NormalUser user = new NormalUser(username, password, email, Role.USER);
                    user.setId(id);
                    users.add(user);
                } else if (roleObj == Role.ADMIN) {
                    Admin admin = new Admin(username, password, email, Role.ADMIN);
                    admin.setId(id);
                    users.add(admin);
                }
            }

            return users;

        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<Book> selectBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean available;
            int id, year;
            String title, author, borrowedBy, borrowDate, returnDeadLine;

            while (rs.next()) {
                title = rs.getString("title");
                author = rs.getString("author");
                year = rs.getInt("year");

                Book book = new Book(title, author, year);

                id = rs.getInt("id");
                book.setId(id);

                available = rs.getBoolean("available");
                book.setAvailable(available);

                borrowedBy = rs.getString("borrowedBy");
                book.setBorrowedBy(borrowedBy);

                borrowDate = rs.getString("borrowDate");
                if (borrowDate != null) book.setBorrowDate(LocalDate.parse(borrowDate));

                returnDeadLine = rs.getString("returnDeadLine");
                if (returnDeadLine != null) book.setDeadline(LocalDate.parse(returnDeadLine));

                books.add(book);
            }

            return books;

        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateDeadLine(int id, String date) {
        String sql = "UPDATE books SET returnDeadLine = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateRole(int id, Role newRole) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newRole.toString());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE books SET available = ?, borrowedBy = ?, borrowDate = ?, returnDeadLine = ?   WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, book.isAvailable());
            pstmt.setString(2, book.getBorrowedBy());

            if (book.getBorrowDate() != null) pstmt.setString(3, book.getBorrowDate().toString());
            else pstmt.setString(3, null);

            if (book.getDeadLine() != null) pstmt.setString(4, book.getDeadLine().toString());
            else pstmt.setString(4, null);

            pstmt.setInt(5, book.getId());
            pstmt.executeUpdate();


        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            int id;
            String password, email, role;
            Role roleObj;

            if (rs.next()) {
                id = rs.getInt("id");
                password = rs.getString("password");
                email = rs.getString("email");
                role = rs.getString("role");
                roleObj = Role.valueOf(role);

                User user;

                if (roleObj == Role.ADMIN) {
                    user = new Admin(username, password, email, roleObj);
                } else {
                    user = new NormalUser(username, password, email, roleObj);
                }
                user.setId(id);
                return user;
            }

        } catch (SQLException e) {
            display.showSQLError();
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public void clearDatabase() {
        String deleteBooks = "DELETE FROM books";
        String deleteUsers = "DELETE FROM users";
        String resetIds = "DELETE FROM sqlite_sequence";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(deleteBooks);
            stmt.executeUpdate(deleteUsers);
            stmt.executeUpdate(resetIds);

        } catch (SQLException e) {
            System.out.println("Błąd podczas czyszczenia bazy testowej: " + e.getMessage());
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            String username, password, email;
            Role roleObj;

            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                email = rs.getString("email");
                roleObj = Role.valueOf(rs.getString("role"));

                User user = (roleObj == Role.ADMIN)
                        ? new Admin(username, password, email, roleObj)
                        : new NormalUser(username, password, email, roleObj);

                user.setId(id);
                return user;
            }
        } catch (SQLException e) {
            display.showSQLError();
        }
        return null;
    }

    public ArrayList<Book> selectBooksByTitle(String searchTitle) {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, "%" + searchTitle + "%");
            ResultSet rs = pstmt.executeQuery();

            boolean available;
            int id, year;
            String title, author;

            while (rs.next()) {
                title = rs.getString("title");
                author = rs.getString("author");
                available = rs.getBoolean("available");
                id = rs.getInt("id");
                year = rs.getInt("year");

                Book book = new Book(title, author, year);
                book.setAvailable(available);
                book.setId(id);
                books.add(book);
            }

            return books;


        } catch (SQLException e) {
            display.showSQLError();
            return null;
        }
    }
}
