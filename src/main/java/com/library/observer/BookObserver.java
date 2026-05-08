package com.library.observer;

import com.library.model.Book;

public interface BookObserver {
    void onBookBorrowed(Book book);
    void onBookReturned(Book book);
}
