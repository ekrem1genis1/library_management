package com.library.ui;

import com.library.model.Book;
import com.library.service.LibraryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final LibraryService service;
    private final Scanner scanner;

    public ConsoleUI(LibraryService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Library Management System ===");
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1 -> createBook();
                case 2 -> searchBook();
                case 3 -> borrowBook();
                case 4 -> modifyBook();
                case 0 -> { System.out.println("Goodbye!"); running = false; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ── MAIN MENU ─────────────────────────────────────────────────────────────
    private void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Create Book");
        System.out.println("2. Search Book");
        System.out.println("3. Borrow Book");
        System.out.println("4. Modify Book");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    // ── CREATE BOOK ───────────────────────────────────────────────────────────
    private void createBook() {
        System.out.println("\n-- Create Book --");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Publication Year: ");
        int year = readInt();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Publisher: ");
        String publisher = scanner.nextLine().trim();

        Book book = new Book(title, author, year, isbn, publisher);

        System.out.println("Enter up to 3 categories (press Enter to skip):");
        book.setCategories(readUpTo(3, "Category"));

        System.out.println("Enter up to 3 tags (press Enter to skip):");
        book.setTags(readUpTo(3, "Tag"));

        System.out.print("Save book? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            service.addBook(book);
        } else {
            System.out.println("Book not saved.");
        }
    }

    // ── SEARCH BOOK ───────────────────────────────────────────────────────────
    private void searchBook() {
        System.out.println("\n-- Search Book --");
        System.out.print("Search keyword: ");
        String keyword = scanner.nextLine().trim();

        List<Book> results = service.search(keyword);
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        System.out.println("Sort order: 1. Ascending  2. Descending");
        System.out.print("Choice: ");
        int sortChoice = readInt();
        if (sortChoice == 2) service.setSortDescending();
        else service.setSortAscending();
        service.applySorting(results);

        System.out.println("\n-- Results (" + results.size() + ") --");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }

        System.out.print("Select book to view details (0 to go back): ");
        int sel = readInt();
        if (sel >= 1 && sel <= results.size()) {
            System.out.println("\n" + results.get(sel - 1).toDetailString());
        }
    }

    // ── BORROW BOOK ───────────────────────────────────────────────────────────
    private void borrowBook() {
        System.out.println("\n-- Borrow Book --");
        List<Book> all = service.getAllBooks();
        if (all.isEmpty()) { System.out.println("No books in library."); return; }

        printBookList(all);
        System.out.print("Select book (0 to go back): ");
        int sel = readInt();
        if (sel < 1 || sel > all.size()) return;

        Book book = all.get(sel - 1);
        System.out.println("1. Borrow  2. Return  0. Back");
        System.out.print("Choice: ");
        int action = readInt();
        if (action == 1) service.borrowBook(book);
        else if (action == 2) service.returnBook(book);
    }

    // ── MODIFY BOOK ───────────────────────────────────────────────────────────
    private void modifyBook() {
        System.out.println("\n-- Modify Book --");
        List<Book> all = service.getAllBooks();
        if (all.isEmpty()) { System.out.println("No books in library."); return; }

        printBookList(all);
        System.out.print("Select book to modify (0 to undo last modification): ");
        int sel = readInt();

        if (sel == 0) {
            service.undoLastModification();
            return;
        }
        if (sel < 1 || sel > all.size()) return;

        Book book = all.get(sel - 1);
        Book newState = new Book(book); // start from current state

        System.out.println("Current: " + book.toDetailString());
        System.out.println("\nLeave blank to keep current value.");

        System.out.print("Author [" + book.getAuthor() + "]: ");
        String input = scanner.nextLine().trim();
        if (!input.isEmpty()) newState.setAuthor(input);

        System.out.print("Publication Year [" + book.getPublicationYear() + "]: ");
        input = scanner.nextLine().trim();
        if (!input.isEmpty()) {
            try { newState.setPublicationYear(Integer.parseInt(input)); }
            catch (NumberFormatException e) { System.out.println("Invalid year, skipped."); }
        }

        System.out.print("ISBN [" + book.getIsbn() + "]: ");
        input = scanner.nextLine().trim();
        if (!input.isEmpty()) newState.setIsbn(input);

        System.out.print("Description [" + book.getDescription() + "]: ");
        input = scanner.nextLine().trim();
        if (!input.isEmpty()) newState.setDescription(input);

        System.out.print("Modify categories? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Enter up to 3 categories:");
            newState.setCategories(readUpTo(3, "Category"));
        }

        System.out.print("Modify tags? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Enter up to 3 tags:");
            newState.setTags(readUpTo(3, "Tag"));
        }

        service.modifyBook(book, newState);
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────
    private void printBookList(List<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    private List<String> readUpTo(int max, String label) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            System.out.print(label + " " + i + " (Enter to skip): ");
            String val = scanner.nextLine().trim();
            if (val.isEmpty()) break;
            list.add(val);
        }
        return list;
    }

    private int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
