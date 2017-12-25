package main.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhil on 10/11/2015.
 */
public class Item {
    private int id;
    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public static List<Item> getItems() {
        List<Item> inputList = new ArrayList<>(5);
        inputList.add(new Item("Apple" , 100));
        inputList.add(new Item("Ball" , 100.5));
        inputList.add(new Item("Carrot" , 100.9));
        inputList.add(new Item("Dukes" , 120.5));
        inputList.add(2,new Item("Egg" , 140.5));
        inputList.add(new Item("Apple" , 500));
        return inputList;
    }
}
