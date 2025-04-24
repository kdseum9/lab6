package org.example.command;

import org.example.manager.CollectionManager;

/**
 * Команда {@code exit} завершает выполнение программы.
 * Используется для безопасного выхода пользователя.
 *
 * <p>После вызова этой команды программа завершает выполнение с кодом {@code 0}.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class ExitCommand extends AbstractCommand {

    /**
     * Выполняет команду завершения программы.
     *
     * @param args аргументы команды (не используются)
     * @param collectionManager менеджер коллекции (не используется)
     * @return {@code null} (никогда не возвращается, поскольку программа завершается)
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        logger.info("Exiting the program by user command.");
        System.exit(0);
        return null;
    }
}
