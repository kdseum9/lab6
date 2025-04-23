package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

public class ShowCommand extends AbstractCommand{
    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("The collection is empty.");
            logger.info("Show command executed: collection is empty.");
        } else {
            System.out.println("Displaying all elements in the collection:");
            for (Ticket ticket : collectionManager.getCollection()) {
                System.out.println(ticket);
            }
            logger.info("Show command executed: displayed all tickets.");
        }
        return null;
    }
}
