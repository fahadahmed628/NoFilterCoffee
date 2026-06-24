package com.example.nofiltercoffee;
import android.content.*; import java.text.*; import java.util.*;
public class UserSession {
 static final String P="NoFilterUserSession";
 static SharedPreferences sp(Context c){return c.getSharedPreferences(P,Context.MODE_PRIVATE);} 
 public static void createAccount(Context c,String n,String e,String ph,String pw){sp(c).edit().putBoolean("logged",true).putString("name",n).putString("email",e).putString("phone",ph).putString("password",pw).putInt("beans",0).putInt("wallet",0).putString("cards","").putString("orders","").apply();}
 public static boolean hasAccount(Context c){return !sp(c).getString("email","").isEmpty();}
 public static boolean isLoggedIn(Context c){return sp(c).getBoolean("logged",false);} public static void logout(Context c){sp(c).edit().putBoolean("logged",false).apply();}
 public static boolean login(Context c,String e,String pw){boolean ok=sp(c).getString("email","").equalsIgnoreCase(e)&&sp(c).getString("password","").equals(pw); if(ok)sp(c).edit().putBoolean("logged",true).apply(); return ok;}
 public static String getName(Context c){return sp(c).getString("name","Guest Customer");} public static String getEmail(Context c){return sp(c).getString("email","guest@example.com");} public static String getPhone(Context c){return sp(c).getString("phone","+92 300 0000000");}
 public static int getCoffeeBeans(Context c){return sp(c).getInt("beans",0);} public static void setCoffeeBeans(Context c,int b){sp(c).edit().putInt("beans",Math.max(b,0)).apply();}
 public static int getWalletBalance(Context c){return sp(c).getInt("wallet",0);} public static void setWalletBalance(Context c,int b){sp(c).edit().putInt("wallet",Math.max(b,0)).apply();}
 public static void saveCard(Context c,String holder,String number,String expiry,String cvv){String masked=number; String card=holder+"|"+masked+"|"+expiry+"|"+cvv; String old=sp(c).getString("cards",""); if(!old.contains(masked)) sp(c).edit().putString("cards", old.isEmpty()?card:old+";;"+card).apply();}
 public static String getCards(Context c){return sp(c).getString("cards","");}
 public static void addOrder(Context c,String id,String pickup,long pickupMillis,int total){String order=id+"|"+pickup+"|"+pickupMillis+"|"+total; String old=sp(c).getString("orders",""); sp(c).edit().putString("orders", old.isEmpty()?order:old+";;"+order).apply();}
 public static String getOrders(Context c){return sp(c).getString("orders","");}
}
