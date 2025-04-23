package org.example.util;

import org.example.model.Coordinates;
import org.example.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Timer;

public class XmlManipulator {

    private String path;
    private LinkedHashSet<Ticket> tickets;
    private static final Logger logger = LoggerFactory.getLogger(XmlManipulator.class);

    public XmlManipulator(String path, LinkedHashSet<Ticket> tickets) {
        this.path = path;
        this.tickets = tickets;
    }

    public void write() {
        File file = new File(path);
        if (!file.exists() || file.isDirectory() || !file.canWrite()) {
            logger.error("Can't write to file {}", file.getAbsolutePath());
            return;
        }

        try {
            // цепочка потоков Stream Chains
            BufferedOutputStream fileStream = new BufferedOutputStream(new FileOutputStream(path));
            OutputStreamWriter output = new OutputStreamWriter(fileStream);

            String xml = "<tickets>";
            for (Ticket ticket : tickets) {
                xml += "\n\t" + ticket.toXML();
            }
            xml += "\n</tickets>";

            output.write(xml);
            output.flush();
            fileStream.close();
            logger.info("Successfully wrote {} tickets to file {}", tickets.size(), file.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to write XML file: {}", file.getAbsolutePath(), e);
        }
    }

    public void read() {
        File file = new File(path);

        if (!file.exists() || !file.isFile() || !file.canRead()) {
            logger.error("File not found or not readable: {}", file.getAbsolutePath());
            System.exit(1);
        }

        try {
            // Получение фабрики, чтобы после получить билдер документов.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // Получили из фабрики билдер, который парсит XML, создает структуру Document в виде иерархического дерева.
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно.
            Document doc = db.parse(file);

            NodeList nl = doc.getElementsByTagName("ticket");
            if (nl.getLength() == 0) {
               logger.warn("No tickets found in XML file.");
                return;
            } else {
                logger.info("Found {} tickets in XML file.", nl.getLength());
            }

            for (int i = 0; i < nl.getLength(); i++) {
                Element objectElement = (Element) nl.item(i);
                Ticket ticket = new Ticket();
                ticket.setName(objectElement.getElementsByTagName("name").item(0).getTextContent());

                Coordinates coordinates = new Coordinates();
                coordinates.setX(Float.parseFloat(objectElement.getElementsByTagName("x").item(0).getTextContent()));
                coordinates.setY(Long.parseLong(objectElement.getElementsByTagName("y").item(0).getTextContent()));
                ticket.setCoordinates(coordinates);

                ticket.setCreationDate(ZonedDateTime.parse(objectElement.getElementsByTagName("creationDate").item(0).getTextContent()));

                tickets.add(ticket);
            }
        } catch (ParserConfigurationException | IOException e) {
            logger.error("XML parsing failed: {}", e.getMessage());
            System.exit(1);
        } catch (SAXException e){
            logger.warn("XML file is empty or invalid format.");
            System.exit(1);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
