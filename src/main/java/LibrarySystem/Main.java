package LibrarySystem;

import LibrarySystem.service.BookService;
import LibrarySystem.service.UserService;
import LibrarySystem.database.DatabaseManager;
import LibrarySystem.model.Admin;
import LibrarySystem.model.NormalUser;
import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.ui.Display;

public class Main {
    public static void main(String[] args) {

        DatabaseManager databaseManager = new DatabaseManager();
        BookService bookService = new BookService(databaseManager);
        Display display = new Display();
        UserService userService = new UserService(databaseManager);

        databaseManager.connectAndCreateTables();

        boolean systemRunning = true;

        if (databaseManager.getUserByUsername("a") == null) {
            Admin admin = new Admin("a", "a", "root@admin.com", Role.ADMIN);
            databaseManager.insertUser(admin);
        }

        while (systemRunning) {
            display.showSystemMenu();
            String choice = display.getChoice();

            switch (choice) {
                case "1" -> {
                    display.showLoginMessage();

                    User currentUser = null;

                    while (currentUser == null) {
                        String username = display.getUsername();
                        if (username.isBlank()) {
                            break;
                        }
                        String password = display.getPassword();
                        if (password.isBlank()) {
                            break;
                        }

                        User loggingUser = userService.getUserByUsername(username);

                        if (!display.checkUserExist(loggingUser)) {
                            display.waitForAction();
                            break;
                        }

                        if (display.loginStatus(loggingUser.checkPassword(password))) {
                            currentUser = loggingUser;
                        } else {
                            display.waitForAction();
                        }
                    }
                    if (currentUser != null) {
                        currentUser.runMenu(bookService, display, userService);
                    } else {
                        display.showNoDataRegisterMessage();
                    }
                }
                case "2" -> {
                    display.showRegisterMessage();

                    String username = display.getUsername();
                    boolean isUsernameAvailable = userService.checkUsernameAvailability(username);

                    if (username.isBlank()) {
                        display.showNoDataRegisterMessage();
                        break;
                    }

                    String email = display.getEmail();
                    boolean isEmailAvailable = userService.checkEmailAvailability(email);

                    if (email.isBlank()) {
                        display.showNoDataRegisterMessage();
                        break;
                    }

                    String password = display.getPassword();

                    if (password.isBlank()) {
                        display.showNoDataRegisterMessage();
                        break;
                    }

                    password = display.checkPasswordLength(password);
                    String repeatPassword = display.getRepeatPassword();

                    if (isUsernameAvailable && isEmailAvailable && password.equals(repeatPassword)) {
                        User newUser = new NormalUser(username, password, email, Role.USER);
                        userService.addUser(newUser);
                        display.showRegisterSucces();
                    } else {
                        display.showRegisterError();
                        display.checkUsernameAvailability(isUsernameAvailable, username);
                        display.checkEmailAvailability(isEmailAvailable, email);
                        display.passwordMatch(userService.passwordCompare(password, repeatPassword));
                    }

                    display.waitForAction();
                }
                case "0" -> systemRunning = false;
                default -> {
                    display.wrongChoice();
                    display.waitForAction();
                }
            }
        }
    }
}