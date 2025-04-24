package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.*;

/**
 * <p>Команда для вывода элементов коллекции {@link Ticket} в порядке убывания.</p>
 * <p>Сортировка выполняется на основе реализации интерфейса {@link Comparable} в классе <code>Ticket</code>.</p>
 *
 * <p>Если коллекция пуста, пользователю выводится соответствующее сообщение.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class PrintDescendingCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>print_descending</code>.</p>
     * <p>Элементы коллекции сортируются в обратном порядке и выводятся в консоль.</p>
     *
     * @param args аргументы команды (не используются)
     * @param collectionManager менеджер, управляющий коллекцией
     * @return <code>null</code> — результат выводится в консоль
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        LinkedHashSet<Ticket> tickets = collectionManager.getCollection();

        if (tickets.isEmpty()) {
            System.out.println("The collection is empty.");
            logger.warn("Attempted to print descending, but the collection is empty.");
            return null;
        }

        List<Ticket> sortedList = new ArrayList<>(tickets);
        sortedList.sort(Collections.reverseOrder());

        System.out.println("Tickets in descending order:");
        for (Ticket ticket : sortedList) {
            System.out.println(ticket);
        }

        logger.info("Printed {} tickets in descending order.", sortedList.size());
        return null;
    }
}
