package LibrarySystem;

import LibrarySystem.model.Book;
import LibrarySystem.model.NormalUser;
import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.service.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    void shouldAddBook() {
        Book book = new Book("Harry Potter", "Rowling", 2000);

        boolean result = library.addBook(book);

        assertTrue(result);
        assertEquals(1, library.getBooks().size());

    }

    @Test
    void shouldRemoveBook() {
        Book book = new Book("Harry Potter", "Rowling", 2000);
        library.addBook(book);

        assertTrue(library.removeBook("Harry Potter"));
        assertEquals(0, library.getBooks().size());
    }

    @Test
    void shouldBorrowBook() {
        Book book = new Book("Harry Potter", "Rowling", 2000);
        library.addBook(book);
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);

        boolean result = library.borrowBook("Harry Potter", "Uzytkownik");

        assertTrue(result);
        assertFalse(book.isAvailable());
        assertNotEquals(null, book.getBorrowDate());
        assertNotEquals(null , book.getDeadLine());
        assertEquals(user.getUsername(), book.getBorrowedBy());
    }

    @Test
    void shouldNotBorrowBorrowedBook() {
        Book book = new Book("Harry Potter", "Rowling", 2000);
        library.addBook(book);
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        User user1 = new NormalUser("InnyUzytkownik", "123", "inny@gmail.com", Role.USER);

        library.borrowBook(book.getTitle(), user.getUsername());
        boolean result =  library.borrowBook(book.getTitle(), user1.getUsername());

        assertFalse(result);
        assertEquals(user.getUsername(), book.getBorrowedBy());
    }

    @Test
    void shouldReturnBook() {
        Book book = new Book("Harry Potter", "Rowling", 2000);
        library.addBook(book);
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        library.borrowBook(book.getTitle(), user.getUsername());

        boolean result = library.returnBook(book.getTitle());

        assertTrue(result);
        assertTrue(book.isAvailable());
        assertNull(book.getBorrowedBy());
        assertNull(book.getDeadLine());
        assertNull(book.getBorrowDate());
        assertNotEquals(user.getUsername(), book.getBorrowedBy());
    }



}