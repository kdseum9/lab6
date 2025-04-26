package org.example.util;

import org.example.manager.CollectionManager;
import org.example.manager.TicketValidator;
import org.example.model.Coordinates;
import org.example.model.Ticket;
import org.example.model.Venue;
import org.example.model.enums.TicketType;
import org.example.model.enums.VenueType;
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
import java.util.LinkedHashSet;

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
            logger.error("Can't write to file: {}", file.getAbsolutePath());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            StringBuilder xml = new StringBuilder();
            xml.append("<tickets>\n");

            for (Ticket ticket : tickets) {
                xml.append("\t").append(ticket.toXML()).append("\n");
            }

            xml.append("</tickets>");
            writer.write(xml.toString());

            logger.info("Successfully wrote {} tickets to file {}", tickets.size(), file.getAbsolutePath());

        } catch (IOException e) {
            logger.error("Failed to write to XML file: {}", file.getAbsolutePath(), e);
        }
    }

    public void read() {
        File file = new File(path);

        if (!file.exists() || !file.isFile() || !file.canRead()) {
            logger.error("File not found or not readable: {}", file.getAbsolutePath());
            System.exit(1);
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
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

                try {
                    ticket.setName(getTextContent(objectElement, "name"));

                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(Float.parseFloat(getTextContent(objectElement, "x")));
                    coordinates.setY(Long.parseLong(getTextContent(objectElement, "y")));
                    ticket.setCoordinates(coordinates);

                    ticket.setCreationDate(ZonedDateTime.parse(getTextContent(objectElement, "creationDate")));

                    String priceStr = getTextContent(objectElement, "price");
                    String discountStr = getTextContent(objectElement, "discount");
                    String typeStr = getTextContent(objectElement, "type");

                    if (!priceStr.isEmpty()) ticket.setPrice(Integer.parseInt(priceStr));
                    if (!discountStr.isEmpty()) ticket.setDiscount(Double.parseDouble(discountStr));
                    if (!typeStr.isEmpty()) ticket.setType(TicketType.valueOf(typeStr));

                    String venueIdStr = getTextContent(objectElement, "venueId");
                    String venueName = getTextContent(objectElement, "venueName");
                    String capacityStr = getTextContent(objectElement, "capacity");
                    String venueTypeStr = getTextContent(objectElement, "venueType");

                    if (!venueIdStr.isEmpty() && !venueName.isEmpty() && !capacityStr.isEmpty()) {
                        VenueType venueType = parseVenueType(venueTypeStr);
                        Venue venue = new Venue(
                                Long.parseLong(venueIdStr),
                                venueName,
                                Integer.parseInt(capacityStr),
                                venueType
                        );
                        ticket.setVenue(venue);
                    }

                    if (TicketValidator.validateTicket(ticket)) {
                        tickets.add(ticket);
                        logger.info("Valid ticket loaded: {}", ticket.getName());
                    } else {
                        logger.warn("Invalid ticket. Skipping: {}", ticket.getName());
                    }

                } catch (Exception e) {
                    logger.warn("Invalid ticket at index {} skipped: {}", i, e.getMessage());
                }
            }

        } catch (ParserConfigurationException | IOException e) {
            logger.error("XML parsing failed: {}", e.getMessage());
            System.exit(1);
        } catch (SAXException e) {
            logger.warn("XML file is empty or has invalid format.");
            System.exit(1);
        }
    }

    private String getTextContent(Element element, String tagName) {
        try {
            return element.getElementsByTagName(tagName).item(0).getTextContent();
        } catch (NullPointerException e) {
            logger.info("Missing element: {}", tagName);
            return "";
        }
    }

    public static VenueType parseVenueType(String typeStr) {
        if (typeStr == null || typeStr.trim().isEmpty() || typeStr.trim().equalsIgnoreCase("null")) {
            return null;
        }
        try {
            return VenueType.valueOf(typeStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid VenueType in XML: {}", typeStr);
            return null;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
