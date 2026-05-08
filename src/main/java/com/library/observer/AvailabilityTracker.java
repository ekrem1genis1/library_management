package com.library.observer;

import com.library.model.Book;

public class AvailabilityTracker implements BookObserver {

    @Override
    public void onBookBorrowed(Book book) {
        book.setAvailable(false);
        book.incrementBorrowCount();
        System.out.println("[Tracker] \"" + book.getTitle() + "\" is now BORROWED. Total borrows: " + book.getBorrowCount());
    }

    @Override
    public void onBookReturned(Book book) {
        book.setAvailable(true);
        System.out.println("[Tracker] \"" + book.getTitle() + "\" is now AVAILABLE.");
    }
}
