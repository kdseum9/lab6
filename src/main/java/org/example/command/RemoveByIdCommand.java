package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.Optional;

/**
 * <p>Команда для удаления объекта {@link Ticket} по его <code>ID</code>.</p>
 *
 * <p>Если объект с указанным ID найден в коллекции, он будет удалён.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class RemoveByIdCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>remove_by_id</code>.</p>
     *
     * @param args аргументы команды, где второй аргумент — ID
     * @param collectionManager менеджер коллекции
     * @return результат выполнения команды (null)
     */
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
