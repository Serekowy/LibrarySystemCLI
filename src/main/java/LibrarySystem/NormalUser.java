package LibrarySystem;

public class NormalUser extends User {

    public NormalUser(String username, String password, String email, Role role) {
        super(username, password, email, role);
    }

    @Override
    public void runMenu(Library library, Display display, Database database) {
        FileManager fm = new FileManager();
        boolean running = true;

        while (running) {
            display.showUserMenu();
            String choice = display.getChoice();

            switch (choice) {
                case "1" -> {
                    display.showBooks(library.getBooks());
                    display.waitForAction();
                }
                case "2" -> {
                    String bookTitle = display.getBookTitle();
                    String borrowedBy = username;
                    display.showBookBorrowResult(library.borrowBook(bookTitle, borrowedBy), bookTitle);
                    fm.saveBooks(library.getBooks());
                    display.waitForAction();
                }
                case "3" -> {
                    String bookTitle = display.getBookTitle();
                    display.showBookReturnResult(library.returnBook(bookTitle), bookTitle);
                    fm.saveBooks(library.getBooks());
                    display.waitForAction();
                }
                case "4" -> {
                    display.showBorrowedBooks(library.getBooksByUsername(getUsername()));
                    display.waitForAction();
                }
                case "5" -> {
                    display.showBooksAfterDeadLine(library.getBooksAfterDeadlineByUsername(getUsername()));
                    display.waitForAction();
                }
                case "0" -> running = false;
                default -> display.wrongChoice();
            }
        }
    }
}
