package LibrarySystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Display {

    Scanner scanner = new Scanner(System.in);

    public Display() {}

    public void showUserMenu() {
        System.out.println("Menu");
        System.out.println("1. Wyświetl książki");
        System.out.println("2. Wypożycz książkę");
        System.out.println("3. Oddaj książkę");
        System.out.println("4. Sprawdź wypożyczone książki");
        System.out.println("5. Sprawdź książki po terminie");
        System.out.println("0. Wyloguj się");
        System.out.print("Wybór:");

    }

    public void showAdminMenu() {
        System.out.println("Menu");
        System.out.println("1. Wyświetl książki");
        System.out.println("2. Sprawdź wszystkie wypożyczone książki");
        System.out.println("3. Sprawdź wypożyczone książki użytkownika");
        System.out.println("4. Sprawdź wszystkie książki po terminie");
        System.out.println("5. Sprawdź książki użytkownika po terminie");
        System.out.println("6. Dodaj książkę ");
        System.out.println("7. Usuń książkę ");
        System.out.println("8. Zmień termin oddania książki");
        System.out.println("9. Zmień rolę");
        System.out.println("0. Wyloguj się");
        System.out.print("Wybór:");

    }

    public void wrongChoice() {
        System.out.println("Błędny wybór.");
    }

    public Book addBook() {
        System.out.println("Jeżeli chcesz anulować nic nie wpisuj i wciśnij enter.");
        System.out.println("Dodaj książkę");
        System.out.print("Podaj tytuł książki: ");
        String title = scanner.nextLine();

        if(title.isEmpty()) {
            System.out.println("Anulowano dodawanie nowej książki.");
            return null;
        }

        System.out.print("Podaj autora książki: ");
        String author = scanner.nextLine();

        if(author.isEmpty()) {
            System.out.println("Anulowano dodawanie nowej książki.");
            return null;
        }

        int year = getValidBookYear();

        if(year == -1) {
            System.out.println("Anulowano dodawanie nowej książki.");
            return null;
        }

        return new Book(title, author, year);
    }

    public int getValidBookYear() {
        while (true) {
            try {
                System.out.print("Podaj rok wydania książki:");
                String input = scanner.nextLine();

                if (input.isEmpty()) return -1;

                int year = Integer.parseInt(input);

                if(year < 0 || LocalDate.now().getYear() < year) {
                    throw new NumberFormatException();
                }

                return year;

            } catch (NumberFormatException e) {
                System.out.println("Podano nieprawidłowy rok wydania.");
            }
        }
    }

    public void showAddBookResult(boolean result) {
        String text = (result ? "Książka została dodana pomyślnie!" : "Nie udało się dodać książki");
        System.out.println(text);
    }

    public void removeBook(boolean result, String bookTitle) {
        if(result) {
            System.out.println("Udało się usunąć książkę '" + bookTitle + "' z bazy.");
        } else {
            System.out.println("Książka nie istnieje lub podano zły tytuł.");
        }
    }

    public void showBooks(ArrayList <Book> books) {
        System.out.printf("%-30s %-20s %-15s %s%n" ,"Tytuł", "Autor", "Rok wydania", "Dostępna");

        books.forEach(
                book -> {
                    String borrowed = (book.isAvailable()) ? "Tak" : "Nie";
                    System.out.printf("%-30s %-20s %-15d %s%n", book.getTitle(), book.getAuthor(), book.getYear(), borrowed);
                }
        );
    }

    public void showBorrowedBooks(ArrayList <Book> borrowedBooks) {
        System.out.printf("%-30s %-20s %-15s %-15s %-15s%n" ,"Tytuł", "Autor", "Rok wydania", "Wypożyczona", "Termin zwrotu");

        borrowedBooks.forEach(
                book -> System.out.printf("%-30s %-20s %-15d %-15s %-15s%n", book.getTitle(), book.getAuthor(), book.getYear(), dateToString(book.getBorrowDate()),  dateToString(book.getDeadLine()))
        );

    }

    private String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String getBookTitle() {
        System.out.print("Podaj tytuł książki: ");

        return scanner.nextLine();
    }

    public void showBookBorrowResult(boolean borrowed, String bookTitle) {
        if (borrowed) {
            System.out.println("Książka '" + bookTitle + "' została wypożyczona!");
        } else {
            System.out.println("Taka książka została wypożyczona lub nie ma jej w naszej bazie.");
        }
    }

    public void showBookReturnResult(boolean returned, String bookTitle) {
        if (returned) {
            System.out.println("Książka '" + bookTitle + "' została pomyślnie zwrócona");
        } else {
            System.out.println("Podany tytuł jest błędny lub książka nie była wypożyczona");
        }
    }

    public void showBooksAfterDeadLine(ArrayList <Book> books) {
        showBorrowedBooks(books);
    }

    public LocalDate getLocalDate() {

        boolean isDateCorrect = false;
        LocalDate date = null;

        while (!isDateCorrect) {
            try {
                System.out.print("Podaj datę (w formacie DD-MM-YYYY): ");
                date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                isDateCorrect = true;
            } catch (Exception e) {
                System.out.println("Podaj poprawny format daty");
            }
        }
        return date;
    }

    public void showChangeDeadLineStatus(boolean status, String bookTitle) {
        if (status) {
            System.out.println("Pomyślnie zmieniono datę oddania książki '" + bookTitle + "'.");
        } else {
            System.out.println("Książka '" + bookTitle + "'nie jest wypożyczona lub nie istnieje.");
        }
    }

    public String getChoice() {
        return scanner.nextLine();
    }

    public void waitForAction() {
        System.out.println("Naciśnij Enter, aby wrócić do menu...");
        scanner.nextLine();
    }

    //logowanie i rejestracja

    public void showSystemMenu() {
        System.out.println("Menu systemu");
        System.out.println("1. Zaloguj się.");
        System.out.println("2. Zarejestruj się."); //TODO
        System.out.println("0. Zamknij program");
        System.out.print("Wybór: ");
    }

    public String getUsername() {
        System.out.print("Podaj nazwę użytkownika: ");
        return scanner.nextLine();
    }

    public String getPassword() {
        System.out.print("Podaj hasło: ");
        return scanner.nextLine();
    }

    public String getRepeatPassword() {
        System.out.print("Powtórz hasło: ");
        return scanner.nextLine();
    }

    public String getEmail() {
        System.out.print("Podaj email: ");
        return scanner.nextLine();
    }

    public boolean checkUserExist(User user) {
        if(user == null) {
            System.out.println("Nie znaleziono użytkownika w bazie danych");
            return false;
        }
        return true;
    }

    public boolean loginStatus(boolean status) {
        if (status) {
            System.out.println("Zalogowano pomyślnie!");
            return true;
        } else {
            System.out.println("Nieprawidłowe dane, spróbuj ponownie.");
            return false;
        }
    }

    public void checkUsernameAvailability(boolean status, String username) {
        if (!status) {
            System.out.println("Nazwa użytkownika '"+ username +"' jest już zajęta.");
        }
    }

    public void  checkEmailAvailability(boolean status, String email) {
        if (!status) {
            System.out.println("Email '"+ email +"' istnieje już w naszym systemie.");
        }
    }

    public String checkPasswordLength(String password) {
        while (password.length() <= 5) {
            System.out.println("Hasło jest za krótkie, musi zawierać conajmniej 6 znaków.");
            System.out.print("Wpisz hasło: ");
            password = scanner.nextLine();
        }
        return password;
    }

    public void passwordMatch(boolean status) {
        if (!status) {
            System.out.println("Podane hasła nie są identyczne.");
        }
    }

    public void showRegisterError() {
        System.out.println("Nie udało się zarejestrować, podczas wypełniania formularza wykryto:");
    }

    public void showRegisterSucces() {
            System.out.println("Rejestracja przebiegła pomyślnie.");
    }

    public void showNoDataRegisterMessage() {
        System.out.println("Nie podano wymaganych danych, powrót do menu.");
    }

    public void showLoginMessage() {
        System.out.println("Jeżeli jesteś tu przypadkiem nic nie wpisuj i wciśnij enter. \nZaloguj się");
    }

    public void showRegisterMessage() {
        System.out.println("Jeżeli jesteś tu przypadkiem wciśnij enter w polu podania \nnazwy użytkownika, e-maila lub hasła. \nZarejestruj się ");
    }

    public void showUserBooks(ArrayList <Book> borrowedBooks) {
        System.out.printf("%-30s %-20s %-15s %-15s %-15s %-20s%n" ,"Tytuł", "Autor", "Rok wydania", "Wypożyczona", "Termin zwrotu", "Wypożycza");

        borrowedBooks.forEach(
                book -> System.out.printf("%-30s %-20s %-15d %-15s %-15s %-20s%n", book.getTitle(), book.getAuthor(), book.getYear(), dateToString(book.getBorrowDate()), dateToString(book.getDeadLine()), book.getBorrowedBy())
        );
    }

    public Role getUserRole() {
        Role newRole = null;

        while (newRole == null) {
            try {
                System.out.print("Podaj nową rolę (USER/ADMIN): ");
                newRole = Role.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Podano złą rolę, spróbuj ponownie.");
            }
        }

        return newRole;
    }

    public void showChangeRoleResult(String username,Role newRole, boolean status) {
        if (status) {
            System.out.println("Pomyślnie zmieniono rolę użykownika " + username + " na " + newRole.toString().toLowerCase() + "a.");
        } else {
            System.out.println("Nie udało się zmienić roli użytkownika, spróbuj ponownie.");
        }
    }

}
