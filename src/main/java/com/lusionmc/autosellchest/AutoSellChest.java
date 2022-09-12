package com.lusionmc.autosellchest;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import java.io.File;
import java.util.UUID;

public final class AutoSellChest extends JavaPlugin {

    public static Economy econ = null;
    FileConfiguration config;
    File cfile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Auto Sell Chests has been enabled!");

        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        cfile = new File(getDataFolder(), "config.yml");
        getCommand("givechest").setExecutor(new giveCommand());
        getCommand("ascreload").setExecutor(new ascreloadcmd());

        Bukkit.getPluginManager().registerEvents(new myListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        System.out.println("Auto Sell Chests has been disabled!");
    }



    @EventHandler
    public void transfer(InventoryMoveItemEvent e) {
        //Bukkit.getServer().broadcastMessage("Type: " + e.getItem().getType());
        Bukkit.broadcastMessage("TEST1");
        if (e.getInitiator().getType().equals(InventoryType.HOPPER) && e.getDestination().getType().equals(InventoryType.CHEST)) { //Hopper -> Chest Check
            String transfer = e.getItem().getType().toString().toUpperCase();
            Bukkit.broadcastMessage("TEST2");
            if (getConfig().getConfigurationSection("ItemPrices").getKeys(false).contains(e.getItem().getType().toString().toUpperCase())) {
                Bukkit.broadcastMessage("TEST3");
                Block b= e.getDestination().getLocation().getBlock();
                TileState state = (TileState) b.getState();
                PersistentDataContainer data = state.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(AutoSellChest.getPlugin(AutoSellChest.class), "autoSellChests");
                String pName =Bukkit.getPlayer(data.get(key, PersistentDataType.STRING)).getName();
                UUID uuid = Bukkit.getPlayer(data.get(key, PersistentDataType.STRING)).getUniqueId();

                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid); //Grabbing offlinePlayer
                if (player == null) {
                    return;
                }
                double charge = getConfig().getDouble("ItemPrices." + transfer + ".price"); // Grabbing itemPrice
                double multiply = charge * e.getItem().getAmount();
                EconomyResponse r = econ.depositPlayer(player, multiply); //Payment
                NamespacedKey totalSold = new NamespacedKey(AutoSellChest.getPlugin(AutoSellChest.class), "ASCtotalSold");
                if(data.has(totalSold, PersistentDataType.DOUBLE)){
                    Bukkit.broadcastMessage("TEST4");
                    double old = data.get(totalSold, PersistentDataType.DOUBLE);
                    data.set(totalSold, PersistentDataType.DOUBLE, multiply+old);
                }else {
                    data.set(totalSold, PersistentDataType.DOUBLE, multiply);
                }
                if (r.transactionSuccess()) {
                    Bukkit.broadcastMessage("TEST5");
                    // ADD STAT LOGIC HERE
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        public void run() {
                                        e.getDestination().clear();
                                    }}, 1);
                }
            }
        }
    }
}
