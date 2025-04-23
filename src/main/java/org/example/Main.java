package org.example;

import org.example.util.Console;

public class Main {
    public static void main(String[] args) {

        if (args.length != 0) {
            Console console = new Console(args[0]);
            console.start();
        }
    }


}