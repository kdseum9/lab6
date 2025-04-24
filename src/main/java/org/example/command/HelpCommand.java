package org.example.command;

import org.example.manager.CollectionManager;

/**
 * <p>Команда для отображения справки по всем доступным командам.</p>
 * <p>Выводит список всех команд с кратким описанием в консоль.</p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class HelpCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>help</code>.</p>
     * <p>Выводит список всех доступных команд с кратким описанием.</p>
     *
     * @param arg аргументы команды (не используются)
     * @param collectionManager менеджер, управляющий коллекцией
     * @return пустая строка, так как результат выводится в консоль
     */
    @Override
    public String execute(String[] arg, CollectionManager collectionManager) {
        String helpText = (
                "Доступные команды:\n" +
                        "help : вывести справку по командам\n" +
                        "info : вывести информацию о коллекции\n" +
                        "show : вывести все элементы коллекции\n" +
                        "add {element} : добавить новый элемент\n" +
                        "update id {element} : обновить элемент по id\n" +
                        "remove_by_id id : удалить элемент по id\n" +
                        "clear : очистить коллекцию\n" +
                        "save : сохранить коллекцию в файл\n" +
                        "execute_script file_name : выполнить скрипт из файла\n" +
                        "exit : завершить программу\n" +
                        "add_if_max : добавить элемент в коллекцию, если его значение превышает значение наибольшего элемента коллекции\n" +
                        "remove_greater {element} : удалить элемент, превышающий заданный\n" +
                        "remove_lower {element} : удалить элемент, меньше заданного\n" +
                        "filter_starts_with_name name : вывести элементы, у которых name начинается с заданной подстроки\n" +
                        "print_descending : вывести элементы в порядке убывания\n" +
                        "print_field_descending_discount : вывести значения поля discount в порядке убывания"
        );

        logger.info("Help command executed.");
        System.out.println(helpText);

        return "";
    }
}
