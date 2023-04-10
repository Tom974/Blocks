package me.mynqme.blocks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.PlaceholderAPI;
import java.util.UUID;

public class BlocksCommand implements CommandExecutor {
    public Blocks instance;
    public BlocksCommand(Blocks plugin) {
        this.instance = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blocks")) {
            if (args.length > 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length == 3) {
                        Player player = Bukkit.getPlayer(args[1]);
                        UUID uuid = player.getUniqueId();

                        this.instance.blocks.put(uuid, Integer.parseInt(args[2]));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                this.instance.config.getString("messages.prefix") +
                                this.instance.config.getString("messages.set-blocks").replace("%player%", args[1]).replace("%blocks%", args[2]))
                        );
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                this.instance.config.getString("messages.prefix") +
                                this.instance.config.getString("messages.invalid-arguments"))
                        );
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.instance.config.getString("messages.prefix") +
                            this.instance.config.getString("messages.invalid-arguments"))
                    );
                    return true;
                }
            }

            if (args.length == 1) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    UUID uuid = player.getUniqueId();
                    int blocks = this.instance.blocks.getOrDefault(uuid, 0);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.instance.config.getString("messages.prefix") +
                            this.instance.config.getString("messages.blocks-broken").replace("%player%", args[0]).replace("%blocks%", String.valueOf(blocks)))
                    );
                    return true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.instance.config.getString("messages.prefix") +
                            this.instance.config.getString("messages.invalid-arguments"))
                    );
                    return true;
                }
            }

            Player player = (Player) sender;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    this.instance.config.getString("messages.prefix") +
                    this.instance.config.getString("messages.blocks-broken").replace("%blocks%", PlaceholderAPI.setPlaceholders(player, "%nf_4X_blocks_broken%")))
            );
            return true;

        }
        return true;
    }
}
