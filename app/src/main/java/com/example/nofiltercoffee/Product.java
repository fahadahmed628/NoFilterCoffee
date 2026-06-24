package com.example.nofiltercoffee;

import java.io.Serializable;

public class Product implements Serializable {
    public String id, name, description, category, badge;
    public int price;
    public int originalPrice;      // for deals: original price before discount
    public int discountPercent;    // e.g. 20 means 20% off

    // Constructor for normal products
    public Product(String id, String name, String description, String category, String badge, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.badge = badge;
        this.price = price;
        this.originalPrice = price;      // no discount
        this.discountPercent = 0;
    }

    // Constructor for deal products with original price and discount percent
    public Product(String id, String name, String description, String category, String badge, int price, int originalPrice, int discountPercent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.badge = badge;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
    }

    public boolean isDeal() {
        return originalPrice > price;
    }
}