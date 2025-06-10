package pws.Luqky;

import org.bukkit.plugin.java.JavaPlugin;
import pws.Luqky.commands.JailCommands;
import pws.Luqky.config.ConfigPrisonerPlayers;
import pws.Luqky.listeners.PlayerListener;

import java.util.Objects;

public class WsPlugin extends JavaPlugin {
    //Custom configuration for prisoners
    private ConfigPrisonerPlayers configPrisonerPlayers;
     private static WsPlugin instance;

    //Execution when server starts
    public void onEnable() {
        instance = this;
        configPrisonerPlayers = new ConfigPrisonerPlayers(this);
        configPrisonerPlayers.loadConfig();

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    //Function to register custom commands
    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("jail")).setExecutor(new JailCommands(this.configPrisonerPlayers));
    }

    //Function to register custom event handlers
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this.configPrisonerPlayers), this);
    }

    public static WsPlugin getInstance() {
        return instance;
    }
}
