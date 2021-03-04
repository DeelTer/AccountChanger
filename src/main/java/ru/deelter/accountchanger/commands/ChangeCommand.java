package ru.deelter.accountchanger.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ru.deelter.accountchanger.Config;
import ru.deelter.accountchanger.utils.Colors;
import ru.deelter.myrequests.utils.MyRequest;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class ChangeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Config.MSG_NO_PERM);
            return true;
        }

        if (args[0] != null && args[0].equalsIgnoreCase("RELOAD")) {
            sender.sendMessage(Config.MSG_RELOAD);
            Config.reload();
        }

        if (args.length < 2) {
            sender.sendMessage(Config.MSG_HELP);
            return true;
        }

        if (args[0].length() != 36 || args[1].length() != 36) {
            sender.sendMessage(Config.MSG_INVALID_UUID);
            return true;
        }

        /* Change process */
        UUID uuid = UUID.fromString(args[0]);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        final String name = player.getName();

        if (!player.hasPlayedBefore()) {
            sender.sendMessage(Colors.set(Config.MSG_PLAYER_NOT_EXIST));
            return true;
        }

        if (Config.CHECK_WHITELIST && !player.isWhitelisted()) {
            sender.sendMessage(Config.MSG_PLAYER_NOT_WHITELISTED);
            return true;
        }

        if (Config.CHECK_BANNED && player.isBanned()) {
            sender.sendMessage(Config.MSG_PLAYER_BANNED);
            return true;
        }

        if (player.isOnline()) {
            sender.sendMessage(Config.MSG_PLAYER_ONLINE);
            return true;
        }

        UUID uuid2 = UUID.fromString(args[1]);
        OfflinePlayer player2 = Bukkit.getOfflinePlayer(uuid2);
        final String name2 = player2.getName();

        if (player2.isOnline()) {
            sender.sendMessage(Config.MSG_PLAYER_ONLINE);
            return true;
        }

        /* Whitelist change */
        player.setWhitelisted(false);
        player2.setWhitelisted(true);

        /* Change files */
        change(uuid.toString(), uuid2.toString());
        sender.sendMessage(Config.MSG_SUCCESS.replace("%NAME%", name).replace("%NAME2%", name2));

        /* API hook */
        if (Config.MY_REQUESTS_ENABLE) {
            for (String id : Config.REQUESTS) {
                MyRequest request = MyRequest.getRequest(id).clone();
                Map<String, String> body = request.getBody();
                for (Map.Entry<String, String> entry : body.entrySet()) {
                    /* Placeholders confirm */
                    String formatted = entry.getValue()
                            .replace("%NAME%", name)
                            .replace("%NAME2%", name2)
                            .replace("%UUID%", uuid.toString())
                            .replace("%UUID2%", uuid2.toString());
                    body.replace(entry.getKey(), formatted);
                }
                request.send();
            }
        }
        return true;
    }

    private void change(String uuid, String uuid2) {
        /* Inventory files */
        String world = Bukkit.getWorldContainer().getPath() + "/world";
        if (Config.INVENTORY_TRANSFER)
            renameFileTo(world + "/playerdata/" + uuid, uuid2, ".dat");

        /* Statistic files */
        if (Config.STATISTIC_TRANSFER)
            renameFileTo(world + "/stats/" + uuid, uuid2, ".json");

        /* Advancements files */
        if (Config.ADVANCEMENTS_TRANSFER)
            renameFileTo(world + "/advancements/" + uuid, uuid2, ".json");
    }

    /** Renames the old player file to the new one */
    private void renameFileTo(String from, String to, String format) {
        File file = new File(from + format);
        if (!file.exists())
            return;

        file.renameTo(new File(file.getParent() + "/" + to + format));
    }
}
