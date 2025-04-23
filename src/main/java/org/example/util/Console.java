package org.example.util;

import org.example.manager.CollectionManager;
import org.example.manager.CommandManager;

import java.util.Collection;
import java.util.Scanner;

public class Console {
    private CollectionManager collectionManager;
    private CommandManager commandManager;

    public Console(String fileName) {
        this.collectionManager = new CollectionManager(fileName);
        this.commandManager = new CommandManager(collectionManager);
    }

    public void start() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            commandManager.doCommand(line.split(" "));
        }
    }
}
