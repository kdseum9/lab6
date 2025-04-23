package org.example.command;

import org.example.manager.CollectionManager;

public class SaveCommand extends AbstractCommand {
    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        collectionManager.getXmlManipulator().write();
        System.out.println("Collection saved to file.");
        logger.info("Save command executed successfully");
        return null;
    }
}
