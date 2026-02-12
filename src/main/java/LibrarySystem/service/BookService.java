package LibrarySystem.service;

import LibrarySystem.database.DatabaseManager;
import LibrarySystem.model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BookService {

    private static final int BOOK_BORROW_TIME = 7;
    private final DatabaseManager databaseManager;

    public BookService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean addBook(Book book) {
        try {
            databaseManager.insertBook(book);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeBook(int bookId) {

        for (Book book : getBooks()) {
            if (book.getId() == bookId) {
                databaseManager.deleteBook(bookId);
                return true;
            }
        }

        return false;
    }

    //TODO jeżeli są dwie takie same książki, to wtedy wyświetlić pole autora żeby wypożyczyć odpowiednią dodatkowo może książki po id wtedy poprawnie by było
    public boolean borrowBook(String bookTitle, String username) {
        for (Book book : getBooks()) {
            if (book.getTitle().equalsIgnoreCase(bookTitle) && book.isAvailable()) {
                book.setAvailable(false);
                book.setBorrowDate(LocalDate.now());
                book.setDeadline(book.getBorrowDate().plusDays(BOOK_BORROW_TIME));
                book.setBorrowedBy(username);
                databaseManager.updateBook(book);
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(String bookTitle) {
        for (Book book : getBooks()) {
            if (book.getTitle().equalsIgnoreCase(bookTitle) && !book.isAvailable()) {
                book.setAvailable(true);
                book.setBorrowDate(null);
                book.setDeadline(null);
                book.setBorrowedBy(null);
                databaseManager.updateBook(book);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Book> getBooks() {
        return databaseManager.selectBooks();
    }

    public ArrayList<Book> getBorrowedBooks() {
        return getBooks().stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getDeadLine().isAfter(LocalDate.now()) || b.getDeadLine().equals(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Book> getBooksAfterDeadline() {
        return getBooks().stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getDeadLine().isBefore(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean changeDeadLine(int bookId, LocalDate deadLine) {
        for (Book book : getBooks()) {
            if (book.getId() == bookId && !book.isAvailable()) {
                book.setDeadline(deadLine);
                databaseManager.updateDeadLine(bookId, deadLine.toString());
                return true;
            }
        }
        return false;
    }

    public ArrayList<Book> getBooksByUsername(String username) {
        return getBooks().stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getBorrowedBy() != null && b.getBorrowedBy().equalsIgnoreCase(username))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Book> getBooksAfterDeadlineByUsername(String username) {
        return getBooks().stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getBorrowedBy() != null && b.getBorrowedBy().equalsIgnoreCase(username))
                .filter(b -> b.getDeadLine().isBefore(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
