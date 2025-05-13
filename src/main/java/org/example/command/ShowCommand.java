package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.share.Request;
import org.example.share.Response;

import java.util.LinkedHashSet;
import java.util.List;

public class ShowCommand extends AbstractCommand {

    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        LinkedHashSet<Ticket> collection = collectionManager.getCollection();

        if (collection.isEmpty()) {
            logger.info("Show command executed: collection is empty.");
            return new Response("The collection is empty.", null);
        }

        logger.info("Show command executed: {} tickets displayed.", collection.size());
        return new Response("Displaying all elements in the collection:", List.copyOf(collection));
    }
}
