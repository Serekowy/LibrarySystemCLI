package LibrarySystem.model;

import LibrarySystem.service.BookService;
import LibrarySystem.service.UserService;
import LibrarySystem.ui.Display;
import LibrarySystem.util.PasswordUtil;

public abstract class User {

    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected Role role;

    User(String username, String password, String email, Role role) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract void runMenu(BookService bookService, Display display, UserService userService);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean checkPassword(String password) {
        return PasswordUtil.verifyPassword(password, this.password);
    }


}
