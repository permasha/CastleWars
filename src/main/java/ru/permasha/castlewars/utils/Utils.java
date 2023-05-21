package ru.permasha.castlewars.utils;

import org.bukkit.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

    public static String colorize(String msg) {
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = pattern.matcher(msg);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getTime(int seconds){
        String minutesStr = (seconds >= 60) ? String.valueOf(seconds / 60) : "0";
        String secondsStr = (seconds >= 60) ? ((seconds % 60 > 9) ? String.valueOf(seconds % 60) : "0" + seconds % 60) : ((seconds > 9) ? String.valueOf(seconds) : "0" + seconds);

        return minutesStr + ":" + secondsStr;
    }

    public static Color stringToColor(final String value) {
        if (value == null) {
            return Color.BLACK;
        }
        // if we can't decode lets try to get it by name
        try {
            // try to get a color by name using reflection
            final Field f = Color.class.getField(value);

            return (Color) f.get(null);
        } catch (Exception ce) {
            // if we can't get any color return black
            return Color.BLACK;
        }
    }

}
