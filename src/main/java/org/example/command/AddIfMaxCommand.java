package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AddIfMaxCommand extends AbstractCommand{
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {

        Ticket ticket = TicketInput.generateTicket();

        if (collectionManager.getCollection().isEmpty()) {
            collectionManager.add(ticket);
            logger.info("Added ticket with price: {}", ticket.getPrice());
            return "Ticket added.";

        }

        int maxPrice = collectionManager.getCollection().stream()
                .mapToInt(Ticket::getPrice)
                .max()
                .orElse(Integer.MIN_VALUE);
        if (ticket.getPrice() > maxPrice) {
            collectionManager.add(ticket);
            logger.info("Added ticket with price: {}", ticket.getPrice());
            return "Ticket added.";
        }
        else {
            logger.info("Not added ticket with price: {} <= {}", ticket.getPrice(), maxPrice);
            return "Not added ticket. Price not greater than max.";
        }
    }
    }

