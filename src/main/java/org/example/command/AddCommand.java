package org.example.command;

import org.example.manager.CollectionManager;
import org.example.share.Request;
import org.example.share.Response;
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
     * Выполняет команду с заданными аргументами и менеджером коллекции.
     * Возвращает результат выполнения команды в виде объекта Response.
     *
     * @param request запрос, содержащий команду и параметры
     * @param collectionManager объект, управляющий коллекцией элементов
     * @return результат выполнения команды в виде объекта Response
     */
    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        Ticket ticket = request.getTicket();  // Генерация нового билета через TicketInput

        if (ticket == null) {
            return new Response("ERROR: Failed to generate ticket.", null);
        }

        collectionManager.add(ticket);  // Добавляем новый билет в коллекцию
        logger.info("Ticket added: {}", ticket);

        return new Response("Ticket added successfully.", ticket);  // Возвращаем успешный ответ
    }
}
