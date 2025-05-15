package org.example.client;

import org.example.common.model.Ticket;
import org.example.common.Request;
import org.example.common.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Клиентское приложение для взаимодействия с сервером через TCP-соединение с использованием {@link SocketChannel}.
 * <p>Позволяет отправлять команды, сериализованные в объект {@link Request}, и получать ответ {@link Response}.
 * Поддерживает интерактивный режим и выполнение скриптов из файлов.</p>
 */
public class Client {

    /** Адрес сервера. */
    private static final String SERVER_ADDRESS = "localhost";

    /** Порт, на котором работает сервер. */
    private static final int SERVER_PORT = 9090;

    /**
     * Точка входа клиента. Устанавливает соединение с сервером, обрабатывает пользовательские команды.
     *
     * @param args аргументы командной строки (не используются)
     * @throws InterruptedException если поток сна прерывается между попытками соединения
     */
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ScriptExecutor scriptExecutor = new ScriptExecutor();

        while (true) {
            SocketChannel socket = null;
            try {
                socket = SocketChannel.open();
                socket.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
                System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

                while (true) {
                    try {
                        System.out.print("Enter command: ");
                        String command = scanner.nextLine();

                        if (command.equalsIgnoreCase("exit")) {
                            System.out.println("Exiting...");
                            break;
                        }

                        Request request = createRequest(scanner, socket, command, scriptExecutor);

                        if (request != null) {
                            sendRequest(socket, request);
                            Response response = receiveResponse(socket);
                            System.out.println("Server response: " + response.getMessage());

                            if (response.getData() != null) {
                                System.out.println("Server data: " + response.getData());
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Error during command processing: " + e.getMessage());
                    }
                }

            } catch (IOException e) {
                System.err.println("Connection error: " + e.getMessage());
                Thread.sleep(1000);
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
    }

    /**
     * Создает объект {@link Request} на основе введенной команды и дополнительных данных.
     *
     * @param scanner сканер для ввода данных пользователем
     * @param socket активный сокет клиента
     * @param command строка команды
     * @param scriptExecutor исполнитель скриптов
     * @return созданный объект запроса или {@code null}, если команда обрабатывается отдельно (например, скрипт)
     */
    private static Request createRequest(Scanner scanner, SocketChannel socket, String command, ScriptExecutor scriptExecutor) {
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
                    Ticket updatedTicket = TicketInput.generateTicket();
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
                scriptExecutor.executeScript(filePath, socket);
                return null;

            default:
                request = new Request(command, new String[0], null);
                break;
        }

        return request;
    }

    /**
     * Отправляет объект {@link Request} на сервер через сокет.
     *
     * @param socket активный {@link SocketChannel}
     * @param request сериализуемый объект запроса
     * @throws IOException при ошибке отправки
     */
    public static void sendRequest(SocketChannel socket, Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        objectOutputStream.close();

        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        socket.write(buffer);
    }

    /**
     * Получает объект {@link Response} от сервера.
     *
     * @param socketChannel активный {@link SocketChannel}
     * @return десериализованный {@link Response}
     * @throws IOException при ошибке чтения
     * @throws ClassNotFoundException если класс {@link Response} не найден
     */
    public static Response receiveResponse(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        while (lengthBuffer.hasRemaining()) {
            int read = socketChannel.read(lengthBuffer);
            if (read == -1) {
                throw new IOException("Connection closed before reading response length");
            }
        }
        lengthBuffer.flip();
        int length = lengthBuffer.getInt();

        ByteBuffer dataBuffer = ByteBuffer.allocate(length);
        while (dataBuffer.hasRemaining()) {
            int read = socketChannel.read(dataBuffer);
            if (read == -1) {
                throw new IOException("Connection closed before reading full response");
            }
        }
        dataBuffer.flip();
        ByteArrayInputStream bais = new ByteArrayInputStream(dataBuffer.array());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Response) ois.readObject();
    }
}
