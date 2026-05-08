package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
    private String publisher;
    private String description;
    private List<String> categories;
    private List<String> tags;
    private boolean available;
    private int borrowCount;

    public Book(String title, String author, int publicationYear, String isbn, String publisher) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.publisher = publisher;
        this.description = "";
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.available = true;
        this.borrowCount = 0;
    }

    // Copy constructor (for Command pattern snapshots)
    public Book(Book other) {
        this.title = other.title;
        this.author = other.author;
        this.publicationYear = other.publicationYear;
        this.isbn = other.isbn;
        this.publisher = other.publisher;
        this.description = other.description;
        this.categories = new ArrayList<>(other.categories);
        this.tags = new ArrayList<>(other.tags);
        this.available = other.available;
        this.borrowCount = other.borrowCount;
    }

    // Getters & Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getBorrowCount() { return borrowCount; }
    public void incrementBorrowCount() { this.borrowCount++; }

    @Override
    public String toString() {
        return String.format("[%s] \"%s\" by %s (%d) | ISBN: %s | %s",
                available ? "AVAILABLE" : "BORROWED",
                title, author, publicationYear, isbn,
                available ? "" : "Borrowed " + borrowCount + " time(s)");
    }

    public String toDetailString() {
        return String.format(
            "Title       : %s\n" +
            "Author      : %s\n" +
            "Year        : %d\n" +
            "ISBN        : %s\n" +
            "Publisher   : %s\n" +
            "Description : %s\n" +
            "Categories  : %s\n" +
            "Tags        : %s\n" +
            "Status      : %s\n" +
            "Borrow Count: %d",
            title, author, publicationYear, isbn, publisher,
            description.isEmpty() ? "-" : description,
            categories.isEmpty() ? "-" : String.join(", ", categories),
            tags.isEmpty() ? "-" : String.join(", ", tags),
            available ? "Available" : "Borrowed",
            borrowCount
        );
    }
}
