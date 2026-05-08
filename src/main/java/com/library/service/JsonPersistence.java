package com.library.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.library.model.Book;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonPersistence {
    private static final String FILE_PATH = "library_data.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(List<Book> books) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(books, writer);
        } catch (IOException e) {
            System.out.println("[Warning] Could not save data: " + e.getMessage());
        }
    }

    public static List<Book> load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = GSON.fromJson(reader, listType);
            return books != null ? books : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("[Warning] Could not load data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
