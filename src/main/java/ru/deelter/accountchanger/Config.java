package ru.deelter.accountchanger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.deelter.accountchanger.utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static List<String> REQUESTS = new ArrayList<>();

    /* Settings */
    public static boolean MY_REQUESTS_ENABLE = false;
    public static boolean CHECK_BANNED;
    public static boolean CHECK_WHITELIST;

    public static boolean INVENTORY_TRANSFER;
    public static boolean STATISTIC_TRANSFER;
    public static boolean ADVANCEMENTS_TRANSFER;

    /* Messages */
    public static String MSG_HELP;
    public static String MSG_NO_PERM;
    public static String MSG_RELOAD;
    public static String MSG_INVALID_UUID;


    public static String MSG_PLAYER_NOT_EXIST;
    public static String MSG_PLAYER_ONLINE;
    public static String MSG_PLAYER_BANNED;
    public static String MSG_PLAYER_NOT_WHITELISTED;
    public static String MSG_SUCCESS;


    public static void reload() {
        AccountChanger.getInstance().reloadConfig();
        load();
    }

    public static void load() {
        JavaPlugin plugin = AccountChanger.getInstance();

        /* Settings */
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection settings = config.getConfigurationSection("settings");
        CHECK_BANNED = settings.getBoolean("check-banned");
        CHECK_WHITELIST = settings.getBoolean("check-whitelist");

        INVENTORY_TRANSFER = settings.getBoolean("inventory-transfer");
        STATISTIC_TRANSFER = settings.getBoolean("statistic-transfer");
        ADVANCEMENTS_TRANSFER = settings.getBoolean("advancements-transfer");

        /* Api hook */
        ConfigurationSection myrequests = config.getConfigurationSection("api.myrequests");
        MY_REQUESTS_ENABLE = myrequests.getBoolean("enable");
        REQUESTS = myrequests.getStringList("run-requests");

        /* Messages */
        ConfigurationSection messages = config.getConfigurationSection("messages");
        MSG_NO_PERM = Colors.set(messages.getString("no-permission"));
        MSG_RELOAD = Colors.set(messages.getString("reload"));
        MSG_HELP = Colors.set(messages.getString("help"));
        MSG_INVALID_UUID = Colors.set(messages.getString("invalid-uuid"));

        MSG_PLAYER_NOT_EXIST = Colors.set(messages.getString("player-not-exist"));
        MSG_PLAYER_ONLINE = Colors.set(messages.getString("player-online"));
        MSG_PLAYER_BANNED = Colors.set(messages.getString("player-banned"));
        MSG_PLAYER_NOT_WHITELISTED = Colors.set(messages.getString("player-whitelist"));
        MSG_SUCCESS = Colors.set(messages.getString("success"));

    }
}
