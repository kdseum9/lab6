package org.example.manager;

import org.example.model.Ticket;
import org.example.util.XmlManipulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionManager {
    private final LocalDateTime timeOfInitial = LocalDateTime.now();
    private final LinkedHashSet<Ticket> collection = new LinkedHashSet<>();
    private final XmlManipulator xmlManipulator;
    private static final Logger logger = LoggerFactory.getLogger(CollectionManager.class);
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public CollectionManager(String xmlFilePath) {
        xmlManipulator = new XmlManipulator(xmlFilePath, collection);
        xmlManipulator.read();
    }

    public void add(Ticket ticket) {
        writeLock.lock();
        try {
            collection.add(ticket);
            logger.info("Added ticket: {}", ticket);
        } finally {
            writeLock.unlock();
        }
    }

    public void update(Ticket updatedTicket) {
        writeLock.lock();
        try {
            collection.removeIf(ticket -> ticket.getId() == updatedTicket.getId());
            collection.add(updatedTicket);
            logger.info("Updated ticket: {}", updatedTicket);
        } finally {
            writeLock.unlock();
        }
    }

    public void delete(Ticket ticketToDelete) {
        writeLock.lock();
        try {
            boolean removed = collection.remove(ticketToDelete);
            if (removed) {
                logger.info("Deleted ticket: {}", ticketToDelete);
            } else {
                logger.warn("Attempted to delete non-existent ticket: {}", ticketToDelete);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void remove_lower(Ticket compareTicket) {
        writeLock.lock();
        try {
            int removedCount = 0;
            Iterator<Ticket> iterator = collection.iterator();
            while (iterator.hasNext()) {
                Ticket currentTicket = iterator.next();
                if (compareTicket.compareTo(currentTicket) > 0) {
                    iterator.remove();
                    removedCount++;
                    logger.info("Removed ticket: {}", currentTicket);
                }
            }
            logger.info("Total tickets removed (lower than {}): {}", compareTicket, removedCount);
        } finally {
            writeLock.unlock();
        }
    }

    public void remove_greater(Ticket compareTicket) {
        writeLock.lock();
        try {
            int removedCount = 0;
            Iterator<Ticket> iterator = collection.iterator();
            while (iterator.hasNext()) {
                Ticket currentTicket = iterator.next();
                if (compareTicket.compareTo(currentTicket) < 0) {
                    iterator.remove();
                    removedCount++;
                    logger.info("Removed ticket: {}", currentTicket);
                }
            }
            logger.info("Total tickets removed (greater than {}): {}", compareTicket, removedCount);
        } finally {
            writeLock.unlock();
        }
    }

    public LocalDateTime getTimeOfInitial() {
        readLock.lock();
        try {
            return timeOfInitial;
        } finally {
            readLock.unlock();
        }
    }

    public LinkedHashSet<Ticket> getCollection() {
        readLock.lock();
        try {
            return new LinkedHashSet<>(collection);
        } finally {
            readLock.unlock();
        }
    }

    public Ticket getById(long id) {
        readLock.lock();
        try {
            return collection.stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElse(null);
        } finally {
            readLock.unlock();
        }
    }

    public void setCollection(LinkedHashSet<Ticket> newCollection) {
        writeLock.lock();
        try {
            collection.clear();
            collection.addAll(newCollection);
            logger.info("Collection replaced with new set (size: {})", collection.size());
        } finally {
            writeLock.unlock();
        }
    }

    public XmlManipulator getXmlManipulator() {
        readLock.lock();
        try {
            return xmlManipulator;
        } finally {
            readLock.unlock();
        }
    }

    public boolean add_if_max(Ticket ticket) {
        writeLock.lock();
        try {
            if (collection.isEmpty()) {
                collection.add(ticket);
                logger.info("Added ticket (collection was empty): {}", ticket);
                return true;
            }

            Ticket maxTicket = collection.stream()
                    .max(Ticket::compareTo)
                    .orElse(null);

            if (maxTicket != null && ticket.compareTo(maxTicket) > 0) {
                collection.add(ticket);
                logger.info("Added ticket (greater than max): {}", ticket);
                return true;
            } else {
                logger.info("Not added ticket (not greater than max): {}", ticket);
                return false;
            }
        } finally {
            writeLock.unlock();
        }
    }
}