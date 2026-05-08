package com.library.command;

import com.library.model.Book;

public class ModifyBookCommand implements Command {
    private final Book book;
    private final Book beforeState;
    private final Book afterState;

    public ModifyBookCommand(Book book, Book afterState) {
        this.book = book;
        this.beforeState = new Book(book); // snapshot before
        this.afterState = afterState;
    }

    @Override
    public void execute() {
        applyState(book, afterState);
    }

    @Override
    public void undo() {
        applyState(book, beforeState);
        System.out.println("Undo successful. Book reverted to previous state.");
    }

    private void applyState(Book target, Book source) {
        target.setTitle(source.getTitle());
        target.setAuthor(source.getAuthor());
        target.setPublicationYear(source.getPublicationYear());
        target.setIsbn(source.getIsbn());
        target.setPublisher(source.getPublisher());
        target.setDescription(source.getDescription());
        target.setCategories(source.getCategories());
        target.setTags(source.getTags());
    }
}
