package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;

/**
 * Команда для добавления нового объекта Ticket в коллекцию,
 * если его цена больше, чем у любого другого объекта в коллекции.
 * Если коллекция пуста, элемент добавляется без сравнения.
 * <p>
 * Author kdseum9
 * version 1.0
 */
public class AddIfMaxCommand extends AbstractCommand {

    /**
     * Выполняет команду add_if_max.
     * Генерирует новый Ticket, сравнивает его цену с максимальной ценой в коллекции,
     * и добавляет его, если она больше.
     * <p>
     * param args аргументы команды (не используются)
     * param collectionManager менеджер, управляющий коллекцией
     * return результат выполнения команды
     */
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
        } else {
            logger.info("Not added ticket with price: {} <= {}", ticket.getPrice(), maxPrice);
            return "Not added ticket. Price not greater than max.";
        }
    }
}
