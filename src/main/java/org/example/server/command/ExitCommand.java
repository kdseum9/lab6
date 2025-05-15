package org.example.server.command;

import org.example.server.manager.CollectionManager;
import org.example.common.Request;
import org.example.common.Response;

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
     * @param request объект Request, который содержит команду и параметры
     * @param collectionManager менеджер коллекции (не используется в данном случае)
     * @return объект Response, содержащий результат выполнения команды
     */
    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        logger.info("Exiting the program by user command.");

        // Завершаем выполнение программы
        System.exit(0);

        // Мы не вернемся из этого места, но нужно вернуть объект Response
        return new Response("Exiting the program by user command.", null);
    }
}
