package pws.Luqky;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pws.Luqky.commands.JailCommands;
import pws.Luqky.config.ConfigPrisonerPlayers;
import pws.Luqky.listeners.PlayerListener;

import java.util.Objects;

public class WsPlugin extends JavaPlugin {
    //Custom configuration for prisoners
    private ConfigPrisonerPlayers configPrisonerPlayers;

    //Execution when server starts
    public void onEnable() {
        configPrisonerPlayers = new ConfigPrisonerPlayers(this);
        configPrisonerPlayers.loadConfig();

        registerCommands();
        registerEvents();
    }

    //Execution when server stops
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',  "&cBye World!"));
    }

    //Function to register custom commands
    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("jail")).setExecutor(new JailCommands(this.configPrisonerPlayers));
    }

    //Function to register custom event handlers
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this.configPrisonerPlayers), this);
    }
}
