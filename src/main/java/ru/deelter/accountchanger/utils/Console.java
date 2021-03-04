package ru.deelter.accountchanger.utils;

import org.bukkit.Bukkit;

public class Console {

    /** Send message to console */
    public static void info(String s) {
        Bukkit.getLogger().info(Colors.set(s));
    }
}
