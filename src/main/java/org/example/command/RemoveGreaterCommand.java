package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;

import java.util.Iterator;

/**
 * <p>Команда для удаления всех элементов коллекции {@link Ticket},
 * которые превышают заданный сгенерированный элемент.</p>
 *
 * <p>Сравнение выполняется на основе метода {@link Ticket#compareTo(Ticket)}.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class RemoveGreaterCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>remove_greater</code>.</p>
     * <p>Генерирует новый {@link Ticket} и удаляет все элементы коллекции, которые "больше" его.</p>
     *
     * @param args аргументы команды (не используются)
     * @param collectionManager менеджер коллекции
     * @return результат выполнения команды (null)
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        Ticket referenceTicket = TicketInput.generateTicket();
        logger.info("Generated reference ticket: {}", referenceTicket);

        int removedCount = 0;

        Iterator<Ticket> iterator = collectionManager.getCollection().iterator();
        while (iterator.hasNext()) {
            Ticket currentTicket = iterator.next();
            if (referenceTicket.compareTo(currentTicket) < 0) {
                iterator.remove();
                removedCount++;
                logger.info("Removed ticket: {}", currentTicket);
                System.out.println("Ticket deleted: " + currentTicket);
            }
        }

        if (removedCount == 0) {
            System.out.println("No tickets were greater than the reference ticket.");
            logger.info("No tickets were removed. All are less than or equal to the reference.");
        } else {
            System.out.println("Total tickets removed: " + removedCount);
            logger.info("Total tickets removed: {}", removedCount);
        }

        return null;
    }
}
