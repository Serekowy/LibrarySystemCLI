package LibrarySystem.model;

import LibrarySystem.service.BookService;
import LibrarySystem.service.UserService;
import LibrarySystem.ui.Display;

public class NormalUser extends User {

    public NormalUser(String username, String password, String email, Role role) {
        super(username, password, email, role);
    }

    @Override
    public void runMenu(BookService bookService, Display display, UserService userService) {
        boolean running = true;

        while (running) {
            display.showUserMenu();
            String choice = display.getChoice();

            switch (choice) {
                case "1" -> {
                    display.showBooks(bookService.getBooks());
                    display.waitForAction();
                }
                case "2" -> {
                    String bookTitle = display.getBookTitle();
                    String borrowedBy = username;
                    display.showBookBorrowResult(bookService.borrowBook(bookTitle, borrowedBy), bookTitle);
                    display.waitForAction();
                }
                case "3" -> {
                    String bookTitle = display.getBookTitle();
                    display.showBookReturnResult(bookService.returnBook(bookTitle), bookTitle);
                    display.waitForAction();
                }
                case "4" -> {
                    display.showBorrowedBooks(bookService.getBooksByUsername(getUsername()));
                    display.waitForAction();
                }
                case "5" -> {
                    display.showBooksAfterDeadLine(bookService.getBooksAfterDeadlineByUsername(getUsername()));
                    display.waitForAction();
                }
                case "0" -> running = false;
                default -> display.wrongChoice();
            }
        }
    }
}
