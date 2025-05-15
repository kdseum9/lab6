package org.example.server.command;

import org.example.server.manager.CollectionManager;
import org.example.common.Request;
import org.example.common.Response;
import org.example.common.model.Ticket;

import java.util.OptionalDouble;

/**
 * Команда для добавления объекта Ticket в коллекцию,
 * если его цена больше, чем у любого другого объекта.
 */
public class AddIfMaxCommand extends AbstractCommand {

    /**
     * Выполняет команду с заданными аргументами и менеджером коллекции.
     * Возвращает результат выполнения команды в виде объекта Response.
     *
     * @param request запрос, содержащий команду и параметры
     * @param collectionManager объект, управляющий коллекцией элементов
     * @return результат выполнения команды в виде объекта Response
     */
    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        Ticket ticket = request.getTicket();  // Получаем билет из запроса

        if (ticket == null) {
            return new Response("ERROR: Ticket data is required but not provided.", null);
        }

        if (collectionManager.getCollection().isEmpty()) {
            collectionManager.add(ticket);
            logger.info("Added ticket with price: {}", ticket.getPrice());
            return new Response("Ticket added.", ticket);
        }

        OptionalDouble maxPriceOpt = collectionManager.getCollection().stream()
                .mapToDouble(Ticket::getPrice)
                .max();

        double maxPrice = maxPriceOpt.orElse(Double.MIN_VALUE);

        if (ticket.getPrice() > maxPrice) {
            collectionManager.add(ticket);
            logger.info("Added ticket with price: {}", ticket.getPrice());
            return new Response("Ticket added.", ticket);
        } else {
            logger.info("Ticket not added: price {} <= max price {}", ticket.getPrice(), maxPrice);
            return new Response("Ticket not added. Price is not greater than the maximum.", null);
        }
    }
}
