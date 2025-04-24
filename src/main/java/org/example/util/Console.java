package org.example.util;

import org.example.manager.CollectionManager;
import org.example.manager.CommandManager;

import java.util.Scanner;

/**
 * Класс Console предоставляет интерфейс командной строки для взаимодействия с пользователем.
 * Он обрабатывает ввод пользователя и передаёт команды {@link CommandManager}.
 */
public class Console {
    private CollectionManager collectionManager;
    private CommandManager commandManager;

    /**
     * Конструктор класса Console.
     * Инициализирует менеджеры коллекций и команд.
     *
     * @param fileName имя файла, из которого будет загружена коллекция
     */
    public Console(String fileName) {
        this.collectionManager = new CollectionManager(fileName);
        this.commandManager = new CommandManager(collectionManager);
    }

    /**
     * Запускает цикл обработки ввода пользователя.
     * Ожидает ввод строки, разбивает её по пробелам и передаёт в {@link CommandManager}.
     */
    public void start() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            commandManager.doCommand(line.split(" "));
        }
    }
}
