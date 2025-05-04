package org.example;

import org.example.util.Console;

import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс приложения. Содержит точку входа — метод {@code main}.
 * Ожидает один аргумент командной строки — путь к XML-файлу с коллекцией билетов.
 *
 * <p>Если аргумент передан, запускается консольное приложение с загрузкой коллекции
 * из указанного файла.</p>
 *
 * Пример запуска:
 * <pre>{@code
 * java -jar ticket-app.jar /path/to/tickets.xml
 * }</pre>
 */
public class Main {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки. Ожидается один аргумент — путь к XML-файлу.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            Console console = new Console(args[0]);
            console.start();
        }
    }
}

