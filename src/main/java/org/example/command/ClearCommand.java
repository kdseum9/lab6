package org.example.command;

import org.example.manager.CollectionManager;

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
     * @param args аргументы команды (не используются)
     * @param collectionManager менеджер, управляющий коллекцией
     * @return результат выполнения команды
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        if (collectionManager.getCollection().isEmpty()) {
            logger.warn("Collection is already empty.");
            return "Collection is already empty.";
        }
        collectionManager.getCollection().clear();
        logger.info("Collection successfully cleared.");
        return "Collection successfully cleared.";
    }
}
