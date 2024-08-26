package pws.Luqky.utils;

import org.bukkit.ChatColor;

public class MessageUtils {
    //Utility to help creating colorful and stylish messages to the players
    public static String colorMessage(String color, String message){
        return ChatColor.translateAlternateColorCodes('&', color + message);
    }
}
