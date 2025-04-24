package org.example.model.generator;

import org.example.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Класс для создания объекта {@link Ticket} с вводом данных с консоли.
 * Включает в себя генерацию всех необходимых полей билета.
 */
public class TicketInput {

    private static final Logger logger = LoggerFactory.getLogger(TicketInput.class);

    /**
     * Генерирует новый объект {@link Ticket}, запрашивая все данные у пользователя.
     * @return Сгенерированный объект Ticket
     */
    public static Ticket generateTicket() {
        logger.info("Generating new ticket...");
        Ticket ticket = new Ticket();
        ticket.setId(IdGenerator.generateId());
        ticket.setName(generateName());
        ticket.setCoordinates(CoordinatesGenerator.generateCoordinates());
        ticket.setPrice(generatePrice());
        ticket.setDiscount(generateDiscount());
        ticket.setVenue(VenueGenerator.generateVenue());
        logger.info("Ticket successfully generated with ID {}", ticket.getId());
        return ticket;
    }

    /**
     * Запрашивает у пользователя имя билета.
     * @return Введённое имя
     */
    private static String generateName() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter name: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                logger.info("Name set: {}", input);
                return input;
            }
            logger.warn("Empty name entered by user");
            System.out.println("Name cannot be empty!");
        }
    }

    /**
     * Запрашивает у пользователя цену билета.
     * @return Цена билета в виде целого числа
     */
    private static int generatePrice() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter price (number > 0): ");
                String input = scanner.nextLine().trim();
                double price = Double.parseDouble(input);
                if (price > 0) {
                    logger.info("Price set: {}", price);
                    return (int) price;
                }
                logger.warn("Price must be greater than 0, got: {}", price);
                System.out.println("Price must be greater than 0!");
            } catch (NumberFormatException e) {
                logger.warn("Invalid number format for price");
                System.out.println("Invalid number format!");
            }
        }
    }

    /**
     * Запрашивает у пользователя скидку на билет.
     * Может вернуть null, если скидка не указана.
     * @return Скидка как Double или null
     */
    private static Double generateDiscount() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter discount (0-100, or empty for null): ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    logger.info("No discount entered");
                    return null;
                }

                double discount = Double.parseDouble(input);
                if (discount > 0 && discount <= 100) {
                    logger.info("Discount set: {}", discount);
                    return discount;
                }

                logger.warn("Discount out of range: {}", discount);
                System.out.println("Discount must be between 0 and 100!");

            } catch (NumberFormatException e) {
                logger.warn("Invalid number format for discount");
                System.out.println("Invalid number format!");
            }
        }
    }
}
