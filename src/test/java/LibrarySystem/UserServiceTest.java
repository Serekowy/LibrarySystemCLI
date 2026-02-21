package LibrarySystem;

import LibrarySystem.database.DatabaseManager;
import LibrarySystem.model.NormalUser;
import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private DatabaseManager databaseManager;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        databaseManager = new DatabaseManager();
        databaseManager.connectAndCreateTables();
        databaseManager.clearDatabase();

        userService = new UserService(databaseManager);
    }

    @Test
    public void shouldAddUser() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        assertNotNull(databaseManager.getUserByUsername(user.getUsername()));
        assertEquals(user.getUsername(), getUserFromDb(user.getUsername()).getUsername());
        assertEquals(user.getEmail(), getUserFromDb(user.getUsername()).getEmail());
        assertEquals(1, getUserFromDb(user.getUsername()).getId());

    }

    @Test
    public void shouldLoginBeAvailable() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        assertTrue(userService.checkUsernameAvailability("uzo"));
    }

    @Test
    public void shouldLoginNotBeAvailable() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        assertFalse(userService.checkUsernameAvailability(user.getUsername()));
    }

    @Test
    public void shouldEmailBeAvailable() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        assertTrue(userService.checkEmailAvailability("a@a.com"));
    }

    @Test
    public void shouldEmailNotBeAvailable() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        assertFalse(userService.checkEmailAvailability("u@u.com"));
    }

    @Test
    public void shouldChangeUserRole() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        boolean result = userService.changeUserRole(getUserFromDb(user.getUsername()).getId(), Role.ADMIN);
        assertTrue(result);

        User userFromDb = getUserFromDb(user.getUsername());
        assertEquals(Role.ADMIN, userFromDb.getRole());
    }

    @Test
    public void shouldNotChangeUserRole() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);
        userService.addUser(user);

        boolean result = userService.changeUserRole(getUserFromDb(user.getUsername()).getId(), Role.USER);
        assertFalse(result);

        User userFromDb = getUserFromDb(user.getUsername());
        assertNotEquals(Role.ADMIN, userFromDb.getRole());

        result = userService.changeUserRole(514213, Role.USER);
        assertFalse(result);

    }

    private User getUserFromDb(String username) {
        return databaseManager.getUserByUsername(username);
    }
}
