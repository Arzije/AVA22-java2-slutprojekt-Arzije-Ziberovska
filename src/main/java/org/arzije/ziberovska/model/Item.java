package org.arzije.ziberovska.model;

import java.io.Serializable;

/**
 * Represents a generic item with an ID.
 */
public class Item implements Serializable {

    private String id = "";

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
