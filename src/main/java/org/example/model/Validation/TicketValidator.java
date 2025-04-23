package org.example.model.Validation;


import org.example.model.Coordinates;
import org.example.model.Ticket;
import org.example.model.Venue;

public class TicketValidator {

    public static boolean validateTicket(Ticket ticket) {
        return validateName(ticket.getName())
                && validateCoordinates(ticket.getCoordinates())
                && validatePrice(ticket.getPrice())
                && validateDiscount(ticket.getDiscount())
                && validateVenue(ticket.getVenue());
    }

    public static boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean validateCoordinates(Coordinates coordinates) {
        return coordinates != null && coordinates.getY() != null;
    }

    public static boolean validatePrice(Integer price) {
        return price != null && price > 0;
    }

    public static boolean validateDiscount(Double discount) {
        return discount == null || (discount > 0 && discount <= 100);
    }

    public static boolean validateVenue(Venue venue) {
        return venue != null
                && venue.getName() != null && !venue.getName().trim().isEmpty()
                && venue.getCapacity() > 0
                && venue.getType() != null;
    }
}

