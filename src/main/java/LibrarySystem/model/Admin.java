package LibrarySystem.model;

import LibrarySystem.database.Database;
import LibrarySystem.database.FileManager;
import LibrarySystem.service.Library;
import LibrarySystem.ui.Display;

import java.time.LocalDate;

public class Admin extends User {

    public Admin(String username, String password, String email, Role role) {
        super(username, password, email, role);
    }

    @Override
    public void runMenu(Library library, Display display, Database database) {

        FileManager fm = new FileManager();

        boolean running = true;

        while (running) {

            display.showAdminMenu();
            String choice = display.getChoice();

            switch (choice) {
                case "1" -> {
                    display.showBooks(library.getBooks());
                    display.waitForAction();
                }
                case "2" -> {
                    display.showBooks(library.getBorrowedBooks());
                    display.waitForAction();
                }
                case "3" -> {
                    String username = display.getUsername();
                    display.showUserBooks(library.getBooksByUsername(username));
                    display.waitForAction();
                }
                case "4" -> {
                    display.showBooks(library.getBooksAfterDeadline());
                    display.waitForAction();
                }
                case "5" -> {
                    String username = display.getUsername();
                    display.showUserBooks(library.getBooksAfterDeadlineByUsername(username));
                    display.waitForAction();
                }
                case "6" -> {
                    Book newBook = display.addBook();
                    if(newBook == null) break;
                    else {
                        display.showAddBookResult(library.addBook(newBook));
                        fm.saveBooks(library.getBooks());
                        display.waitForAction();
                    }
                }
                case "7" -> {
                    String title = display.getBookTitle();
                    display.removeBook(library.removeBook(title), title);
                    fm.saveBooks(library.getBooks());
                    display.waitForAction();
                }
                case "8" -> {
                    String bookTitle = display.getBookTitle();
                    LocalDate inputDate = display.getLocalDate();
                    display.showChangeDeadLineStatus(library.changeDeadLine(bookTitle, inputDate), bookTitle);
                    fm.saveBooks(library.getBooks());
                    display.waitForAction();
                }
                case "9" -> {
                    String username = display.getUsername();
                    Role newRole = display.getUserRole();
                    display.showChangeRoleResult(username, newRole, database.changeUserRole(username, newRole));
                    fm.saveUsers(database.getUsers());
                    display.waitForAction();
                }
                case "0" -> running = false;
                default -> display.wrongChoice();
            }
        }
    }
}
