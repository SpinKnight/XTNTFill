package dev.spinn.plugins.xtntfill.factions;

import dev.spinn.plugins.xtntfill.XTNTFill;
import dev.spinn.plugins.xtntfill.factions.hooks.FactionsSaberHook;
import dev.spinn.plugins.xtntfill.factions.hooks.FactionsSavageHook;
import dev.spinn.plugins.xtntfill.factions.hooks.FactionsUUIDHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class FHook {

    private final XTNTFill plugin;
    private FManager fManager;

    public FHook(XTNTFill plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        Plugin factions = Bukkit.getPluginManager().getPlugin("Factions");
        if (factions != null) {
            switch (factions.getDescription().getMain()) {
                case "com.massivecraft.factions.FactionsPlugin":
                    if (factions.getDescription().getAuthors().contains("Driftay")) {
                        plugin.getLogger().info("Hooking into SaberFactions now...");
                        this.fManager = new FactionsSaberHook();
                    } else {
                        plugin.getLogger().info("Hooking into FactionsUUID (or fork) now...");
                        this.fManager = new FactionsUUIDHook();
                    }
                    break;
                case "com.massivecraft.factions.SavageFactions":
                    plugin.getLogger().info("Hooking into SavageFactions now...");
                    this.fManager = new FactionsSavageHook();
                    break;
            }
        }

        if (this.fManager == null) {
            plugin.getLogger().severe("Could not find a supported Factions Plugin.");
            plugin.getLogger().severe("Shutting plugin down...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

    }

    public FManager getFManager() {
        return this.fManager;
    }

}
