package org.example.manager;

import org.example.command.*;

import java.util.HashMap;

/**
 * Менеджер команд, осуществляющий регистрацию и выполнение доступных команд.
 * <p>Хранит отображение названий команд на соответствующие классы и передаёт управление при выполнении.</p>
 */
public class CommandManager {
    private final CollectionManager collectionManager;
    private final HashMap<String, AbstractCommand> commands = new HashMap<>();

    /**
     * Конструктор инициализирует все доступные команды.
     *
     * @param collectionManager менеджер коллекции, с которым работают команды
     */
    public CommandManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;

        // Регистрация всех доступных команд
        commands.put("info", new InfoCommand());
        commands.put("add", new AddCommand());
        commands.put("show", new ShowCommand());
        commands.put("save", new SaveCommand());
        commands.put("remove_greater", new RemoveGreaterCommand());
        commands.put("print_descending", new PrintDescendingCommand());
        commands.put("execute_script", new ExecuteScriptCommand(this));
        commands.put("help", new HelpCommand());
        commands.put("update", new UpdateCommand());
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("clear", new ClearCommand());
        commands.put("exit", new ExitCommand());
        commands.put("add_if_max", new AddIfMaxCommand());
        commands.put("remove_lower", new RemoveLowerCommand());
        commands.put("filter_starts_with", new FilterCommand());
        commands.put("print_field_descending_discount", new PrintFieldDescendingDiscountCommand());
    }

    /**
     * Выполняет команду, соответствующую переданному вводу.
     *
     * @param input массив строк, где первый элемент — название команды, а остальные — её аргументы
     */
    public void doCommand(String[] input) {
        String command = input[0];
        if (commands.containsKey(command)) {
            try {
                commands.get(command).execute(input, collectionManager);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}
