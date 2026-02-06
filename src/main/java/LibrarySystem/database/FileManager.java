package LibrarySystem.database;

import LibrarySystem.model.Role;
import LibrarySystem.model.User;
import LibrarySystem.model.Admin;
import LibrarySystem.model.Book;
import LibrarySystem.model.NormalUser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class FileManager {

    private static final String BOOK_FILE = "books.txt";
    private static final String USERS_FILE = "users.txt";

    public void saveBooks(ArrayList<Book> books) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book book : books) {
                StringJoiner sj = new StringJoiner(";");
                sj.add(book.getTitle());
                sj.add(book.getAuthor());
                sj.add(String.valueOf(book.getYear()));
                sj.add(book.getBorrowedBy());
                sj.add(String.valueOf(book.isAvailable()));
                sj.add(String.valueOf(book.getBorrowDate()));
                sj.add(String.valueOf(book.getDeadLine()));

                String line = sj.toString();

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Book> loadBooks() {
        ArrayList<Book> books = new ArrayList<>();
        File file = new File(BOOK_FILE);

        if(!file.exists()) {
            return books;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;
            while((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");

                String title = st.nextToken();
                String author = st.nextToken();
                int year = Integer.parseInt(st.nextToken());
                String borrowedBy = st.nextToken();
                boolean isAvailable = Boolean.parseBoolean(st.nextToken());

                String borrowDateString = st.nextToken();
                LocalDate borrowDate;

                if(borrowDateString.equals("null")) {borrowDate = null;}
                else borrowDate = LocalDate.parse(borrowDateString);

                String deadLineString = st.nextToken();
                LocalDate deadLine;

                if(deadLineString.equals("null")) {deadLine = null;}
                else deadLine = LocalDate.parse(deadLineString);

                Book book = new Book(title, author, year);
                book.setBorrowedBy(borrowedBy);
                book.setAvailable(isAvailable);
                book.setBorrowDate(borrowDate);
                book.setDeadline(deadLine);

                books.add(book);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    public void saveUsers(ArrayList<User> users) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                StringJoiner sj = new StringJoiner(";");
                sj.add(user.getUsername());
                sj.add(user.getPassword());
                sj.add(user.getEmail());
                sj.add(user.getRole().toString());

                String line = sj.toString();

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if(!file.exists()) {
            return users;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line  = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");
                String username = st.nextToken();
                String password = st.nextToken();
                String email = st.nextToken();
                String role = st.nextToken();

                if(role.equals("ADMIN")) {
                    Admin admin = new Admin(username, password, email, Role.ADMIN);
                    users.add(admin);
                } else if (role.equals("USER")) {
                    NormalUser normalUser = new NormalUser(username, password, email, Role.USER);
                    users.add(normalUser);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }
}
