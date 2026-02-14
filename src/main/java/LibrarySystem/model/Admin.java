package LibrarySystem.model;

import LibrarySystem.service.BookService;
import LibrarySystem.service.UserService;
import LibrarySystem.ui.Display;

import java.time.LocalDate;

public class Admin extends User {

    public Admin(String username, String password, String email, Role role) {
        super(username, password, email, role);
    }

    @Override
    public void runMenu(BookService bookService, Display display, UserService userService) {
        boolean running = true;

        while (running) {

            display.showAdminMenu();
            String choice = display.getChoice();

            switch (choice) {
                case "1" -> {
                    display.showBooksWithID(bookService.getBooks());
                    display.waitForAction();
                }
                case "2" -> {
                    display.showBooksWithID(bookService.getBorrowedBooks());
                    display.waitForAction();
                }
                case "3" -> {
                    String username = display.getUsername();
                    display.showUserBooks(bookService.getBooksByUsername(username));
                    display.waitForAction();
                }
                case "4" -> {
                    display.showBooksWithID(bookService.getBooksAfterDeadline());
                    display.waitForAction();
                }
                case "5" -> {
                    String username = display.getUsername();
                    display.showUserBooks(bookService.getBooksAfterDeadlineByUsername(username));
                    display.waitForAction();
                }
                case "6" -> {
                    Book newBook = display.addBook();
                    if (newBook == null) break;
                    else {
                        display.showAddBookResult(bookService.addBook(newBook));
                        display.waitForAction();
                    }
                }
                case "7" -> {
                    int bookId = display.getBookId();
                    display.removeBook(bookService.removeBook(bookId), bookId);
                    display.waitForAction();
                }
                case "8" -> {
                    int bookId = display.getBookId();
                    LocalDate inputDate = display.getLocalDate();
                    display.showChangeDeadLineStatus(bookService.changeDeadLine(bookId, inputDate), bookId);
                    display.waitForAction();
                }
                case "9" -> {
                    int userId = display.getUserId();
                    Role newRole = display.getUserRole();
                    display.showChangeRoleResult(userId, newRole, userService.changeUserRole(userId, newRole));
                    display.waitForAction();
                }
                case "10" -> {
                    display.showUsers(userService.getUsers());
                    display.waitForAction();
                }
                case "0" -> running = false;
                default -> display.wrongChoice();
            }
        }
    }
}
