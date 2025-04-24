package org.example.command;

import org.example.manager.CollectionManager;

/**
 * <p>Команда для отображения информации о текущей коллекции.</p>
 * <p>Выводит тип коллекции, её размер и время инициализации.</p>
 *
 * <p>Используется для получения общей информации о состоянии программы.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class InfoCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>info</code>.</p>
     * <p>Выводит в консоль следующую информацию:</p>
     * <ul>
     *     <li>Тип используемой коллекции</li>
     *     <li>Размер коллекции</li>
     *     <li>Время инициализации коллекции</li>
     * </ul>
     *
     * @param arg аргументы команды (не используются)
     * @param manager менеджер, управляющий коллекцией
     * @return <code>null</code> — результат выводится в консоль
     */
    @Override
    public String execute(String[] arg, CollectionManager manager) {
        logger.info("Executing 'info' command");

        System.out.println("Application Info:");
        System.out.println("- Collection type: " + manager.getCollection().getClass().getSimpleName());
        System.out.println("- Collection size: " + manager.getCollection().size());
        System.out.println("- Initialization time: " + manager.getTimeOfInitial());

        return null;
    }
}
