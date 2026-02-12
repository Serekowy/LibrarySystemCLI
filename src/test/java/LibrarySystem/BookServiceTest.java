package LibrarySystem;

import LibrarySystem.database.DatabaseManager;
import LibrarySystem.model.Book;
import LibrarySystem.model.NormalUser;
import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private final DatabaseManager databaseManager = new DatabaseManager();
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(databaseManager);
        databaseManager.connectAndCreateTables();
        databaseManager.clearDatabase();
    }

    @Test
    void shouldAddBook() {
        String title = "Harry Potter";

        Book book = new Book(title, "Rowling", 2000);
        boolean result = bookService.addBook(book);

        Book bookInDb = returnBookFromDb(title, bookService);

        assertTrue(result);
        assertEquals(1, bookService.getBooks().size());
        assertTrue(bookInDb.getId() > 0);
    }

    @Test
    void shouldRemoveBook() {
        String title = "Harry Potter";

        Book book = new Book(title, "Rowling", 2000);
        bookService.addBook(book);

        Book bookInDb = returnBookFromDb(title, bookService);

        assertTrue(bookService.removeBook(bookInDb.getId()));
        assertEquals(0, bookService.getBooks().size());
    }

    @Test
    void shouldBorrowBook() {
        String title = "Harry Potter";

        Book book = new Book(title, "Rowling", 2000);
        bookService.addBook(book);

        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        databaseManager.insertUser(user);

        boolean result = bookService.borrowBook("Harry Potter", "Uzytkownik");
        assertTrue(result);

        User userInDb = returnUserFromDb(user.getUsername(), databaseManager.selectUsers());
        Book bookInDb = returnBookFromDb(title, bookService);

        assertFalse(bookInDb.isAvailable());
        assertNotEquals(null, bookInDb.getBorrowDate());
        assertNotEquals(null, bookInDb.getDeadLine());
        assertEquals(userInDb.getUsername(), bookInDb.getBorrowedBy());
    }

    @Test
    void shouldNotBorrowBorrowedBook() {
        String title = "Harry Potter";

        Book book = new Book(title, "Rowling", 2000);

        bookService.addBook(book);

        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        User user1 = new NormalUser("InnyUzytkownik", "123", "inny@gmail.com", Role.USER);
        databaseManager.insertUser(user);
        databaseManager.insertUser(user1);

        User userInDb = returnUserFromDb(user.getUsername(), databaseManager.selectUsers());
        User userInDb1 = returnUserFromDb(user1.getUsername(), databaseManager.selectUsers());

        bookService.borrowBook(book.getTitle(), userInDb.getUsername());
        boolean result = bookService.borrowBook(title, userInDb1.getUsername());

        assertFalse(result);

        Book bookInDb = returnBookFromDb(title, bookService);

        assertEquals(userInDb.getUsername(), bookInDb.getBorrowedBy());
    }

    @Test
    void shouldReturnBook() {
        String title = "Harry Potter";

        Book book = new Book(title, "Rowling", 2000);
        bookService.addBook(book);

        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        databaseManager.insertUser(user);

        User userInDb = returnUserFromDb(user.getUsername(), databaseManager.selectUsers());

        bookService.borrowBook(title, userInDb.getUsername());

        boolean result = bookService.returnBook(title);

        Book bookInDb = returnBookFromDb(title, bookService);

        assertTrue(result);
        assertTrue(bookInDb.isAvailable());
        assertNull(bookInDb.getBorrowedBy());
        assertNull(bookInDb.getDeadLine());
        assertNull(bookInDb.getBorrowDate());
        assertNotEquals(userInDb.getUsername(), bookInDb.getBorrowedBy());
    }

    private Book returnBookFromDb(String title, BookService bookService) {
        return bookService.getBooks().stream()
                .filter(b -> b.getTitle().equals(title))
                .findFirst()
                .orElseThrow();
    }

    private User returnUserFromDb(String username, ArrayList<User> users) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow();
    }
}