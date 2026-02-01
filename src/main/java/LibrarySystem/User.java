package LibrarySystem;

public abstract class User {

    protected String username;
    protected String password;
    protected String email;
    protected Role role;

    User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract void runMenu(Library library, Display display, Database database);

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
        return this.password.equals(password);
    }


}
