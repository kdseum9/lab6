package org.example.command;

import org.example.manager.CollectionManager;
import org.example.manager.CommandManager;
import org.example.model.Coordinates;
import org.example.model.Ticket;
import org.example.model.Validation.TicketValidator;
import org.example.model.Venue;
import org.example.model.enums.TicketType;
import org.example.model.enums.VenueType;

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
     * @param args аргументы, где второй элемент (args[1]) — путь к скриптовому файлу
     * @param collectionManager менеджер коллекции, к которому применяются команды
     * @return строка с результатом выполнения
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        if (args.length < 2) {
            logger.error("No script file specified.");
            return "Error: No script file specified.";
        }

        String fileName = args[1];
        if (files.contains(fileName)) {
            logger.warn("Recursion detected for file '{}'. Skipping.", fileName);
            return "Recursion skipped for file: " + fileName;
        }

        files.push(fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String command;
            while ((command = readNonEmptyLine(reader)) != null) {
                try {
                    String baseCommand = command.trim().split(" ")[0];
                    if (Arrays.asList(extraCommands).contains(baseCommand)) {
                        Ticket ticket = parseTicketFromScript(reader);
                        if (ticket == null) {
                            logger.warn("Skipping command '{}' due to invalid ticket data.", baseCommand);
                            continue;
                        }

                        switch (baseCommand) {
                            case "add" -> collectionManager.add(ticket);
                            case "update" -> collectionManager.update(ticket);
                            case "remove_greater" -> collectionManager.remove_greater(ticket);
                            case "remove_lower" -> collectionManager.remove_lower(ticket);
                            case "add_if_max" -> collectionManager.add_if_max(ticket);
                        }

                        logger.info("Executed extra command: {}", baseCommand);
                    } else {
                        manager.doCommand(command.trim().split(" "));
                        logger.info("Executed command: {}", command);
                    }
                } catch (Exception e) {
                    logger.error("Error processing command in script '{}': {}", fileName, e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading script file '{}': {}", fileName, e.getMessage());
            return "Error reading script file.";
        } finally {
            files.pop();
        }

        return "Script executed successfully.";
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
