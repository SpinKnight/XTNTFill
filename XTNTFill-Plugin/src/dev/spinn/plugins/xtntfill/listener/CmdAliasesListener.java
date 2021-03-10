package dev.spinn.plugins.xtntfill.listener;

import dev.spinn.plugins.xtntfill.XTNTFill;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CmdAliasesListener implements Listener {

    private final XTNTFill plugin;

    public CmdAliasesListener(XTNTFill plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().toLowerCase();
        int cmdLength = cmd.length() - 1; // -1 = slash
        for (String alias : plugin.getConfig().getStringList("cmd-aliases")) {
            if (!cmd.startsWith("/" + alias) || (cmdLength > alias.length() && cmd.charAt(alias.length() + 1) != ' ')) continue;
            event.setCancelled(true);
            String args = cmd.substring(alias.length() + 1);
            Bukkit.dispatchCommand(event.getPlayer(),"tntfill" + args);
            break;
        }
    }

}
