package org.example.command;

import org.example.exceptions.NoElementException;
import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;

/**
 * <p>Команда для обновления элемента коллекции по заданному ID.</p>
 *
 * <p>Если элемент с указанным ID не найден, будет выброшено исключение {@link NoElementException}.</p>
 *
 * <p><strong>Пример использования:</strong> <code>update 5</code></p>
 *
 * @version 1.0
 */
public class UpdateCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>update id</code>. Обновляет {@link Ticket} с заданным ID.</p>
     *
     * @param args аргументы команды, где args[1] должен содержать ID
     * @param collectionManager менеджер коллекции
     * @return сообщение об успехе или ошибке
     * @throws NoElementException если элемент с указанным ID не найден
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) throws NoElementException {
        if (args.length < 2) {
            logger.warn("Update command failed: ID not provided");
            return "Write ID";
        }

        long id;
        try {
            id = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            logger.warn("Update command failed: Incorrect ID format '{}'", args[1]);
            return "Incorrect ID";
        }

        Ticket existingTicket = collectionManager.getById(id);
        if (existingTicket == null) {
            logger.warn("Update command failed: No ticket found with ID {}", id);
            throw new NoElementException(id);
        }

        Ticket updatedTicket = TicketInput.generateTicket();
        updatedTicket.setId(id); // сохраняем старый ID

        collectionManager.delete(existingTicket);
        collectionManager.add(updatedTicket);

        logger.info("Ticket with ID {} successfully updated", id);
        return "Element with ID " + id + " successfully updated";
    }
}
