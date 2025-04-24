package org.example.model.Validation;

import org.example.model.Coordinates;
import org.example.model.Ticket;
import org.example.model.Venue;

/**
 * Класс для валидации объектов {@link Ticket} и связанных с ним данных.
 */
public class TicketValidator {

    /**
     * Проверяет валидность всех полей билета.
     *
     * @param ticket объект Ticket для валидации
     * @return true, если билет валиден; false — в противном случае
     */
    public static boolean validateTicket(Ticket ticket) {
        return validateName(ticket.getName())
                && validateCoordinates(ticket.getCoordinates())
                && validatePrice(ticket.getPrice())
                && validateDiscount(ticket.getDiscount())
                && validateVenue(ticket.getVenue());
    }

    /**
     * Проверяет корректность имени билета.
     *
     * @param name имя билета
     * @return true, если имя не null и не пустое
     */
    public static boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Проверяет координаты. X может быть null, но Y должен быть задан.
     *
     * @param coordinates объект Coordinates
     * @return true, если координаты не null и Y задан
     */
    public static boolean validateCoordinates(Coordinates coordinates) {
        return coordinates != null && coordinates.getY() != null;
    }

    /**
     * Проверяет цену билета.
     *
     * @param price цена
     * @return true, если цена не null и больше 0
     */
    public static boolean validatePrice(Integer price) {
        return price != null && price > 0;
    }

    /**
     * Проверяет скидку.
     *
     * @param discount значение скидки
     * @return true, если скидка null или в пределах от 0 (не включая) до 100 (включительно)
     */
    public static boolean validateDiscount(Double discount) {
        return discount == null || (discount > 0 && discount <= 100);
    }

    /**
     * Проверяет объект Venue.
     *
     * @param venue объект Venue
     * @return true, если имя задано, вместимость > 0, тип не null
     */
    public static boolean validateVenue(Venue venue) {
        return venue != null
                && venue.getName() != null && !venue.getName().trim().isEmpty()
                && venue.getCapacity() > 0
                && venue.getType() != null;
    }
}
