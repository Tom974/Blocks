package me.mynqme.blocks;

import me.mynqme.blocks.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AsyncTask {
    public AsyncTask(Plugin plugin, Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }
}

