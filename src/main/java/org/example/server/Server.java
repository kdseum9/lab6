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

/**
 * Главный серверный класс, реализующий TCP-сервер с использованием NIO (non-blocking I/O).
 * <p>Сервер слушает указанный порт, обрабатывает входящие подключения и команды от клиентов,
 * используя {@link CommandManager} и {@link CollectionManager}.</p>
 * <p>Ответы сериализуются и отправляются обратно клиентам.</p>
 *
 * <p>Особенности:</p>
 * <ul>
 *     <li>Асинхронная работа с помощью {@link Selector}</li>
 *     <li>Обработка сериализованных {@link Request} и {@link Response}</li>
 *     <li>Сохранение коллекции при завершении программы</li>
 * </ul>
 *
 * @author —
 * @version 1.0
 */
public class Server {

    private static final String XML_FILE_PATH = "C:\\lab5\\src\\main\\java\\org\\example\\data.xml";
    private static CommandManager commandManager;
    private static CollectionManager collectionManager;

    /**
     * Точка входа в серверное приложение.
     * <p>Инициализирует коллекцию, запускает серверный канал, селектор
     * и обрабатывает события подключения, чтения и записи.</p>
     *
     * @param args Аргументы командной строки (не используются).
     * @throws IOException если возникает ошибка при работе с каналами.
     * @throws ClassNotFoundException если не удаётся десериализовать объект.
     */
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

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 9090));

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP server started on port 9090");

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
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

    /**
     * Обрабатывает новое входящее соединение от клиента.
     * <p>Настраивает клиентский канал на неблокирующий режим и регистрирует его для чтения.</p>
     *
     * @param key Ключ выбора, связанный с серверным каналом.
     * @param selector Селектор, с которым регистрируется новый канал.
     * @throws IOException если возникает ошибка при принятии соединения.
     */
    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("New connection from " + clientChannel.getRemoteAddress());
    }

    /**
     * Обрабатывает входящие данные от клиента.
     * <p>Десериализует объект {@link Request}, выполняет команду,
     * сериализует {@link Response} и регистрирует канал для отправки ответа.</p>
     *
     * @param key Ключ выбора, связанный с клиентским каналом.
     * @param selector Селектор, с которым регистрируется канал на запись.
     * @throws IOException если возникает ошибка чтения или записи в канал.
     * @throws ClassNotFoundException если не удаётся десериализовать входящий объект.
     */
    private static void handleRead(SelectionKey key, Selector selector) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                Request request = (Request) ois.readObject();

                Response response = commandManager.doCommand(request, collectionManager);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.flush();
                byte[] responseBytes = baos.toByteArray();
                ByteBuffer outBuffer = ByteBuffer.allocate(4 + responseBytes.length);
                outBuffer.putInt(responseBytes.length);
                outBuffer.put(responseBytes);
                outBuffer.flip();

                clientChannel.register(selector, SelectionKey.OP_WRITE, outBuffer);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error processing request from " + clientChannel.getRemoteAddress() + ": " + e.getMessage());
                Response errorResponse = new Response("Error processing request on server: " + e.getMessage(), null);
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(errorResponse);
                    oos.flush();
                    ByteBuffer outBuffer = ByteBuffer.wrap(baos.toByteArray());
                    clientChannel.register(selector, SelectionKey.OP_WRITE, outBuffer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (bytesRead == -1) {
            clientChannel.close();
            System.out.println("Connection closed by client.");
        }
    }

    /**
     * Отправляет ответ клиенту через соответствующий канал.
     * <p>После успешной отправки переводит канал обратно в режим чтения.</p>
     *
     * @param key Ключ выбора, содержащий буфер с данными ответа.
     * @throws IOException если возникает ошибка при записи в канал.
     */
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
