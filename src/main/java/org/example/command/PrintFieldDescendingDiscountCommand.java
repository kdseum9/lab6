package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;


public class PrintFieldDescendingDiscountCommand extends AbstractCommand {
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        LinkedHashSet<Ticket> collection = collectionManager.getCollection();

        if (collection.isEmpty()) {
            System.out.println("The collection is empty.");
            logger.warn("Attempted to print discounts, but the collection is empty.");
            return null;
        }

        List<Double> discounts = collection.stream()
                .map(Ticket::getDiscount)
                .filter(discount -> discount != null && discount > 0)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (!discounts.isEmpty()) {
            System.out.println("Discount values (descending):");
            discounts.forEach(d -> System.out.printf("- %.2f%%\n", d));
            logger.info("Printed {} discount values in descending order.", discounts.size());
        } else {
            System.out.println("No valid discount values found in the collection.");
            logger.info("No valid discounts found to display.");
        }

        return null;
    }
}
