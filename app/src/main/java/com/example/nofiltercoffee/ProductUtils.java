package com.example.nofiltercoffee;
public class ProductUtils { public static boolean isDrink(String c){return c.equals("Hot Coffee")||c.equals("Iced Coffee")||c.equals("Matcha")||c.equals("Frappes");} public static int getSmallPrice(int p){return Math.max(p-100,0);} public static int getRegularPrice(int p){return p;} public static int getLargePrice(int p){return p+150;} }
