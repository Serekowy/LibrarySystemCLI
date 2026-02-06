package LibrarySystem;

import LibrarySystem.database.Database;
import LibrarySystem.database.FileManager;
import LibrarySystem.model.Admin;
import LibrarySystem.model.NormalUser;
import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.service.Library;
import LibrarySystem.ui.Display;

public class Main {
    public static void main(String[] args) {

        Library library = new Library();
        Display display = new Display();
        Database database = new Database();
        FileManager fm = new FileManager();

        library.setBooks(fm.loadBooks());
        database.setUsers(fm.loadUsers());

        boolean systemRunning = true;

        if(database.getUsers().isEmpty()){
            Admin admin = new Admin("a", "a", "root@admin.com", Role.ADMIN);
            database.addUser(admin);
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
                        if (username.isBlank()) {break;}
                        String password = display.getPassword();
                        if (password.isBlank()) {break;}

                        User loggingUser = database.getUserByUsername(username);

                        if (!display.checkUserExist(loggingUser)) {
                            display.waitForAction();
                            break;
                        }

                        if(display.loginStatus(loggingUser.checkPassword(password))) {
                            currentUser = loggingUser;
                        } else {
                            display.waitForAction();
                        }
                    }
                    if(currentUser != null) {
                        currentUser.runMenu(library, display, database);
                    } else {
                        display.showNoDataRegisterMessage();
                    }
                    fm.saveBooks(library.getBooks());
                }
                case "2" -> {
                    display.showRegisterMessage();

                    String username = display.getUsername();
                    boolean isUsernameAvailable = database.checkUsernameAvailability(username);

                    if(username.isBlank()) {display.showNoDataRegisterMessage(); break; }

                    String email = display.getEmail();
                    boolean isEmailAvailable = database.checkEmailAvailability(email);

                    if(email.isBlank()) {display.showNoDataRegisterMessage(); break; }

                    String password = display.getPassword();

                    if(password.isBlank()) {display.showNoDataRegisterMessage(); break; }

                    password = display.checkPasswordLength(password);
                    String repeatPassword = display.getRepeatPassword();

                    if(isUsernameAvailable && isEmailAvailable && password.equals(repeatPassword)) {
                        database.addUser(new NormalUser(username, password, email, Role.USER));
                        display.showRegisterSucces();
                        fm.saveUsers(database.getUsers());
                    } else {
                        display.showRegisterError();
                        display.checkUsernameAvailability(isUsernameAvailable, username);
                        display.checkEmailAvailability(isEmailAvailable, email);
                        display.passwordMatch(database.passwordCompare(password, repeatPassword));
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