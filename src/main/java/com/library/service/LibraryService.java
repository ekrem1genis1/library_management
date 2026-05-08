package com.library.service;

import com.library.command.CommandHistory;
import com.library.command.ModifyBookCommand;
import com.library.model.Book;
import com.library.observer.AvailabilityTracker;
import com.library.observer.BookObserver;
import com.library.strategy.AscendingTitleSort;
import com.library.strategy.DescendingTitleSort;
import com.library.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryService {
    private final List<Book> books = new ArrayList<>();
    private final List<BookObserver> observers = new ArrayList<>();
    private final CommandHistory commandHistory = new CommandHistory();
    private SortStrategy sortStrategy = new AscendingTitleSort();

    public LibraryService() {
        observers.add(new AvailabilityTracker());
        books.addAll(JsonPersistence.load());
        if (!books.isEmpty()) {
            System.out.println("[Info] Loaded " + books.size() + " book(s) from storage.");
        }
    }

    public void addBook(Book book) {
        books.add(book);
        JsonPersistence.save(books);
        System.out.println("Book saved: \"" + book.getTitle() + "\"");
    }

    public void modifyBook(Book book, Book newState) {
        ModifyBookCommand cmd = new ModifyBookCommand(book, newState);
        commandHistory.execute(cmd);
        JsonPersistence.save(books);
        System.out.println("Book modified: \"" + book.getTitle() + "\"");
    }

    public void undoLastModification() {
        commandHistory.undo();
        JsonPersistence.save(books);
    }

    public List<Book> search(String keyword) {
        String kw = keyword.toLowerCase();
        return books.stream()
            .filter(b ->
                b.getTitle().toLowerCase().contains(kw) ||
                b.getAuthor().toLowerCase().contains(kw) ||
                b.getIsbn().toLowerCase().contains(kw) ||
                b.getCategories().stream().anyMatch(c -> c.toLowerCase().contains(kw)) ||
                b.getTags().stream().anyMatch(t -> t.toLowerCase().contains(kw))
            )
            .collect(Collectors.toList());
    }

    public void setSortAscending() { this.sortStrategy = new AscendingTitleSort(); }
    public void setSortDescending() { this.sortStrategy = new DescendingTitleSort(); }
    public void applySorting(List<Book> results) { sortStrategy.sort(results); }

    public boolean borrowBook(Book book) {
        if (!book.isAvailable()) {
            System.out.println("Book is not available for borrowing.");
            return false;
        }
        for (BookObserver obs : observers) obs.onBookBorrowed(book);
        JsonPersistence.save(books);
        return true;
    }

    public boolean returnBook(Book book) {
        if (book.isAvailable()) {
            System.out.println("Book is already available (not borrowed).");
            return false;
        }
        for (BookObserver obs : observers) obs.onBookReturned(book);
        JsonPersistence.save(books);
        return true;
    }

    public List<Book> getAllBooks() { return books; }
    public boolean hasBooks() { return !books.isEmpty(); }
}
