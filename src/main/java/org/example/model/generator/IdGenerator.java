package org.example.model.generator;

import java.util.ArrayList;

public class IdGenerator {
    public static long nextId = 1; // Начальное значение ID
    public static ArrayList<Long> idList = new ArrayList<>();

    public static long generateId() {
        long newId = nextId++;
        idList.add(newId);
        return newId;
    }

    public static void add(long id) {
        idList.add(id);
    }

    public static void remove(long id) {
        idList.remove(id);
    }
}

