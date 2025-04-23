package org.example.exceptions;

public class WrongArgumentException extends Exception {

    public WrongArgumentException(String argument){
        super("Уважаемый пользователь! Что-то не так с введенными данными. Пожалуйста, проверьте необходимый тип данных" + argument);
        System.out.println("Неправильный ввод данных {}" + argument);

    }
}
