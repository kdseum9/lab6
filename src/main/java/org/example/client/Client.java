package org.example.client;

import org.example.model.Ticket;
import org.example.model.generator.TicketInput;
import org.example.share.Request;
import org.example.share.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            SocketChannel socket = null;
            try {
                // Подключение к серверу
                InetSocketAddress address = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
                socket = SocketChannel.open();
                socket.connect(address);
                System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

                // Считывание команды пользователя
                System.out.print("Enter command: ");
                String command = scanner.nextLine();

                // Если команда exit, завершаем работу клиента
                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                Request request = createRequest(scanner, command);

                if (request != null) {
                    // Подготовка и отправка запроса на сервер
                    sendRequest(socket, request);

                    // Получение ответа от сервера
                    Response response = receiveResponse(socket);
                    System.out.println("Server response: " + response.getMessage());
                    if (response.getData() != null) {
                        System.out.println("Server data: " + response.getData());
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                if (socket != null && socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.err.println("Error closing socket: " + e.getMessage());
                    }
                }
            }
        }
        scanner.close();
    }

    private static Request createRequest(Scanner scanner, String command) {
        Request request = null;
        switch (command.toLowerCase()) {
            case "add":
            case "add_if_max":
                Ticket addTicket = TicketInput.generateTicket();
                request = new Request(command, new String[0], addTicket);
                break;
            case "update":
                System.out.print("Enter ID of the ticket to update: ");
                String idStr = scanner.nextLine();
                try {
                    long id = Long.parseLong(idStr);
                    Ticket updatedTicket = TicketInput.generateTicket(); // Запросить новые данные
                    request = new Request(command, new String[]{String.valueOf(id)}, updatedTicket);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ID format.");
                }
                break;
            case "remove_by_id":
                System.out.print("Enter ID of the ticket to remove: ");
                String removeIdStr = scanner.nextLine();
                request = new Request(command, new String[]{removeIdStr}, null);
                break;
            case "remove_greater":
            case "remove_lower":
                Ticket compareTicket = TicketInput.generateTicket();
                request = new Request(command, new String[0], compareTicket);
                break;
            case "filter_starts_with":
                System.out.print("Enter name prefix to filter: ");
                String prefix = scanner.nextLine();
                request = new Request(command, new String[]{prefix}, null);
                break;
            case "execute_script":
                System.out.print("Enter script file path: ");
                String filePath = scanner.nextLine();
                request = new Request(command, new String[]{filePath}, null);
                break;
            case "info":
            case "show":
            case "save":
            case "print_descending":
            case "help":
            case "clear":
            case "print_field_descending_discount":
            case "exit": // Exit обрабатывается отдельно в main
                request = new Request(command, new String[0], null);
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
        return request;
    }

    private static void sendRequest(SocketChannel socket, Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(request);

        objectOutputStream.flush();
        objectOutputStream.close();

        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        socket.write(buffer);
    }


    private static Response receiveResponse(SocketChannel socket) throws IOException, ClassNotFoundException {
        // Создаем буфер для приема длины (4 байта = int)
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        while (lengthBuffer.hasRemaining()) {
            socket.read(lengthBuffer);
        }
        lengthBuffer.flip();
        int length = lengthBuffer.getInt();

        // Читаем сам объект Response
        ByteBuffer dataBuffer = ByteBuffer.allocate(length);
        while (dataBuffer.hasRemaining()) {
            socket.read(dataBuffer);
        }
        dataBuffer.flip();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Response) objectInputStream.readObject();
    }

}