package LibrarySystem;

import LibrarySystem.model.NormalUser;
import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private User user;

    @Test
    void shouldCheckPassword() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);

        assertTrue(user.checkPassword(user.getPassword()));
    }

    @Test
    void shouldNotAcceptPassword() {
        User user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);

        assertFalse(user.checkPassword("1234"));
    }
}
