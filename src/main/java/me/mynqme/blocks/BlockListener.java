package me.mynqme.blocks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class BlockListener implements Listener {
    private final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();

    private final Blocks plugin;

    public BlockListener(Blocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (isInAllowedRegion(player, event.getBlock().getLocation())) {
            UUID uuid = player.getUniqueId();
            int blocks = plugin.blocks.getOrDefault(uuid, 0);
            plugin.blocks.put(uuid, blocks + 1);
        }
    }

    public boolean isInAllowedRegion(Player player, Location loc) {
        RegionManager manager = container.get(loc.getWorld());
        boolean found = false;
        for (ProtectedRegion region : manager.getApplicableRegions(loc)) {
            if (region.getFlag(DefaultFlag.BLOCK_BREAK) == State.ALLOW) {
                found = true;
            }
        }

        return found;
    }
}
