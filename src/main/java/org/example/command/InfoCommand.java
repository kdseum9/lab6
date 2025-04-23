package org.example.command;

import org.example.manager.CollectionManager;

public class InfoCommand extends AbstractCommand{
    @Override
    public String execute(String[] arg, CollectionManager manager) {
        logger.info("Executing 'info' command");

        System.out.println("Application Info:");
        System.out.println("- Collection type: " + manager.getCollection().getClass().getSimpleName());
        System.out.println("- Collection size: " + manager.getCollection().size());
        System.out.println("- Initialization time: " + manager.getTimeOfInitial());

        return null;
    }
}
