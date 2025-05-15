package org.example.server.command;

import org.example.server.manager.CollectionManager;
import org.example.common.*;

/**
 * Команда для очистки коллекции.
 * <p>
 * Удаляет все элементы из коллекции, если она не пуста.
 * </p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class ClearCommand extends AbstractCommand {

    /**
     * Выполняет команду clear.
     * Очищает коллекцию, если она содержит элементы.
     *
     * @param request запрос, содержащий команду и параметры
     * @param collectionManager менеджер, управляющий коллекцией
     * @return результат выполнения команды в виде объекта Response
     */
    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        if (collectionManager.getCollection().isEmpty()) {
            logger.warn("Collection is already empty.");
            return new Response("Collection is already empty.", null);
        }
        collectionManager.getCollection().clear();
        logger.info("Collection successfully cleared.");
        return new Response("Collection successfully cleared.", null);
    }
}
