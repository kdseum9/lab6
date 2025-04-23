package org.example.model.generator;

import org.example.model.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CoordinatesGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CoordinatesGenerator.class);

    public static Coordinates generateCoordinates() {
        logger.info("Generating coordinates...");
        Coordinates coordinates = new Coordinates();

        Float x = generateX();
        if (x != null) {
            coordinates.setX(x);
            logger.info("X coordinate set: {}", x);
        } else {
            logger.info("X coordinate not provided (null)");
        }

        Long y = generateY();
        coordinates.setY(y);
        logger.info("Y coordinate set: {}", y);

        return coordinates;
    }

    private static Float generateX() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter X coordinate: ");
            try {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    logger.info("Empty input)");
                    return null;
                }
                float x = Float.parseFloat(line);
                if (x <= 300) {
                    return x;
                }
                logger.warn("X coordinate must be <= 300, got: {}", x);
                System.out.println("X must be less than or equal to 300");
            } catch (Exception e) {
                logger.warn("Invalid input for X coordinate", e);
                System.out.println("Invalid coordinates entered");
            }
        }
    }

    private static Long generateY() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter Y coordinate: ");
            try {
                String line = scanner.nextLine();
                Long y = Long.parseLong(line);
                return y;
            } catch (Exception e) {
                logger.warn("Invalid input for Y coordinate", e);
                System.out.println("Invalid coordinates entered");
            }
        }
    }
}
