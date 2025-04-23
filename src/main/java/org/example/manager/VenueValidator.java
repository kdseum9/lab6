package org.example.manager;

import org.example.exceptions.*;
import org.example.model.enums.TicketType;
import org.example.model.enums.VenueType;


public class VenueValidator {

    public static void idIsOk(String arg) throws WrongArgumentException {
        long id;
        try {
            id = Long.parseLong(arg);
            if (id <= 0) {
                throw new WrongArgumentException("ID места должен быть больше 0");
            }


        } catch (NumberFormatException e) {
            throw new WrongArgumentException("Некорректный формат ID");
        }
    }

    public static void nameIsOk(String venueName) throws WrongArgumentException {
        if (venueName == null || venueName.trim().isEmpty()) {
            throw new WrongArgumentException("Название места проведения не может быть пустым");
        }
    }

    public static void capacityIsOk(String arg) throws WrongArgumentException {
        int capacity;
        try {
            capacity = Integer.parseInt(arg);
            if (capacity <= 0) {
                throw new WrongArgumentException("Вместимость должна быть больше 0");
            }
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("Некорректный формат capacity");
        }
    }

    public static void typeIsOk(String arg) throws WrongArgumentException {
        try {
            VenueType.valueOf(arg);
        } catch (Exception e) {
            throw new WrongArgumentException("Некорректный формат VenueType.");
        }
    }
}