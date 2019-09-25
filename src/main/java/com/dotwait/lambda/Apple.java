package com.dotwait.lambda;

public class Apple {
    private int inventory;
    private int weight;

    public Apple(int inventory, int weight) {
        this.inventory = inventory;
        this.weight = weight;
    }

    public int getColor() {
        return inventory;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "inventory=" + inventory +
                ", weight=" + weight +
                '}';
    }
}
