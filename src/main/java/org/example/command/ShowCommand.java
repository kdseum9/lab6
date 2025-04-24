package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

/**
 * <p>Команда для отображения всех элементов коллекции {@link Ticket}.</p>
 *
 * <p>Если коллекция пуста, будет выведено соответствующее сообщение.</p>
 *
 * <p><strong>Пример использования:</strong> <code>show</code></p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class ShowCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>show</code>, выводя все элементы в коллекции.</p>
     *
     * @param arg аргументы команды (не используются)
     * @param collectionManager менеджер, управляющий коллекцией
     * @return {@code null} после выполнения
     */
    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("The collection is empty.");
            logger.info("Show command executed: collection is empty.");
        } else {
            System.out.println("Displaying all elements in the collection:");
            for (Ticket ticket : collectionManager.getCollection()) {
                System.out.println(ticket);
            }
            logger.info("Show command executed: displayed all tickets.");
        }
        return null;
    }
}
