package org.example.manager;

import org.example.exceptions.WrongArgumentException;
import org.example.model.enums.TicketType;
import org.example.model.enums.VenueType;

/**
 * Валидатор полей модели Ticket.
 * <p>Проверяет корректность ввода для различных полей билета и места проведения.</p>
 */
public class TicketValidator {

    /**
     * Проверка корректности ID.
     *
     * @param arg строка с предполагаемым числом
     * @throws WrongArgumentException если значение не является числом
     */
    public static void idIsOK(String arg) throws WrongArgumentException {
        try {
            Long.parseLong(arg);
        } catch (Exception e) {
            throw new WrongArgumentException("Invalid format for ID.");
        }
    }

    /**
     * Проверяет, что поле не пустое.
     *
     * @param arg   значение поля
     * @param model имя поля для отображения в сообщении
     * @throws WrongArgumentException если значение пустое или содержит только пробелы
     */
    public static void inputIsNotEmpty(String arg, String model) throws WrongArgumentException {
        if (arg == null || arg.trim().isEmpty()) {
            throw new WrongArgumentException(model + " must not be empty.");
        }
    }

    /**
     * Проверка корректности цены.
     *
     * @param arg значение цены
     * @throws WrongArgumentException если значение не является положительным числом
     */
    public static void priceIsOk(String arg) throws WrongArgumentException {
        try {
            int n = Integer.parseInt(arg);
            if (n <= 0) {
                throw new WrongArgumentException("Price must be greater than 0.");
            }
        } catch (Exception e) {
            throw new WrongArgumentException("Invalid format for price.");
        }
    }

    /**
     * Проверка корректности скидки.
     *
     * @param arg значение скидки
     * @throws WrongArgumentException если значение не входит в диапазон (0, 100)
     */
    public static void discountIsOk(String arg) throws WrongArgumentException {
        try {
            double n = Double.parseDouble(arg);
            if (n <= 0) {
                throw new WrongArgumentException("Discount must be greater than 0.");
            } else if (n >= 100) {
                throw new WrongArgumentException("Discount must be less than 100.");
            }
        } catch (Exception e) {
            throw new WrongArgumentException("Invalid format for discount.");
        }
    }

    /**
     * Проверка корректности типа билета.
     *
     * @param arg значение перечисления TicketType
     * @throws WrongArgumentException если значение не совпадает с возможными типами
     */
    public static void typeIsOk(String arg) throws WrongArgumentException {
        try {
            TicketType.valueOf(arg);
        } catch (Exception e) {
            throw new WrongArgumentException("Invalid TicketType value.");
        }
    }

    /**
     * Проверка корректности вместимости.
     *
     * @param arg строка, представляющая целое число
     * @throws WrongArgumentException если вместимость не положительная или не число
     */
    public static void capacityIsOk(String arg) throws WrongArgumentException {
        try {
            int n = Integer.parseInt(arg);
            if (n <= 0) {
                throw new WrongArgumentException("Capacity must be greater than 0.");
            }
        } catch (Exception e) {
            throw new WrongArgumentException("Invalid format for capacity.");
        }
    }

    /**
     * Проверка корректности типа места проведения.
     *
     * @param arg значение перечисления VenueType
     * @throws WrongArgumentException если значение не пустое и не соответствует перечислению
     */
    public static void venueTypeIsOk(String arg) throws WrongArgumentException {
        if (!arg.isEmpty()) {
            try {
                VenueType.valueOf(arg);
            } catch (IllegalArgumentException e) {
                throw new WrongArgumentException("Invalid VenueType value.");
            }
        }
    }
}
