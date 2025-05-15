package org.example.common;

import org.example.common.model.Ticket;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;  // Имя команды
    private String[] args;       // Аргументы команды
    private Ticket ticket;       // Объект Ticket (если он передается)

    public Request(String commandName, String[] args, Ticket ticket) {
        this.commandName = commandName;
        this.args = args;
        this.ticket = ticket;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public Ticket getTicket() {
        return ticket;
    }


    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", args=" + String.join(",", args) +
                ", ticket=" + ticket +
                '}';
    }
}
