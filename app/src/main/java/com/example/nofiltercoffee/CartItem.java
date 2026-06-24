package com.example.nofiltercoffee;
public class CartItem { public Product product; public int quantity, selectedPrice; public String options; public CartItem(Product product,int quantity,int selectedPrice,String options){this.product=product;this.quantity=quantity;this.selectedPrice=selectedPrice;this.options=options;} public int getTotal(){return selectedPrice*quantity;} }
