package dev.spinn.plugins.xtntfill.command;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import dev.spinn.plugins.xtntfill.XTNTFill;
import dev.spinn.plugins.xtntfill.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TNTFillCmd implements CommandExecutor {

    private final XTNTFill plugin;

    public TNTFillCmd(XTNTFill plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args[0].equals("reload")) {
            if (sender.hasPermission("tntfill.reload")) {
                plugin.reload();
                Util.sendMessage(sender, plugin.getConfig().getString("lang.plugin-reloaded"));
                return true;
            }
        }

        if (!sender.hasPermission("tntfill.use")) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.no-permission"));
            return true;
        }

        Player player = (Player) sender;
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);

        boolean adminMode = player.hasPermission("tntfill.admin") && player.getGameMode() == GameMode.CREATIVE;

        if (!fPlayer.hasFaction()) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.no-faction"));
            return true;
        }

        if (plugin.getConfig().getBoolean("use-f-perms") && !adminMode && !plugin.getFManager().hasTnTFillPerms(player)) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.no-faction-permission"));
            return true;
        }

        if (args.length == 0) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.radius-missing"));
            return true;
        }

        int radius;

        try {
            radius = Integer.parseInt(args[0]);
            int minRadius = Math.max(plugin.getConfig().getInt("min-radius"), 1);
            int maxRadius = Math.max(plugin.getMaxFillRadius(player), 1);

            if (radius < minRadius) {
                Util.sendMessage(sender, plugin.getConfig().getString("lang.radius-too-small").replace("$max", maxRadius + "").replace("$min", minRadius + ""));
                return true;
            } else if (radius > maxRadius) {
                Util.sendMessage(sender, plugin.getConfig().getString("lang.radius-too-big").replace("$max", maxRadius + "").replace("$min", minRadius + ""));
                return true;
            }

        } catch (NumberFormatException e) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.not-a-number").replace("$number", args[0]));
            return true;
        }

        if (args.length == 1) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.amount-missing"));
            return true;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
            int minAmount = Math.max(plugin.getConfig().getInt("min-amount"), 1);
            int maxAmount = Math.max(plugin.getMaxFillAmount(player), 1);

            if (amount < minAmount || amount < 1) {
                Util.sendMessage(sender, plugin.getConfig().getString("lang.amount-too-small").replace("$max", maxAmount + "").replace("$min", minAmount + ""));
                return true;
            } else if (amount > maxAmount) {
                Util.sendMessage(sender, plugin.getConfig().getString("lang.amount-too-big").replace("$max", maxAmount + "").replace("$min", minAmount + ""));
                return true;
            }

        } catch (NumberFormatException e) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.not-a-number").replace("$number", args[1]));
            return true;
        }

//        Fetch dispensers in radius and count how much can be filled.
        int slots = 9;
        int maxItems = slots * 64;
        int total = 0;

        int allDispensers = 0;
        Block center = player.getLocation().getBlock();
        Block block;
        HashMap<Block, Integer> dispensers = new HashMap<>();
        for (int x = -(radius); x <= radius; x ++){
            for (int y = -(radius); y <= radius; y ++) {
                for (int z = -(radius); z <= radius; z ++) {
                    block = center.getRelative(x, y, z);
                    if (block.getType() == Material.DISPENSER) {
                        allDispensers++;

                        InventoryHolder inventoryHolder = (InventoryHolder) block.getState();
                        Inventory inventory = inventoryHolder.getInventory();

                        int has = 0;
                        for (ItemStack itemStack : inventory.getContents()) {
                            if (itemStack == null) {
                                continue;
                            }

                            if (itemStack.getType() == Material.TNT) {
                                has += itemStack.getAmount();
                            } else {
                                has += maxItems / slots;
                            }
                        }

                        int space = maxItems - has;
                        if (space > 0) {
                            dispensers.put(block, space);
                        }

                    }
                }
            }
        }

        if (allDispensers == 0) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.no-dispensers-nearby").replace("$radius", radius + ""));
            return true;
        }

        // fillAmount - Fill amount PER dispenser
        int fillAmount = amount;
        if (!plugin.getConfig().getBoolean("strict-fill")) {
            fillAmount = (int) Math.floor((double) amount / dispensers.size());
            if (fillAmount <= 0) {
                Util.sendMessage(sender, plugin.getConfig().getString("lang.amount-cant-fill").replace("$radius", radius + "").replace("$dispensers", dispensers.size() + ""));
                return true;
            }
        }

        ArrayList<Integer> filled = new ArrayList<>();
        for (Block dispenser : dispensers.keySet()) {
            int fill = Math.min(fillAmount, dispensers.get(dispenser));
            total += fill;
            filled.add(fill);
        }

        if (!filled.isEmpty()) {
            fillAmount = Collections.max(filled);
        }

        if (total == 0) {
            Util.sendMessage(sender, plugin.getConfig().getString("lang.dispensers-full").replace("$radius", radius + "").replace("$dispensers", allDispensers + ""));
            return true;
        }

        int tnt = plugin.getFManager().getTnt(player);
        if (!adminMode && tnt < total) {
            int needed = total - tnt;
            Util.sendMessage(sender, plugin.getConfig().getString("lang.not-enough-tnt").replace("$dispensers", dispensers.size() + "").replace("$amount", fillAmount + "").replace("$needed", needed + "").replace("$total", total + ""));
            return true;
        }

        if (!adminMode) plugin.getFManager().takeTnt(player, total);

//        Fill dispensers
        for (Block dispenser : dispensers.keySet()) {
            InventoryHolder inventoryHolder = (InventoryHolder) dispenser.getState();
            Inventory inventory = inventoryHolder.getInventory();
            inventory.addItem(new ItemStack(Material.TNT, fillAmount));
            dispenser.getState().update();
        }

        Util.sendMessage(sender, plugin.getConfig().getString("lang.filled-dispensers").replace("$dispensers", dispensers.size() + "").replace("$amount", fillAmount + "").replace("$radius", radius + "").replace("$total", total + "").replace("$left", plugin.getFManager().getTnt(player) + ""));
        return true;
    }
}
