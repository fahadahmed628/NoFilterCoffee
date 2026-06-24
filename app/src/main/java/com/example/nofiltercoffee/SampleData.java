package com.example.nofiltercoffee;

import java.util.ArrayList;

public class SampleData {
    public static ArrayList<String> categories() {
        ArrayList<String> l = new ArrayList<>();
        String[] a = {"All", "Deals", "Hot Coffee", "Iced Coffee", "Matcha", "Frappes", "Bakery", "Desserts", "Sandwiches"};
        for (String s : a) l.add(s);
        return l;
    }

    public static ArrayList<Product> products() {
        ArrayList<Product> l = new ArrayList<>();

        // ---- DEALS ----
        // Combo 1: Latte + Croissant – original 650+650=1300, deal price 999
        l.add(new Product("d1", "Combo #1: Latte + Croissant",
                "Classic Hot Latte paired with a rich Chocolate Croissant.",
                "Deals", "Deal", 999, 1300, 23));

        // Combo 2: Mocha Frappe + Brownie – original 950+550=1500, deal price 1199
        l.add(new Product("d2", "Combo #2: Mocha Frappe + Brownie",
                "Creamy Mocha Frappe with a dense chocolate Brownie.",
                "Deals", "Deal", 1199, 1500, 20));

        // Combo 3: Iced Matcha + Chicken Pesto Sandwich – original 750+960=1710, deal price 1399
        l.add(new Product("d3", "Combo #3: Iced Matcha + Chicken Pesto",
                "Refreshing Iced Matcha with a Chicken Pesto Sandwich.",
                "Deals", "Deal", 1399, 1710, 18));

        // ---- Regular products (unchanged) ----
        l.add(new Product("p1", "Cappuccino", "Classic espresso with steamed milk and foam.", "Hot Coffee", "Classic", 650));
        l.add(new Product("p2", "Hot Latte", "Smooth espresso with warm milk.", "Hot Coffee", "Popular", 650));
        l.add(new Product("p3", "Americano", "Bold espresso with hot water.", "Hot Coffee", "Strong", 550));
        l.add(new Product("p4", "Spanish Iced Latte", "Smooth espresso with milk and sweet Spanish cream.", "Iced Coffee", "Best Seller", 695));
        l.add(new Product("p5", "Tiramisu Iced Latte", "Creamy iced latte with dessert-like tiramisu notes.", "Iced Coffee", "Signature", 730));
        l.add(new Product("p6", "Iced Americano", "Cold, bold and clean coffee.", "Iced Coffee", "Fresh", 590));
        l.add(new Product("p7", "Mango Matcha", "Mango meets matcha in a summer No Filter special.", "Matcha", "New", 850));
        l.add(new Product("p8", "Iced Matcha", "Earthy matcha with chilled milk.", "Matcha", "Matcha", 750));
        l.add(new Product("p9", "Pistachio Frappe", "Cold, creamy pistachio blend for a premium sip.", "Frappes", "Premium", 1490));
        l.add(new Product("p10", "Mocha Frappe", "Chocolate coffee frappe with rich creaminess.", "Frappes", "Cold", 950));
        l.add(new Product("p11", "Chocolate Croissant", "A No Filter special treat with rich chocolate filling.", "Bakery", "Bakery", 650));
        l.add(new Product("p12", "Nutella Cinnamon Roll", "Soft roll with Nutella and cinnamon sweetness.", "Bakery", "Sweet", 720));
        l.add(new Product("p13", "Brownie", "Dense chocolate brownie for a sweet coffee break.", "Desserts", "Sweet", 550));
        l.add(new Product("p14", "Tiramisu Cup", "Coffee-soaked creamy dessert cup.", "Desserts", "Dessert", 690));
        l.add(new Product("p15", "Chicken Pesto Sandwich", "Fresh sandwich with chicken, pesto and café-style bread.", "Sandwiches", "Popular", 960));
        l.add(new Product("p16", "Chicken Cheese Wrap", "Warm wrap with chicken, cheese and sauce.", "Sandwiches", "Filling", 860));

        return l;
    }
}