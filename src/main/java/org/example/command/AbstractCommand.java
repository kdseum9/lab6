package org.example.command;

import org.example.exceptions.NoElementException;
import org.example.manager.CollectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommand {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public abstract String execute(String[] args, CollectionManager collectionManager) throws NoElementException;
}
