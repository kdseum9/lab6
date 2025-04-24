package org.example.command;

import org.example.manager.CollectionManager;
import org.example.model.Ticket;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>Команда для вывода значений поля <code>discount</code> всех объектов {@link Ticket} в порядке убывания.</p>
 *
 * <p>Если скидки равны нулю или отсутствуют, они игнорируются.</p>
 * <p>Вывод происходит в консоль в формате: <code>- 10.50%</code></p>
 *
 * @author kdseum9
 * @version 1.0
 */
public class PrintFieldDescendingDiscountCommand extends AbstractCommand {

    /**
     * <p>Выполняет команду <code>print_field_descending_discount</code>.</p>
     *
     * @param args аргументы команды (не используются)
     * @param collectionManager менеджер, управляющий коллекцией
     * @return <code>null</code> — результат выводится в консоль
     */
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
                .toList(); // Java 16+

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
