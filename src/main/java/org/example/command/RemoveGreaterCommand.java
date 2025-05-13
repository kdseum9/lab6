package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.share.Request;
import org.example.share.Response;

import java.util.Iterator;

public class RemoveGreaterCommand extends AbstractCommand {

    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        Ticket referenceTicket = request.getTicket(); // Получаем Ticket от клиента

        if (referenceTicket == null) {
            logger.warn("Reference ticket was not provided in the request.");
            return new Response("ERROR: Reference ticket was not provided.", null);
        }

        int removedCount = 0;
        Iterator<Ticket> iterator = collectionManager.getCollection().iterator();

        while (iterator.hasNext()) {
            Ticket currentTicket = iterator.next();
            if (referenceTicket.compareTo(currentTicket) < 0) {
                iterator.remove();
                removedCount++;
                logger.info("Removed ticket: {}", currentTicket);
            }
        }

        String resultMessage;
        if (removedCount == 0) {
            resultMessage = "No tickets were greater than the reference ticket.";
            logger.info("No tickets removed.");
        } else {
            resultMessage = "Total tickets removed: " + removedCount;
            logger.info("Removed {} ticket(s).", removedCount);
        }

        return new Response(resultMessage, null);
    }
}
