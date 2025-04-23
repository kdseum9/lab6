package org.example.model.generator;

import org.example.model.Venue;
import org.example.model.enums.VenueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class VenueGenerator {
    private static final Logger logger = LoggerFactory.getLogger(VenueGenerator.class);

    public static Venue generateVenue() {
        logger.info("Generating venue...");
        Scanner scanner = new Scanner(System.in);
        Venue venue = new Venue();

        venue.setId(IdGenerator.generateId());
        logger.info("Venue ID generated: {}", venue.getId());

        while (true) {
            System.out.print("Enter venue name: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                venue.setVenueName(name);
                logger.info("Venue name set: {}", name);
                break;
            }
            logger.warn("Empty venue name entered");
            System.out.println("Venue name cannot be empty!");
        }

        while (true) {
            System.out.print("Enter venue capacity (> 0): ");
            try {
                int capacity = Integer.parseInt(scanner.nextLine());
                if (capacity > 0) {
                    venue.setCapacity(capacity);
                    logger.info("Venue capacity set: {}", capacity);
                    break;
                } else {
                    logger.warn("Invalid venue capacity (<= 0)");
                    System.out.println("Capacity must be greater than 0!");
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid number format for capacity", e);
                System.out.println("Invalid number format!");
            }
        }

        System.out.print("Enter venue type (or leave empty): ");
        String typeInput = scanner.nextLine().trim();
        if (!typeInput.isEmpty()) {
            try {
                VenueType type = VenueType.valueOf(typeInput.toUpperCase());
                venue.setType(type);
                logger.info("Venue type set: {}", type);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid venue type entered: {}", typeInput);
                System.out.println("Invalid venue type, setting as null.");
                venue.setType(null);
            }
        } else {
            logger.info("No venue type provided");
            venue.setType(null);
        }

        logger.info("Venue generated successfully");
        return venue;
    }
}

