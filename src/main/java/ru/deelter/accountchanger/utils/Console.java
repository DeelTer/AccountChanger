package ru.deelter.accountchanger.utils;

import org.bukkit.Bukkit;
import ru.deelter.accountchanger.Config;

public class Console {

    /** Send message to console */
    public static void info(String s) {
        Bukkit.getLogger().info(Colors.set(s));
    }

    public static void debug(String s) {
        if (Config.DEBUG)
            info("&e" + s);
    }
}
