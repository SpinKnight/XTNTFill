package dev.spinn.plugins.xtntfill.factions.hooks;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import dev.spinn.plugins.xtntfill.factions.FManager;
import org.bukkit.entity.Player;

public class FactionsSaberHook implements FManager {

    @Override
    public boolean hasTnTFillPerms(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return !Permission.TNTFILL.has(player) && fPlayer.getRole() != Role.LEADER;
    }

    @Override
    public int getTnt(Player player) {
        return FPlayers.getInstance().getByPlayer(player).getFaction().getTnt();
    }

    @Override
    public void takeTnt(Player player, int amount) {
        FPlayers.getInstance().getByPlayer(player).getFaction().takeTnt(amount);
    }

}
