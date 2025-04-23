package org.example.model;

public class Coordinates {
    private float x; //Максимальное значение поля: 300
    private Long y; //Поле не может быть null

    public Coordinates(){

    }

    public Coordinates(float x, long y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public String toXML(){
        return "<coordinate><x>"+x+"</x><y>"+y+"</y></coordinate>";
    }
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
