package org.arzije.ziberovska.model;

import java.io.Serializable;

public class Item implements Serializable {

    String id = "";

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                '}';
    }
}
