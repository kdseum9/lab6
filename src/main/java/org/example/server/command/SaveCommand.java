package org.example.server.command;

import org.example.server.manager.CollectionManager;

public class SaveCommand {

    public void execute(CollectionManager collectionManager) {
        collectionManager.getXmlManipulator().write();
    }
}
