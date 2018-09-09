package main.Entities;

public class Entity {

    private String name;
    protected char symbol;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

}
