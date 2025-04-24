package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда {@code filter_starts_with_name} фильтрует элементы коллекции по префиксу имени.
 *
 * <p>Возвращает все {@link Ticket}, у которых поле {@code name} начинается с указанной строки.</p>
 *
 * Пример использования:
 * <pre>{@code
 * filter_starts_with_name Abc
 * }</pre>
 *
 * @author kdseum9
 * @version 1.0
 */
public class FilterCommand extends AbstractCommand {

    /**
     * Выполняет фильтрацию коллекции по префиксу имени.
     *
     * @param args аргументы команды, где args[1] — строка-префикс
     * @param collectionManager менеджер, содержащий коллекцию элементов
     * @return переданный фильтр (или {@code null}, если аргумент не задан)
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        if (args.length < 2) {
            logger.warn("Filter argument not provided.");
            System.out.println("Didn't choose the filter");
            return null;
        }

        String filter = args[1];
        List<Ticket> filtered = new ArrayList<>();

        for (Ticket t : collectionManager.getCollection()) {
            if (t.getName() != null && t.getName().startsWith(filter)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No elements start with \"" + filter + "\"");
        } else {
            System.out.println("Found " + filtered.size() + " elements:");
            for (Ticket t : filtered) {
                System.out.println(t);
            }
        }

        return filter;
    }
}
