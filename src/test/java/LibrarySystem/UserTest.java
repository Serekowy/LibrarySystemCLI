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
        user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);

        assertTrue(user.checkPassword(user.getPassword()));
    }

    @Test
    void shouldNotAcceptPassword() {
        user = new NormalUser("Uzytkownik", "123", "u@u.com", Role.USER);

        assertFalse(user.checkPassword("1234"));
    }
}
