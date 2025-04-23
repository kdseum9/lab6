package org.example.manager;

import org.example.exceptions.*;

public class CoordinatesValidator {

    public static void coordinateXIsOk(String arg) throws WrongArgumentException {

        if (arg == null) {
            throw new WrongArgumentException("Значение X не может быть null.");
        }
        if (arg.trim().isEmpty()) {
            throw new WrongArgumentException("Заполните X.");
        }

        try {
            float x = Float.parseFloat(arg);
            if (x > 300) {
                throw new WrongArgumentException("Значение X не может превышать 300.");
            }
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("Некорректный формат X. Ожидается число.");
        }
    }

    public static void coordinateYIsOk(String arg) throws WrongArgumentException {
        if (arg == null) {
            throw new WrongArgumentException("Значение Y не может быть null.");
        }
        if (arg.trim().isEmpty()) {
            throw new WrongArgumentException("Заполните Y.");
        }

        try {
            Long.parseLong(arg);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("Некорректный формат Y. Ожидается целое число.");
        }
    }
}