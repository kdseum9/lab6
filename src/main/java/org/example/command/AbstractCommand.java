package org.example.command;

import org.example.exceptions.NoElementException;
import org.example.manager.CollectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Абстрактный класс для всех команд, реализующих определённое поведение в приложении.
 * Все команды должны реализовывать метод {@code execute}, принимающий аргументы
 * и объект менеджера коллекции.
 *
 * @author kdseum9
 * @version 1.0
 */
public abstract class AbstractCommand {

    /**
     * Логгер для записи информации и ошибок, связанных с выполнением команды.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Выполняет команду с заданными аргументами и менеджером коллекции.
     *
     * @param args массив аргументов команды, где {@code args[0]} — имя команды, остальные — параметры
     * @param collectionManager объект, управляющий коллекцией элементов
     * @return результат выполнения команды в виде строки
     * @throws NoElementException если требуемый элемент не найден в коллекции
     */
    public abstract String execute(String[] args, CollectionManager collectionManager) throws NoElementException;
}
