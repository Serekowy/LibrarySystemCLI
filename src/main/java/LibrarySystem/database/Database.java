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
        return databaseManager.getUserByUsername(username) == null;
    }

    public boolean checkEmailAvailability(String email) {
        return !databaseManager.emailExists(email);
    }

    public boolean changeUserRole(int id, Role role) {
        if (role == null) return false;

        User user = databaseManager.getUserById(id);
        if (user == null) return false;

        if (role == Role.USER && user instanceof NormalUser) return false;
        if (role == Role.ADMIN && user instanceof Admin) return false;

        databaseManager.updateRole(id, role);
        return true;
    }
}
