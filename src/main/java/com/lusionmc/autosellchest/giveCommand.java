package com.lusionmc.autosellchest;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.List;


public class giveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

       //Create Sell Chest

        ItemStack chest = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = chest.getItemMeta();
        meta.setDisplayName("§8[§4Autosell Chest§8]");
        List<String> lore = new ArrayList<>();
        lore.add("§7[§c!§7] - §fAutomatically sell any materials inserted into this.");
        lore.add("§7[§e!§7] - §fAutomate selling with this by attaching §e§lHoppers §r§fto it!");
        lore.add("§7[§e!§7] - §f§lOpen §fthe chest to get a trove of valuable §e§lStatistics§r§f.");
        lore.add("§c§lThis block may only be broken by the player who placed it!");
        meta.setLore(lore);
        chest.setItemMeta(meta);

        //     /sellchestgive <player> sdf sdf sdf

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {

                Player target = Bukkit.getPlayerExact(args[0]);


                //Checking if player exists
                if (Bukkit.getPlayerExact(args[0]) != null) {

                    target.getInventory().addItem(chest);
                    player.sendMessage("Given 1 Autosell Chest to " + target);
                    target.sendMessage("You have received 1 Autosell Chest!");

                } else { player.sendMessage("ERROR Player was not found!"); }

            //Invalid usage of command
            } else { player.sendMessage(Color.RED + "ERROR Invalid Usage! Please use /sellchestgive <player> <amount>"); }

        }

        return true;
    }
}

