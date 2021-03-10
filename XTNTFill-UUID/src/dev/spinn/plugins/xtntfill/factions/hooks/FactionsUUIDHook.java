package dev.spinn.plugins.xtntfill.factions.hooks;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import dev.spinn.plugins.xtntfill.factions.FManager;
import org.bukkit.entity.Player;

public class FactionsUUIDHook implements FManager {

    @Override
    public boolean hasTnTFillPerms(Player player) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        return !Permission.TNT_FILL.has(player) && fPlayer.getRole() != Role.ADMIN;
    }

    @Override
    public int getTnt(Player player) {
        return FPlayers.getInstance().getByPlayer(player).getFaction().getTNTBank();
    }

    public void takeTnt(Player player, int amount) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = fPlayer.getFaction();
        faction.setTNTBank(faction.getTNTBank() - amount);
    }

}
