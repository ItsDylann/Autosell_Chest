package com.lusionmc.autosellchest;

import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class ascreloadcmd implements CommandExecutor {
    FileConfiguration config;
    File cfile;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if (cmd.getName().equalsIgnoreCase("ascreload")){
            config = YamlConfiguration.loadConfiguration(cfile);
            sender.sendMessage(Color.GREEN + "Successfully reloaded Config!");
        }

        return true;
    }
}
