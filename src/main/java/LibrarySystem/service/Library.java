package LibrarySystem.service;

import LibrarySystem.database.DatabaseManager;
import LibrarySystem.model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Library {

    private static final int BOOK_BORROW_TIME = 7;
    DatabaseManager databaseManager = new DatabaseManager();
    private ArrayList<Book> books = new ArrayList<>();

    public boolean addBook(Book book) {
        try {
            books.add(book);
            databaseManager.insertBook(book);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeBook(String bookTitle) {

        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                books.remove(book);
                databaseManager.deleteBook(bookTitle);
                return true;
            }
        }

        return false;
    }

    //TODO jeżeli są dwie takie same książki, to wtedy wyświetlić pole autora żeby wypożyczyć odpowiednią dodatkowo może książki po id wtedy poprawnie by było
    public boolean borrowBook(String bookTitle, String username) {
        for (Book book : books) {
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
        for (Book book : books) {
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
        return books;
    }

    public void setBooks(ArrayList<Book> newBooks) {
        books = newBooks;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return books.stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getDeadLine().isAfter(LocalDate.now()) || b.getDeadLine().equals(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Book> getBooksAfterDeadline() {
        return books.stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getDeadLine().isBefore(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean changeDeadLine(String bookTitle, LocalDate deadLine) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookTitle) && !book.isAvailable()) {
                book.setDeadline(deadLine);
                databaseManager.updateDeadLine(bookTitle, deadLine.toString());
                return true;
            }
        }
        return false;
    }

    public ArrayList<Book> getBooksByUsername(String username) {
        return books.stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getBorrowedBy() != null && b.getBorrowedBy().equalsIgnoreCase(username))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Book> getBooksAfterDeadlineByUsername(String username) {
        return books.stream()
                .filter(b -> !b.isAvailable())
                .filter(b -> b.getBorrowedBy() != null && b.getBorrowedBy().equalsIgnoreCase(username))
                .filter(b -> b.getDeadLine().isBefore(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
