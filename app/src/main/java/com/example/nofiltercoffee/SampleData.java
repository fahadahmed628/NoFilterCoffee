package com.example.nofiltercoffee;

import java.util.ArrayList;

public class SampleData {

    public static ArrayList<String> categories() {
        ArrayList<String> list = new ArrayList<>();
        String[] a = {
                "All",
                "COFFEE",
                "LATTE",
                "MATCHA",
                "FRAPPE",
                "TEA & MORE",
                "DESSERT",
                "SANDWICHES & WRAPS"
        };
        for (String s : a) list.add(s);
        return list;
    }

    public static ArrayList<QuickAction> quickActions() {
        ArrayList<QuickAction> list = new ArrayList<>();
        list.add(new QuickAction("⚡", "Order ahead"));
        list.add(new QuickAction("☕", "Your usual"));
        list.add(new QuickAction("✦", "Rewards"));
        list.add(new QuickAction("🎁", "App deals"));
        return list;
    }

    public static ArrayList<Product> products() {
        ArrayList<Product> list = new ArrayList<>();

        // ==================== COFFEE ====================
        list.add(new Product("p1", "Espresso", "Rich and bold espresso shot.", "COFFEE", "Classic", 485));
        list.add(new Product("p2", "Americano", "Smooth espresso with hot water. Available hot or iced.", "COFFEE", "Popular", 650));
        list.add(new Product("p3", "Cortado", "Espresso with a small amount of warm milk.", "COFFEE", "Signature", 600));
        list.add(new Product("p4", "Cappuccino", "Classic espresso with steamed milk and foam.", "COFFEE", "Classic", 650));
        list.add(new Product("p5", "Classic Affogato", "Vanilla gelato topped with a shot of hot espresso.", "COFFEE", "Dessert", 650));
        list.add(new Product("p6", "V60", "Hand-poured single origin coffee.", "COFFEE", "Premium", 1200));

        // ==================== LATTE (Hot / Cold) ====================
        list.add(new Product("p7", "Latte", "Smooth espresso with steamed milk. Available hot or iced.", "LATTE", "Classic", 650));
        list.add(new Product("p8", "Vanilla Latte", "Smooth latte with vanilla syrup. Available hot or iced.", "LATTE", "Popular", 710));
        list.add(new Product("p9", "Spanish Latte", "Smooth espresso with milk and sweet Spanish cream.", "LATTE", "Best Seller", 695));
        list.add(new Product("p10", "Coconut Latte", "Latte with creamy coconut flavor. Available hot or iced.", "LATTE", "Signature", 719));
        list.add(new Product("p11", "Tiramisu Latte", "Creamy latte with dessert-like tiramisu notes.", "LATTE", "Signature", 730));
        list.add(new Product("p12", "Salted Caramel Latte", "Sweet and salty caramel latte. Available hot or iced.", "LATTE", "Popular", 710));
        list.add(new Product("p13", "Mocha Latte", "Chocolate meets espresso in this smooth latte.", "LATTE", "Classic", 710));
        list.add(new Product("p14", "Hazelnut Latte", "Nutty hazelnut latte. Available hot or iced.", "LATTE", "Popular", 710));
        list.add(new Product("p15", "Pistachio Latte", "Premium pistachio latte. Available hot or iced.", "LATTE", "Premium", 1249));
        list.add(new Product("p16", "Rose Latte", "Floral rose latte with a delicate finish.", "LATTE", "Signature", 750));
        list.add(new Product("p17", "Lotus Latte", "Sweet caramelized lotus latte.", "LATTE", "Popular", 690));

        // ==================== MATCHA ====================
        list.add(new Product("p18", "Matcha", "Earthy matcha with chilled or steamed milk.", "MATCHA", "Classic", 750));
        list.add(new Product("p19", "Strawberry Matcha", "Sweet strawberry meets earthy matcha.", "MATCHA", "Popular", 930));
        list.add(new Product("p20", "Coconut Matcha", "Creamy coconut matcha blend.", "MATCHA", "Signature", 950));
        list.add(new Product("p21", "Vanilla Matcha", "Vanilla-infused matcha latte.", "MATCHA", "Popular", 960));
        list.add(new Product("p22", "Pistachio Matcha", "Premium pistachio matcha.", "MATCHA", "Premium", 1200));
        list.add(new Product("p23", "Coco Cloud Matcha", "Cloudy coconut matcha delight.", "MATCHA", "Signature", 1200));
        list.add(new Product("p24", "Mango Matcha", "Mango meets matcha in a summer special.", "MATCHA", "New", 980));

        // ==================== FRAPPE ====================
        list.add(new Product("p25", "Mocha Frappe", "Chocolate coffee frappe with rich creaminess.", "FRAPPE", "Popular", 850));
        list.add(new Product("p26", "Vanilla Frappe", "Classic vanilla frappe.", "FRAPPE", "Classic", 850));
        list.add(new Product("p27", "Salted Caramel Frappe", "Sweet and salty caramel frappe.", "FRAPPE", "Popular", 850));
        list.add(new Product("p28", "Mixed Berry Frappe", "Berry bliss in a cold frappe.", "FRAPPE", "Signature", 900));
        list.add(new Product("p29", "Matcha Frappe", "Earthy matcha in a cold frappe.", "FRAPPE", "Matcha", 900));
        list.add(new Product("p30", "Tiramisu Frappe", "Dessert-inspired tiramisu frappe.", "FRAPPE", "Dessert", 900));
        list.add(new Product("p31", "Lotus Frappe", "Caramelized lotus frappe.", "FRAPPE", "Popular", 990));
        list.add(new Product("p32", "Pistachio Matcha Frappe", "Premium pistachio matcha frappe.", "FRAPPE", "Premium", 1750));

        // ==================== TEA & MORE ====================
        list.add(new Product("p33", "Peach Iced Tea", "Refreshing peach iced tea.", "TEA & MORE", "Classic", 550));
        list.add(new Product("p34", "Yuzu Iced Tea", "Citrusy yuzu iced tea.", "TEA & MORE", "Signature", 590));
        list.add(new Product("p35", "Mango Iced Tea", "Tropical mango iced tea.", "TEA & MORE", "Popular", 550));
        list.add(new Product("p36", "Belgian Hot Chocolate", "Rich Belgian hot chocolate.", "TEA & MORE", "Popular", 880));
        list.add(new Product("p37", "Nutty Shake", "Nutty, creamy milkshake.", "TEA & MORE", "Shake", 900));
        list.add(new Product("p38", "Oreo Shake", "Oreo cookie milkshake.", "TEA & MORE", "Popular", 700));
        list.add(new Product("p39", "Seasonal Smoothie", "Fresh seasonal fruit smoothie.", "TEA & MORE", "Fresh", 950));
        list.add(new Product("p40", "Chiller", "Blended fruit chiller. Blueberry, Strawberry, Green Apple, Peach, Mango, Yuzu, Raspberry.", "TEA & MORE", "Cold", 650));
        list.add(new Product("p41", "Fizz", "Sparkling fruit fizz. Blue Lagoon, Passion Fruit, Dragon Fruit, Pomegranate, Strawberry.", "TEA & MORE", "Fizzy", 660));

        // ==================== DESSERT ====================
        list.add(new Product("p42", "Chocolate Cake", "Rich chocolate cake.", "DESSERT", "Classic", 660));
        list.add(new Product("p43", "Basque Cake", "Burnt Basque cheesecake.", "DESSERT", "Signature", 790));
        list.add(new Product("p44", "Butter Croissant", "Flaky butter croissant.", "DESSERT", "Bakery", 750));
        list.add(new Product("p45", "Pistachio Croissant", "Pistachio-filled croissant.", "DESSERT", "Premium", 920));
        list.add(new Product("p46", "Almond Croissant", "Almond-filled croissant.", "DESSERT", "Bakery", 830));
        list.add(new Product("p47", "Chocolate Croissant", "Rich chocolate-filled croissant.", "DESSERT", "Bakery", 760));
        list.add(new Product("p48", "Fajita Danish", "Savory fajita danish.", "DESSERT", "Savory", 820));
        list.add(new Product("p49", "Berry Danish", "Sweet berry danish.", "DESSERT", "Sweet", 820));
        list.add(new Product("p50", "Lotus Three Milk Cake", "Lotus-inspired tres leches cake.", "DESSERT", "Signature", 920));
        list.add(new Product("p51", "Chocolate Three Milk", "Chocolate tres leches cake.", "DESSERT", "Classic", 630));
        list.add(new Product("p52", "Tiramisu Cake", "Coffee-soaked tiramisu cake.", "DESSERT", "Dessert", 1300));
        list.add(new Product("p53", "Malai Cake", "Creamy malai cake.", "DESSERT", "Classic", 1300));
        list.add(new Product("p54", "Banana Bread", "Homestyle banana bread.", "DESSERT", "Bakery", 550));
        list.add(new Product("p55", "Fudge Brownie", "Rich fudge brownie.", "DESSERT", "Classic", 530));
        list.add(new Product("p56", "Chocolate Lisbon Cake", "Lisbon chocolate cake.", "DESSERT", "Signature", 730));
        list.add(new Product("p57", "Classic Cinnamon Roll", "Classic cinnamon roll.", "DESSERT", "Bakery", 660));
        list.add(new Product("p58", "Nutella Cinnamon Roll", "Nutella-filled cinnamon roll.", "DESSERT", "Sweet", 720));
        list.add(new Product("p59", "Limited Malai Cake", "Limited edition malai cake.", "DESSERT", "Limited", 1500));

        // ==================== SANDWICHES & WRAPS ====================
        list.add(new Product("p60", "Pepperoni Melt", "Pepperoni melt sandwich.", "SANDWICHES & WRAPS", "Popular", 960));
        list.add(new Product("p61", "Tuscan Chicken", "Tuscan chicken sandwich.", "SANDWICHES & WRAPS", "Classic", 960));
        list.add(new Product("p62", "Chicken Garlic Mayo", "Chicken with garlic mayo sandwich.", "SANDWICHES & WRAPS", "Popular", 960));
        list.add(new Product("p63", "Pesto Chicken", "Pesto chicken sandwich.", "SANDWICHES & WRAPS", "Signature", 960));
        list.add(new Product("p64", "Hunter Beef", "Hunter beef sandwich.", "SANDWICHES & WRAPS", "Premium", 970));
        list.add(new Product("p65", "Roast Chicken", "Roast chicken sandwich.", "SANDWICHES & WRAPS", "Classic", 960));
        list.add(new Product("p66", "Pulled Beef", "Pulled beef sandwich.", "SANDWICHES & WRAPS", "Premium", 960));
        list.add(new Product("p67", "Chicken Cheese Wrap", "Chicken and cheese wrap.", "SANDWICHES & WRAPS", "Classic", 860));
        list.add(new Product("p68", "Chicken Jalapeno Wrap", "Spicy chicken jalapeno wrap.", "SANDWICHES & WRAPS", "Spicy", 860));

        // ==================== ADD ONS (Special category) ====================
        list.add(new Product("p69", "Lactose Free Milk", "Lactose free milk add-on.", "ADD ONS", "Milk", 50));
        list.add(new Product("p70", "Low Fat Milk", "Low fat milk add-on.", "ADD ONS", "Milk", 50));
        list.add(new Product("p71", "Extra Shot", "Extra espresso shot.", "ADD ONS", "Coffee", 250));
        list.add(new Product("p72", "Water", "Bottled water.", "ADD ONS", "Drink", 100));
        list.add(new Product("p73", "Cold Foam", "Cold foam topping.", "ADD ONS", "Topping", 100));
        list.add(new Product("p74", "Coconut Milk", "Coconut milk add-on.", "ADD ONS", "Milk", 950));
        list.add(new Product("p75", "Almond Milk", "Almond milk add-on.", "ADD ONS", "Milk", 950));
        list.add(new Product("p76", "Oat Milk", "Oat milk add-on.", "ADD ONS", "Milk", 950));
        list.add(new Product("p77", "Whipped Cream", "Whipped cream topping.", "ADD ONS", "Topping", 100));
        list.add(new Product("p78", "Extra Syrup", "Extra flavor syrup.", "ADD ONS", "Syrup", 120));
        list.add(new Product("p79", "Extra Ice Cream", "Extra scoop of ice cream.", "ADD ONS", "Dessert", 100));
        list.add(new Product("p80", "Up Size", "Upgrade to larger size.", "ADD ONS", "Size", 250));

        return list;
    }
}