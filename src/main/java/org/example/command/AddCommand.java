package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;


public class AddCommand extends AbstractCommand{

    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        Ticket ticket = TicketInput.generateTicket();
        collectionManager.add(ticket);
        logger.info("Added Ticket: {}", ticket);
        return "Ticket added successfully.";

    }
}
