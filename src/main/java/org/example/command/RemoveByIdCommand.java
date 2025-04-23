package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;

import java.util.Optional;

public class RemoveByIdCommand extends AbstractCommand{
    @Override

    public String execute(String[] args, CollectionManager collectionManager) {
        if (args.length < 2) {
            System.out.println("Please provide an ID.");
            logger.warn("ID was not provided for remove_by_id command.");
            return null;
        }

        try {
            long id = Long.parseLong(args[1]);

            Optional<Ticket> ticketToRemove = collectionManager.getCollection()
                    .stream()
                    .filter(ticket -> ticket.getId() == id)
                    .findFirst();

            if (ticketToRemove.isPresent()) {
                collectionManager.getCollection().remove(ticketToRemove.get());
                System.out.println("Ticket with ID " + id + " was successfully removed.");
                logger.info("Removed ticket with ID: {}", id);
            } else {
                System.out.println("No ticket with ID " + id + " found.");
                logger.info("No ticket found with ID: {}", id);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a numeric ID.");
            logger.error("Invalid ID format: {}", args[1], e);
        }

        return null;
    }
}
