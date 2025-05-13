package org.example.command;

import org.example.manager.CollectionManager;
import org.example.share.Request;
import org.example.share.Response;

/**
 * Команда для отображения информации о коллекции.
 */
public class InfoCommand extends AbstractCommand {

    /**
     * Выполняет команду info.
     *
     * @param request объект запроса
     * @param manager менеджер коллекции
     * @return объект ответа с информацией о коллекции
     */
    @Override
    public Response execute(Request request, CollectionManager manager) {
        logger.info("Executing 'info' command");

        StringBuilder sb = new StringBuilder();
        sb.append("Application Info:\n");
        sb.append("- Collection type: ").append(manager.getCollection().getClass().getSimpleName()).append("\n");
        sb.append("- Collection size: ").append(manager.getCollection().size()).append("\n");
        sb.append("- Initialization time: ").append(manager.getTimeOfInitial()).append("\n");

        return new Response(sb.toString(), null);
    }
}
