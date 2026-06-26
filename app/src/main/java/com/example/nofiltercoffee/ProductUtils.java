package com.example.nofiltercoffee;

public class ProductUtils {

    public static boolean requiresCustomization(String category) {
        if (category == null) return false;
        return isDrink(category) || isCombo(category);
    }

    public static boolean isDrink(String category) {
        if (category == null) return false;
        return category.equals("Hot Coffee") ||
                category.equals("Iced Coffee") ||
                category.equals("Matcha") ||
                category.equals("Frappes");
    }


    public static boolean isCombo(String category) {
        if (category == null) return false;
        return category.equals("Combo") ||
                category.equals("Deals") ||
                category.equals("Combos") ||
                category.toLowerCase().contains("combo") ||
                category.toLowerCase().contains("deal");
    }

    public static int getSmallPrice(int price) {
        return Math.max(price - 100, 0);
    }

    public static int getRegularPrice(int price) {
        return price;
    }

    public static int getLargePrice(int price) {
        return price + 150;
    }

    public static int getPriceBySize(int price, String size) {
        if (size == null) return price;
        switch (size) {
            case "Small": return getSmallPrice(price);
            case "Large": return getLargePrice(price);
            default: return getRegularPrice(price);
        }
    }
}