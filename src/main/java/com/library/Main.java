package com.library;

import com.library.service.LibraryService;
import com.library.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        ConsoleUI ui = new ConsoleUI(service);
        ui.start();
    }
}
