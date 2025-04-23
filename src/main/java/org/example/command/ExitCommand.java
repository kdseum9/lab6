package org.example.command;

import org.example.manager.CollectionManager;

public class ExitCommand extends AbstractCommand{
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        logger.info("Exiting the program by user command.");
        System.exit(0);
        return null;
    }
}
