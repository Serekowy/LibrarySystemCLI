package LibrarySystem.model;

import java.time.LocalDate;

public class Book {
    private final String title;
    private final String author;
    private final Integer year;
    private int id;
    private boolean available;
    private String borrowedBy;
    private LocalDate borrowDate;
    private LocalDate returnDeadLine;

    public Book(String title, String author, Integer year) {
        this.id = 0;
        this.title = title;
        this.author = author;
        this.year = year;
        this.borrowedBy = null;
        this.available = true;
        this.borrowDate = null;
        this.returnDeadLine = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getYear() {
        return year;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDeadline(LocalDate returnDeadLine) {
        this.returnDeadLine = returnDeadLine;
    }

    public LocalDate getDeadLine() {
        return returnDeadLine;
    }
}
