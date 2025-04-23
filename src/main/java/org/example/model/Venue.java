package org.example.model;


import org.example.model.enums.VenueType;
import org.example.model.generator.IdGenerator;

public class Venue {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String venueName; //Поле не может быть null, Строка не может быть пустой
    private Integer capacity; //Поле не может быть null, Значение поля должно быть больше 0
    private VenueType type; //Поле может быть null

    public Venue() {
        this.id = IdGenerator.generateId();
        this.venueName = null;
        this.capacity = null;
        this.type = null;
    }

    public Venue(long id, String name, Integer capacity, VenueType type){
        this.id = id;
        this.venueName = name;
        this.capacity = capacity;
        this.type = type;
    }

    public Venue(String name, Integer capacity, VenueType type){
        this.venueName = null;
        this.capacity = null;
        this.type = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return venueName;
    }

    public void setName(String name){
        this.venueName = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public VenueType getType() {
        return type;
    }

    public void setType(VenueType type) {
        this.type = type;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", venueName='" + venueName + '\'' +
                ", capacity=" + capacity +
                ", type=" + type +
                '}';
    }
}
