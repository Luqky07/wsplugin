package pws.Luqky.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class MessageUtils {
    //Utility to help creating colorful and stylish messages to the players
    public static Component colorMessage(TextColor color, String message){
        return Component.text(message).color(color);
    }
}
