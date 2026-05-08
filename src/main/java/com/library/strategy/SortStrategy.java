package com.library.strategy;

import com.library.model.Book;
import java.util.List;

public interface SortStrategy {
    void sort(List<Book> books);
}
