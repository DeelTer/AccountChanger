package ru.deelter.accountchanger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.deelter.accountchanger.commands.ChangeCommand;
import ru.deelter.accountchanger.utils.Console;

import java.io.File;

public final class AccountChanger extends JavaPlugin {

    private static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        File config = new File(getDataFolder().getPath() + "/config.yml");
        if (!config.exists()) {
            Console.info("&6#AccountChanger&f загружаем конфигурацию");
            saveDefaultConfig();
        }

        /* Config loader */
        Config.load();

        /* Hooks */
        if (Config.MY_REQUESTS_ENABLE) {
            if (Bukkit.getPluginManager().getPlugin("MyRequests") == null) {
                Console.info("&6#AccountChanger&f хей, похоже плагин &6MyRequests&f отсутствует!\nВыключите эту функцию в конфиге, если не используете её");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        /* Command register */
        getCommand("changer").setExecutor(new ChangeCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
