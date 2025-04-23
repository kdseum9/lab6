package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.*;

public class PrintDescendingCommand extends AbstractCommand{
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        LinkedHashSet<Ticket> Tickets = collectionManager.getCollection();

        if (Tickets.isEmpty()) {
            System.out.println("The collection is empty.");
            logger.warn("Attempted to print descending, but the collection is empty.");
            return null;
        }

        List<Ticket> sortedList = new ArrayList<>(Tickets);

        sortedList.sort(Collections.reverseOrder());

        System.out.println("Tickets in descending order:");
        for (Ticket ticket : sortedList) {
            System.out.println(ticket);
        }

        logger.info("Printed {} tickets in descending order.", sortedList.size());
        return null;
    }
}
