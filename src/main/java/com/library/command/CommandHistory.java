package com.library.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandHistory {
    private final Deque<Command> history = new ArrayDeque<>();

    public void execute(Command command) {
        command.execute();
        history.push(command);
    }

    public void undo() {
        if (history.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        history.pop().undo();
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
