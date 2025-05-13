package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.share.Request;
import org.example.share.Response;

import java.util.*;

/**
 * Команда для вывода элементов коллекции Ticket в порядке убывания.
 */
public class PrintDescendingCommand extends AbstractCommand {

    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        LinkedHashSet<Ticket> tickets = collectionManager.getCollection();

        if (tickets.isEmpty()) {
            logger.warn("Attempted to print descending, but the collection is empty.");
            return new Response("The collection is empty.", null);
        }

        List<Ticket> sortedList = new ArrayList<>(tickets);
        sortedList.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder("Tickets in descending order:\n");
        for (Ticket ticket : sortedList) {
            sb.append(ticket).append("\n");
        }

        logger.info("Printed {} tickets in descending order.", sortedList.size());
        return new Response(sb.toString(), null);
    }
}
