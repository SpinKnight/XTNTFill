package dev.spinn.plugins.xtntfill;

import dev.spinn.plugins.xtntfill.command.TNTFillCmd;
import dev.spinn.plugins.xtntfill.factions.FHook;
import dev.spinn.plugins.xtntfill.factions.FManager;
import dev.spinn.plugins.xtntfill.listener.CmdAliasesListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XTNTFill extends JavaPlugin {

    private FHook fHook;

    public void onEnable() {
        init();
    }

    private void init() {
        loadConfig();
        this.fHook = new FHook(this);
        fHook.setup();
        registerListeners();
        registerCommands();
    }

    public void registerCommands() {
        getCommand("tntfill").setExecutor(new TNTFillCmd(this));
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CmdAliasesListener(this),this);
    }

    public void loadConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        this.fHook = new FHook(this);
        fHook.setup();
    }

    public FHook getFHook() {
        return this.fHook;
    }

    public FManager getFManager() {
        return this.fHook.getFManager();
    }

    public Set<String> getTNTFillRanks() {
        return getConfig().getConfigurationSection("rank-settings").getKeys(false);
    }

    public int getTNTFillRankOption(String rank, String option) {
        return getConfig().getInt("rank-settings." + rank + "." + option);
    }

    public int getMaxFillRadius(Player player) {
        ArrayList<Integer> radii = new ArrayList<>();
        String option = "max-radius";
        radii.add(getConfig().getInt(option));

        for (String rank : getTNTFillRanks()) {
            if (!player.hasPermission("tntfill.ranks." + rank)) continue;

            radii.add(getTNTFillRankOption(rank, option));
        }

        return Collections.max(radii);
    }

    public int getMaxFillAmount(Player player) {
        ArrayList<Integer> amounts = new ArrayList<>();
        String option = "max-amount";
        amounts.add(getConfig().getInt(option));

        for (String rank : getTNTFillRanks()) {
            if (!player.hasPermission("tntfill.ranks." + rank)) continue;

            amounts.add(getTNTFillRankOption(rank, option));
        }

        return Collections.max(amounts);
    }

}
