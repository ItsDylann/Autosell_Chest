package com.lusionmc.autosellchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class myListener implements Listener {
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e){
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
        Player p = e.getPlayer();

        UUID uuid = p.getUniqueId();

        Block b = e.getBlock();
        if(!(b.getType() == Material.CHEST)){
            return;
        } else {

        if(!(b.getState() instanceof TileState)){
            return;
        }
        if(!(e.getItemInHand().getItemMeta().hasLore())){return;}
            TileState state = (TileState) b.getState();

            PersistentDataContainer data = p.getPersistentDataContainer();
            PersistentDataContainer c = state.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(AutoSellChest.getPlugin(AutoSellChest.class), "autoSellChests");
            data.set(key, PersistentDataType.STRING, "true");
            c.set(key, PersistentDataType.STRING, uuid.toString());
        }


    }
    @EventHandler
    public void onBlockRemove(BlockBreakEvent e) {
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
        Player p = e.getPlayer();

        UUID uuid = p.getUniqueId();

        Block b = e.getBlock();
        TileState state = (TileState) b.getState();
 
        PersistentDataContainer c = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(AutoSellChest.getPlugin(AutoSellChest.class), "autoSellChests");
        if(!(c.has(key, PersistentDataType.STRING))){return;}
        UUID targetuuid = Bukkit.getPlayer(c.get(key,PersistentDataType.STRING)).getUniqueId();
        if(uuid != targetuuid){
            p.sendMessage(ChatColor.RED + "This chest is not owned by you");
            e.setCancelled(true);
        } else e.setCancelled(true);
        if(p.getInventory().firstEmpty() != -1){
        b.setType(Material.AIR);
        p.getInventory().addItem(chest);}
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e){

        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK){return;}
        UUID uuid = p.getUniqueId();
        Block block = e.getClickedBlock();
        TileState state = (TileState) block.getState();
        PersistentDataContainer c = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(AutoSellChest.getPlugin(AutoSellChest.class), "autoSellChests");
        if(c.has(key, PersistentDataType.STRING)){
            e.setCancelled(true);
            NamespacedKey totalSold = new NamespacedKey(AutoSellChest.getPlugin(AutoSellChest.class), "ASCtotalSold");

            p.sendMessage("§cTotal amount sold §7- §a$" + c.get(totalSold, PersistentDataType.DOUBLE));

        }



    }

}
