package me.mynqme.blocks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeave implements Listener {
    private final Blocks instance;
    public JoinLeave(Blocks plugin) {
        this.instance = plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int blocks = instance.database.getBlocks(uuid);
        this.instance.blocks.put(uuid, blocks);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        new AsyncTask(instance, () -> {
            this.instance.database.setBlocks(uuid, this.instance.blocks.get(uuid));
        });
    }
}
