package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class FilterCommand extends AbstractCommand {
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        if (args.length < 2) {
            logger.warn("Filter argument not provided.");
            System.out.println("Didn't choose the filter");
            return null;
        }

        String filter = args[1];
        List<Ticket> filtered = new ArrayList<>();

        for (Ticket t : collectionManager.getCollection()) {
            if (t.getName() != null && t.getName().startsWith(filter)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No elements start with \"" + filter + "\"");
        } else {
            System.out.println("Found " + filtered.size() + " elements:");
            for (Ticket t : filtered) {
                System.out.println(t);
            }
        }

        return filter;
    }
}
