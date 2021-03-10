package dev.spinn.plugins.xtntfill.factions;

import org.bukkit.entity.Player;

public interface FManager {

    boolean hasTnTFillPerms(Player player);

    int getTnt(Player player);

    void takeTnt(Player player, int amount);

}
