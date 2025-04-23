package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;

import java.util.Iterator;

public class RemoveGreaterCommand extends AbstractCommand {

    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        Ticket referenceTicket = TicketInput.generateTicket();

        int removedCount = 0;

        Iterator<Ticket> iterator = collectionManager.getCollection().iterator();
        while (iterator.hasNext()) {
            Ticket currentTicket = iterator.next();
            if (referenceTicket.compareTo(currentTicket) < 0) {
                iterator.remove();
                removedCount++;
                logger.info("Removed ticket: {}", currentTicket);
                System.out.println("Ticket deleted: " + currentTicket);
            }
        }
        if (removedCount == 0) {
            System.out.println("No tickets were greater than the reference ticket.");
            logger.info("No tickets were removed. All are less than the reference.");
        } else {
            System.out.println("Total tickets removed: " + removedCount);
            logger.info("Total tickets removed: {}", removedCount);
        }

        return null;
    }
}