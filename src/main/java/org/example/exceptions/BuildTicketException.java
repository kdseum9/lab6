package org.example.exceptions;

public class BuildTicketException extends Exception{

    public BuildTicketException(String message){
        super(message);
        System.out.println(message);
    }
}