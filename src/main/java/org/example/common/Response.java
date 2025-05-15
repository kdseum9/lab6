package org.example.common;

import java.io.Serializable;

public class Response implements Serializable {
    private String message;   // Сообщение результата команды
    private Object data;      // Дополнительные данные (например, коллекция, объект)

    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

