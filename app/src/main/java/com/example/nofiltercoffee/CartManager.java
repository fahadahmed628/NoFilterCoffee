package com.example.nofiltercoffee;

import java.util.ArrayList;

public class CartManager {
    private static final ArrayList<CartItem> cart = new ArrayList<>();
    private static boolean useWallet = false, useBeans = false, promoRequested = false;

    public static void add(Product p) { add(p, p.price, "Standard item"); }
    public static void add(Product p, int price, String opt) {
        for (CartItem i : cart) {
            if (i.product.id.equals(p.id) && i.selectedPrice == price && i.options.equals(opt)) {
                i.quantity++;
                return;
            }
        }
        cart.add(new CartItem(p, 1, price, opt));
    }

    public static void increase(CartItem i) { i.quantity++; }
    public static void decrease(CartItem i) {
        i.quantity--;
        if (i.quantity <= 0) cart.remove(i);
    }
    public static ArrayList<CartItem> getCart() { return cart; }
    public static int getCount() { int c = 0; for (CartItem i : cart) c += i.quantity; return c; }

    public static int getSubtotal() { int s = 0; for (CartItem i : cart) s += i.getTotal(); return s; }
    public static int getTax16Percent() { return Math.round(getSubtotal() * 0.16f); }
    public static int getTax5Percent() { return Math.round(getSubtotal() * 0.05f); }
    public static int getTaxByPayment(boolean card) { return card ? getTax5Percent() : getTax16Percent(); }

    public static void setUseWallet(boolean v) { useWallet = v; }
    public static boolean isUseWallet() { return useWallet; }
    public static void setUseCoffeeBeans(boolean v) { useBeans = v; }
    public static boolean isUseCoffeeBeans() { return useBeans; }

    public static void requestPromo() { promoRequested = true; }
    public static void removePromo() { promoRequested = false; }
    public static boolean isPromoRequested() { return promoRequested; }
    public static boolean isPromoEligible() { return getSubtotal() >= 1500; }
    public static boolean isPromoApplied() { return promoRequested && isPromoEligible(); }
    public static int getPromoAmountNeeded() { return Math.max(1500 - getSubtotal(), 0); }
    public static int getPromoDiscount() { return isPromoApplied() ? 500 : 0; }

    public static int getTotalBeforeRewards(boolean card) { return Math.max(getSubtotal() + getTaxByPayment(card) - getPromoDiscount(), 0); }
    public static int getWalletDiscount(int wallet, boolean card) { return useWallet ? Math.min(wallet, getTotalBeforeRewards(card)) : 0; }
    public static int getCoffeeBeansDiscount(int beans, int wallet, boolean card) {
        if (!useBeans) return 0;
        int rem = getTotalBeforeRewards(card) - getWalletDiscount(wallet, card);
        return Math.min(beans, Math.max(rem, 0));
    }

    public static int getPayableTotal(int wallet, int beans, boolean card) {
        return Math.max(getTotalBeforeRewards(card) - getWalletDiscount(wallet, card) - getCoffeeBeansDiscount(beans, wallet, card), 0);
    }
    public static int getTotal() { return getSubtotal() + getTax16Percent(); }
    public static int getCoffeeBeansEarned() { return Math.round(getSubtotal() * 0.10f); }
    public static void clearCart() { cart.clear(); useWallet = false; useBeans = false; promoRequested = false; }
}