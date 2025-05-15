package org.example.server;

import org.example.server.manager.CollectionManager;
import org.example.server.manager.CommandManager;
import org.example.common.Request;
import org.example.common.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

    private static final String XML_FILE_PATH = "C:\\lab5\\src\\main\\java\\org\\example\\data.xml";
    private static CommandManager commandManager;
    private static CollectionManager collectionManager;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            try {
                collectionManager.getXmlManipulator().write();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }));

        collectionManager = new CollectionManager(XML_FILE_PATH);
        commandManager = new CommandManager();

        // Создаем серверный канал
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Привязываем канал к порту
        serverChannel.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 9090));

        // Создаем селектор
        Selector selector = Selector.open();
        // Регистрируем серверный канал для приема подключений
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP server started on port 9090");

        // Основной цикл обработки событий
        while (true) {
            selector.select(); // Количество ключей, чьи каналы готовы к операции
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // Обработка событий
                if (key.isAcceptable()) {
                    handleAccept(key, selector);
                } else if (key.isReadable()) {
                    handleRead(key, selector);
                } else if (key.isValid() && key.isWritable()) {
                    sendAnswer(key);
                }
                keyIterator.remove();
            }
        }
    }

    // Обработка события ACCEPT
    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("New connection from " + clientChannel.getRemoteAddress());
    }

    // Обработка события READ
    private static void handleRead(SelectionKey key, Selector selector) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                Request request = (Request) ois.readObject();

                // Выполнение команды
                Response response = commandManager.doCommand(request, collectionManager);

                // Подготовка ответа для отправки
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.flush();
                byte[] responseBytes = baos.toByteArray();
                ByteBuffer outBuffer = ByteBuffer.allocate(4 + responseBytes.length); // Выделяем буфер под длину + данные
                outBuffer.putInt(responseBytes.length); // Записываем длину
                outBuffer.put(responseBytes);           // Записываем данные
                outBuffer.flip();

                // Регистрируем канал на запись, прикрепляя буфер с ответом
                clientChannel.register(selector, SelectionKey.OP_WRITE, outBuffer);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error processing request from " + clientChannel.getRemoteAddress() + ": " + e.getMessage());
                // Отправляем сообщение об ошибке клиенту
                Response errorResponse = new Response("Error processing request on server: " + e.getMessage(), null);
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(errorResponse);
                    oos.flush();
                    ByteBuffer outBuffer = ByteBuffer.wrap(baos.toByteArray());
                    clientChannel.register(selector, SelectionKey.OP_WRITE, outBuffer);
                } catch (IOException ex) {
                    // Обработка ошибки при отправке сообщения об ошибке
                    ex.printStackTrace();
                }
            }
        } else if (bytesRead == -1) {
            clientChannel.close();
            System.out.println("Connection closed by client.");
        }
    }

    // Отправка ответа
    private static void sendAnswer(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        clientChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            clientChannel.register(key.selector(), SelectionKey.OP_READ);
            System.out.println("Response sent to client.");
        }
    }
}