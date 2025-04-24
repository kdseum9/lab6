package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;
import org.example.model.generator.TicketInput;

/**
 * Команда для добавления нового объекта {@link Ticket} в коллекцию.
 * Использует ввод данных через {@link TicketInput}.
 *
 * @author kdseum9
 * @version 1.0
 */
public class AddCommand extends AbstractCommand {

    /**
     * Выполняет добавление нового элемента {@link Ticket} в коллекцию.
     * Ввод данных осуществляется с помощью {@link TicketInput}.
     *
     * @param arg аргументы команды (не используются в данной реализации)
     * @param collectionManager менеджер коллекции, в которую будет добавлен новый элемент
     * @return сообщение об успешном добавлении
     */
    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        Ticket ticket = TicketInput.generateTicket();
        collectionManager.add(ticket);
        logger.info("Added Ticket: {}", ticket);
        return "Ticket added successfully.";
    }
}
