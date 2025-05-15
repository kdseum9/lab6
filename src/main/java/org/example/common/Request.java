package org.example.common;

import org.example.common.model.Ticket;

import java.io.Serializable;

/**
 * Класс {@code Request} представляет собой объект-запрос, отправляемый клиентом серверу.
 * <p>Содержит имя команды, аргументы и (опционально) объект {@link Ticket}, если команда требует его передачи.</p>
 * <p>Является сериализуемым, чтобы можно было передавать по сети.</p>
 */
public class Request implements Serializable {

    /**
     * Имя команды, которую необходимо выполнить на сервере.
     */
    private String commandName;

    /**
     * Аргументы команды, если они требуются.
     */
    private String[] args;

    /**
     * Объект {@link Ticket}, если команда предполагает передачу элемента коллекции.
     */
    private Ticket ticket;

    /**
     * Создаёт новый объект запроса.
     *
     * @param commandName имя команды
     * @param args массив аргументов
     * @param ticket объект {@link Ticket}, связанный с командой (может быть {@code null})
     */
    public Request(String commandName, String[] args, Ticket ticket) {
        this.commandName = commandName;
        this.args = args;
        this.ticket = ticket;
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Возвращает массив аргументов команды.
     *
     * @return аргументы команды
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Возвращает объект {@link Ticket}, если он присутствует.
     *
     * @return объект {@link Ticket} или {@code null}
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Возвращает строковое представление объекта запроса.
     *
     * @return строковое представление запроса
     */
    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", args=" + String.join(",", args) +
                ", ticket=" + ticket +
                '}';
    }
}
