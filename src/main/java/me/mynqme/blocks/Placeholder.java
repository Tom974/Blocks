package me.mynqme.blocks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Placeholder extends PlaceholderExpansion
{
    private final Blocks instance;

    public Placeholder(Blocks instance) {
        this.instance = instance;
    }

    public @Nonnull String getIdentifier() {
        return "blocks";
    }

    public @Nonnull String getAuthor() {
        return "MyNqme";
    }

    public @Nonnull String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(final Player p, final String identifier) {
        if (identifier.equalsIgnoreCase("broken")) {
            return String.valueOf(instance.blocks.getOrDefault(p.getUniqueId(), 0));
        } else {
            return "&7&oUnknown placeholder";
        }
    }
}
