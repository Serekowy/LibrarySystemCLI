package LibrarySystem.database;

import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.model.Admin;
import LibrarySystem.model.NormalUser;

import java.util.ArrayList;

public class Database {
    DatabaseManager databaseManager = new DatabaseManager();

    public void addUser(User user) {
        databaseManager.insertUser(user);
    }

    public ArrayList<User> getUsers() {
        return databaseManager.selectUsers();
    }

    public User getUserByUsername(String username) {
        return databaseManager.getUserByUsername(username);
    }

    public boolean passwordCompare(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    public boolean checkUsernameAvailability(String username) {
        for (User user : databaseManager.selectUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkEmailAvailability(String email) {
        for (User user : databaseManager.selectUsers()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    public boolean changeUserRole(int id, Role role) {

        if (role == null) {
            return false;
        }

        ArrayList<User> users = databaseManager.selectUsers();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            if (user.getId() == id) {
                switch (role) {
                    case USER -> {
                        if (user instanceof NormalUser) {
                            return false;
                        }
                        databaseManager.updateRole(id, Role.USER);
                        return true;
                    }
                    case ADMIN -> {
                        if (user instanceof Admin) {
                            return false;
                        }
                        databaseManager.updateRole(id, Role.ADMIN);
                        return true;
                    }
                    default -> {
                        return false;
                    }
                }
            }
        }

        return false;
    }
}
