package org.example.server.command;

import org.example.common.exceptions.NoElementException;
import org.example.server.manager.CollectionManager;
import org.example.common.model.Ticket;
import org.example.common.Request;
import org.example.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Команда для обновления элемента коллекции по заданному ID.</p>
 *
 * <p>Если элемент с указанным ID не найден, возвращает соответствующий объект {@link Response} с сообщением об ошибке.</p>
 *
 * <p><strong>Пример использования:</strong> <code>update 5 {element_data}</code></p>
 *
 * @version 1.3
 */
public class UpdateCommand extends AbstractCommand {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCommand.class);

    /**
     * Выполняет команду <code>update id</code>. Обновляет {@link Ticket} с заданным ID,
     * используя данные из объекта запроса.
     *
     * @param request         объект запроса, содержащий ID элемента для обновления в аргументах и новый объект Ticket в данных
     * @param collectionManager менеджер коллекции
     * @return объект {@link Response} с сообщением об успехе или ошибке
     * @throws NoElementException если элемент с указанным ID не найден (хотя теперь обрабатывается и возвращается в Response)
     */
    @Override
    public Response execute(Request request, CollectionManager collectionManager) throws NoElementException {
        if (request.getArgs().length < 1) {
            logger.warn("Update command failed: missing ID argument.");
            return new Response("Please provide the ID of the element to update.", null);
        }

        long id;
        try {
            id = Long.parseLong(request.getArgs()[0]);
        } catch (NumberFormatException e) {
            logger.warn("Update command failed: invalid ID format: {}", request.getArgs()[0]);
            return new Response("Invalid ID format. Please provide a numeric ID.", null);
        }

        Ticket existingTicket = collectionManager.getById(id);
        if (existingTicket == null) {
            logger.warn("Update command failed: no element found with ID: {}", id);
            return new Response("No ticket found with ID " + id + " in the collection.", null);
        }

        Ticket updatedTicket = (Ticket) request.getTicket();
        if (updatedTicket == null) {
            logger.warn("Update command failed: no ticket data provided in the request.");
            return new Response("No ticket data provided for update.", null);
        }

        updatedTicket.setId(id);
        collectionManager.update(updatedTicket);
        logger.info("Ticket with ID {} updated successfully.", id);
        return new Response("Ticket with ID " + id + " updated successfully.", updatedTicket);
    }
}