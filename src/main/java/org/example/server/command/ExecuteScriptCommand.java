package org.example.server.command;

import org.example.server.manager.CollectionManager;
import org.example.server.manager.CommandManager;
import org.example.server.manager.TicketValidator;
import org.example.common.model.Coordinates;
import org.example.common.model.Venue;
import org.example.common.model.enums.TicketType;
import org.example.common.model.enums.VenueType;
import org.example.common.*;
import org.example.common.model.Ticket;

import java.io.*;
import java.util.Arrays;
import java.util.Stack;

/**
 * Команда {@code execute_script} исполняет команды из указанного скриптового файла.
 * Поддерживает выполнение как обычных команд, так и команд, требующих создания {@link Ticket}.
 * Предотвращает рекурсивное выполнение скриптов.
 * <p>
 * Поддерживаемые специальные команды: {@code add, update, remove_greater, remove_lower, add_if_max}.
 * Эти команды считывают параметры объекта {@link Ticket} из строк файла.
 *
 * @author kdseum9
 * @version 1.0
 */
public class ExecuteScriptCommand extends AbstractCommand {

    private final Stack<String> files = new Stack<>();
    private final String[] extraCommands = {"add", "update", "remove_greater", "remove_lower", "add_if_max"};
    private final CommandManager manager;

    /**
     * Конструктор команды.
     *
     * @param manager диспетчер команд, используемый для выполнения обычных команд
     */
    public ExecuteScriptCommand(CommandManager manager) {
        this.manager = manager;
    }

    /**
     * Выполняет скрипт из указанного файла.
     *
     * @param request объект запроса, содержащий параметры (в том числе путь к файлу)
     * @param collectionManager менеджер коллекции, к которому применяются команды
     * @return объект Response с результатом выполнения команды
     */
    @Override
    public Response execute(Request request, CollectionManager collectionManager) {
        String[] args = request.getArgs();

        if (args.length < 2) {
            logger.error("No script file specified.");
            return new Response("Error: No script file specified.", null);
        }

        String fileName = args[1];
        if (files.contains(fileName)) {
            logger.warn("Recursion detected for file '{}'. Skipping.", fileName);
            return new Response("Recursion skipped for file: " + fileName, null);
        }

        files.push(fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String command;
            while ((command = readNonEmptyLine(reader)) != null) {
                try {
                    String baseCommand = command.trim().split(" ")[0];
                    Ticket ticket = null;
                    if (Arrays.asList(extraCommands).contains(baseCommand)) {
                        ticket = parseTicketFromScript(reader);
                        if (ticket == null) {
                            logger.warn("Skipping command '{}' due to invalid ticket data.", baseCommand);
                            continue;
                        }
                        Request commandRequest = new Request(baseCommand, new String[]{baseCommand}, ticket);

                        switch (baseCommand) {
                            case "add" -> collectionManager.add(ticket);
                            case "update" -> collectionManager.update(ticket);
                            case "remove_greater" -> collectionManager.remove_greater(ticket);
                            case "remove_lower" -> collectionManager.remove_lower(ticket);
                            case "add_if_max" -> collectionManager.add_if_max(ticket);
                        }

                        logger.info("Executed extra command: {}", baseCommand);
                    } else {
                        Request commandRequest = new Request(command.trim(), args, ticket);
                        manager.doCommand(commandRequest, collectionManager);
                        logger.info("Executed command: {}", command);
                    }
                } catch (Exception e) {
                    logger.error("Error processing command in script '{}': {}", fileName, e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading script file '{}': {}", fileName, e.getMessage());
            return new Response("Error reading script file.", null);
        } finally {
            files.pop();
        }

        return new Response("Script executed successfully.", null);
    }

    /**
     * Считывает непустую строку из файла.
     *
     * @param reader буферизированный ридер
     * @return строка без пустых строк, либо {@code null}, если достигнут конец файла
     * @throws IOException при ошибке чтения
     */
    private String readNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (line != null && line.trim().isEmpty());
        return line;
    }

    /**
     * Считывает поля {@link Ticket} из файла скрипта.
     *
     * @param reader ридер, читающий скриптовый файл
     * @return {@link Ticket}, если все поля корректны, иначе {@code null}
     */
    private Ticket parseTicketFromScript(BufferedReader reader) {
        try {
            String name = readNonEmptyLine(reader);
            float x = Float.parseFloat(readNonEmptyLine(reader));
            long y = Long.parseLong(readNonEmptyLine(reader));
            int price = Integer.parseInt(readNonEmptyLine(reader));
            double discount = Double.parseDouble(readNonEmptyLine(reader));
            TicketType type = TicketType.valueOf(readNonEmptyLine(reader));

            String venueName = readNonEmptyLine(reader);
            int capacity = Integer.parseInt(readNonEmptyLine(reader));
            VenueType venueType = VenueType.valueOf(readNonEmptyLine(reader));

            Ticket ticket = new Ticket();
            ticket.setName(name);
            ticket.setCoordinates(new Coordinates(x, y));
            ticket.setPrice(price);
            ticket.setDiscount(discount);
            ticket.setType(type);
            ticket.setVenue(new Venue(venueName, capacity, venueType));

            if (!TicketValidator.validateTicket(ticket)) {
                logger.warn("Ticket validation failed.");
                return null;
            }

            return ticket;
        } catch (Exception e) {
            logger.error("Invalid input while parsing ticket: {}", e.getMessage());
            return null;
        }
    }
}
