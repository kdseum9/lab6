package org.example.command;

import org.example.manager.CollectionManager;

/**
 * <p>Команда для сохранения текущей коллекции в XML-файл.</p>
 *
 * <p>Выполняется с помощью {@code XmlManipulator}, связанного с {@link CollectionManager}.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class SaveCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>save</code>, вызывая метод {@code write()} у {@code XmlManipulator}.</p>
     *
     * @param arg аргументы команды (не используются)
     * @param collectionManager менеджер коллекции, содержащий XML-манипулятор
     * @return {@code null} после завершения
     */
    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        collectionManager.getXmlManipulator().write();
        System.out.println("Collection saved to file.");
        logger.info("Save command executed successfully");
        return null;
    }
}
