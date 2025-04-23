package org.example.model;


import org.example.manager.CoordinatesValidator;
import org.example.manager.TicketValidator;
import org.example.manager.VenueValidator;
import org.example.model.enums.TicketType;
import org.example.model.enums.VenueType;
import org.example.model.generator.IdGenerator;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Ticket implements Comparable<Ticket> {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer price; //Значение поля должно быть больше 0
    private Double discount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100
    private TicketType type; //Поле не может быть null
    private Venue venue; //Поле может быть null

    public Ticket() {
        this.id = IdGenerator.generateId();
        this.name = null;
        this.coordinates = null;
        this.creationDate = ZonedDateTime.now();
        this.price = null;
        this.discount = null;
        this.type = null;
        this.venue = null;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Ticket(String id, String name, String X, String Y, String creationDate, String price, String discount, String type, String venueId, String venueName, String capacity, String venueType) throws Exception {
        TicketValidator.idIsOK(id);
        TicketValidator.inputIsNotEmpty(name, "NAME");
        CoordinatesValidator.coordinateXIsOk(X);
        CoordinatesValidator.coordinateYIsOk(Y);
        TicketValidator.inputIsNotEmpty(creationDate, "DATE");
        TicketValidator.priceIsOk(price);
        TicketValidator.discountIsOk(discount);
        TicketValidator.typeIsOk(type);
        VenueValidator.idIsOk(venueId);
        VenueValidator.nameIsOk(venueName);
        VenueValidator.capacityIsOk(capacity);
        VenueValidator.typeIsOk(venueType);

        this.id = Long.parseLong(id);
        this.name = name;
        this.coordinates = new Coordinates(Float.parseFloat(X), Long.parseLong(Y));
        this.creationDate = ZonedDateTime.parse(creationDate);
        this.price = Integer.parseInt(price);
        this.discount = Double.parseDouble(discount);
        this.type = TicketType.valueOf(type);
        this.venue = new Venue(Long.parseLong(id), venueName, Integer.parseInt(capacity), VenueType.valueOf(type));
    }

    public String toXML() {
        String xmlFormat = "<ticket>" +
                "<id>" + id + "</id>" +
                "<name>" + name + "</name>" +
                "<creationDate>" + creationDate + "</creationDate>" +
                "<price>" + price + "</price>" +
                coordinates.toXML();
        if (venue != null) {
            xmlFormat += "<venueId>" + venue.getId() + "</venueId>" +
                    "<venueName>" + venue.getName() + "</venueName>" +
                    "<capacity>" + venue.getCapacity() + "</capacity>" +
                    "<venueType>" + venue.getType() + "</venueType>";
        }
        xmlFormat += "</ticket>";
        return xmlFormat;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", discount=" + discount +
                ", type=" + type +
                ", venue=" + venue +
                '}';
    }

    @Override
    public int compareTo(Ticket other) {
        if (this.price == null && other.price == null) return 0;
        if (this.price == null) return -1;
        if (other.price == null) return 1;
        return Integer.compare(this.price, other.price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && Objects.equals(name, ticket.name) && Objects.equals(coordinates, ticket.coordinates) && Objects.equals(creationDate, ticket.creationDate) && Objects.equals(price, ticket.price) && Objects.equals(discount, ticket.discount) && type == ticket.type && Objects.equals(venue, ticket.venue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, discount, type, venue);
    }

}





