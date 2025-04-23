package org.example.manager;
import org.example.model.*;
import org.example.exceptions.*;
import org.example.model.enums.TicketType;
import org.example.model.enums.VenueType;

public class TicketValidator {

    public static void idIsOK(String arg) throws  WrongArgumentException {
        long id;

        try {
            id = Long.parseLong(arg);

        } catch (Exception e) {
            throw new WrongArgumentException("Некорректный формат ID");
        }

    }

    public static void inputIsNotEmpty(String arg, String model) throws WrongArgumentException {
        if (arg.isEmpty() || arg.trim().isEmpty()) {
            throw new WrongArgumentException("Заполните " + model);
        }
    }


    public static void priceIsOk(String arg) throws WrongArgumentException {
        try {
            int n = Integer.parseInt(arg);
            if (n <= 0) {
                throw new WrongArgumentException("Некорректный формат price. Цена не может быть меньше нуля.");
            }
        } catch (Exception e) {
            throw new WrongArgumentException("Некорректный формат price");
        }
    }

    public static void discountIsOk(String arg) throws WrongArgumentException {
        try {
            double n = Double.parseDouble(arg);
            if (n <= 0) {
                throw new WrongArgumentException("Некорректный формат discount. Скидка не может быть меньше нуля.");
            } else if (n >= 100) {
                throw new WrongArgumentException("Некорректный формат discount. Скидка не может быть больше ста.");

            }
        } catch (Exception e) {
            throw new WrongArgumentException("Некорректный формат discount.");
        }
    }

    public static void typeIsOk(String arg) throws WrongArgumentException {
        try {
            TicketType.valueOf(arg);
        } catch (Exception e) {
            throw new WrongArgumentException("Некорректный формат TicketType.");
        }
    }

    public static void capacityIsOk(String arg) throws WrongArgumentException {
        try {
            int n = Integer.parseInt(arg);
            if (n <= 0) {
                throw new WrongArgumentException("Вместимость должна быть больше 0");
            }
        } catch (Exception e) {
            throw new WrongArgumentException("Некорректный формат capacity.");
        }
    }

    public static void venueTypeIsOk(String arg) throws WrongArgumentException {
        if (!arg.isEmpty()) {
            try {
                VenueType.valueOf(arg);
            } catch (IllegalArgumentException e) {
                throw new WrongArgumentException("Недопустимый тип места проведения");
            }
        }
    }
}