package pws.Luqky.config;

import org.bukkit.configuration.file.FileConfiguration;
import pws.Luqky.WsPlugin;

import java.util.List;

public class ConfigPrisonerPlayers {
    private CustomConfig customConfig;
    private WsPlugin wsPlugin;
    private List<String> prisonerPlayers;

    //Constructor to initialize the custom config for prisoners
    public ConfigPrisonerPlayers(WsPlugin wsPlugin) {
        this.wsPlugin = wsPlugin;
        this.customConfig = new CustomConfig("config_prisoner_players.yml", null, wsPlugin);

        customConfig.registerConfig();
        loadConfig();
    }

    //Load the config
    public void loadConfig() {
        FileConfiguration config = customConfig.getConfig();

        prisonerPlayers = config.getStringList("prisoner_players");
    }

    //Reload the config
    public void reloadConfig() {
        customConfig.reloadConfig();
        loadConfig();
    }

    //Get the list of prisoners
    public List<String> getPrisonerPlayers() {
        return prisonerPlayers;
    }
}
