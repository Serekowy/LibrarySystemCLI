package LibrarySystem.database;

import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.model.Admin;
import LibrarySystem.model.NormalUser;

import java.util.ArrayList;

public class Database {
    public ArrayList<User> users = new ArrayList<>();
    DatabaseManager databaseManager = new DatabaseManager();

    public void addUser(User user) {
        users.add(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean passwordCompare(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    public boolean checkUsernameAvailability(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkEmailAvailability(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    public boolean changeUserRole(String username, Role role) {

        if (role == null) {
            return false;
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            if (user.getUsername().equalsIgnoreCase(username)) {
                switch (role) {
                    case USER -> {
                        if (user instanceof NormalUser) {
                            return false;
                        }
                        NormalUser newUser = new NormalUser(user.getUsername(), user.getPassword(), user.getEmail(), Role.USER);
                        users.set(i, newUser);
                        databaseManager.updateRole(username, Role.USER);
                        return true;
                    }
                    case ADMIN -> {
                        if (user instanceof Admin) {
                            return false;
                        }
                        Admin newAdmin = new Admin(user.getUsername(), user.getPassword(), user.getEmail(), Role.ADMIN);
                        users.set(i, newAdmin);
                        databaseManager.updateRole(username, Role.ADMIN);
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
