package me.mynqme.blocks;

import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import me.mynqme.blocks.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import de.leonhard.storage.Yaml;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.UUID;

public final class Blocks extends JavaPlugin {

    public HashMap<UUID, Integer> blocks = new HashMap<UUID, Integer>();
    public Database database;
    public Yaml config;
    public static Blocks instance;

    @Override
    public void onEnable() {
        this.database = new Database(this);
        this.database.createBlocksTable();
        config = SimplixBuilder.fromFile(new File(getDataFolder(),"config")).addInputStream(getResource("config.yml")).setConfigSettings(ConfigSettings.PRESERVE_COMMENTS).createYaml();
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new JoinLeave(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getCommand("blocks").setExecutor(new BlocksCommand(this));

        new Placeholder(this).register();

        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            int blocks = this.database.getBlocks(uuid);
            p.sendMessage("You have broken " + blocks + " blocks!");
            this.blocks.put(uuid, blocks);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                updateBlocks();
                Bukkit.getOperators().forEach(op -> {
                    if (op.isOnline()) {
                        Player target = Bukkit.getPlayer(op.getName());
                        if (target != null) {
                            target.sendMessage("§5§lPlasma§f§lMC §8» §fSaved all broken blocks to database!");
                        }
                    }
                });
            }
        }.runTaskTimerAsynchronously(this, (long) this.config.getInt("autosave-interval") * 20L, (long) this.config.getInt("autosave-interval") * 20L);
    }

    public void updateBlocks() {
        // run in async ofcourse
        new AsyncTask(this, () -> {
            for (UUID uuid : this.blocks.keySet()) {
                database.setBlocks(uuid, this.blocks.get(uuid));
            }
        });
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (UUID uuid : this.blocks.keySet()) {
            database.setBlocks(uuid, this.blocks.get(uuid));
        }
    }

    public static Blocks getInstance() {
        return instance;
    }
}
