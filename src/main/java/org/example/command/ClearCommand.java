package org.example.command;

import org.example.manager.CollectionManager;

public class ClearCommand extends AbstractCommand{

    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        if (collectionManager.getCollection().isEmpty()) {
            logger.warn("Collection is already empty.");
            return "Collection is already empty.";
        }
        collectionManager.getCollection().clear();
        logger.info("Collection successfully cleared.");
        return "Collection successfully cleared.";
    }
}
