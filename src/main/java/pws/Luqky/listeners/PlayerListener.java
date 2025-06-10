package pws.Luqky.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.kyori.adventure.text.Component;
import pws.Luqky.WsPlugin;
import pws.Luqky.config.ConfigPrisonerPlayers;

import java.util.List;

public class PlayerListener implements Listener {
    private final ConfigPrisonerPlayers configPrisonerPlayers;

    public PlayerListener(ConfigPrisonerPlayers configPrisonerPlayers) {
        this.configPrisonerPlayers = configPrisonerPlayers;
    }

    //Event to prevent prisoners enter to the server when it is empty
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        List<String> prisionerPlayers = configPrisonerPlayers.getPrisonerPlayers();

        //Get the player who activate de event and the players online
        Player player = event.getPlayer();
        List<Player> players = player.getWorld().getPlayers();

        //When the server is empty and the player is on the prisoners list the player is kicked
        if(prisionerPlayers.contains(player.getName()) && players.size() == 1){
            player.kick(Component.text("You can not access when other player are not playing"));
        }
    }

    //Event to prevent prisoners to stay alone in the server when allowed players quit
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskLater(WsPlugin.getInstance(), () -> {
            List<String> prisionerPlayers = configPrisonerPlayers.getPrisonerPlayers();

            //Get the player who activate de event and the players online
            Player player = event.getPlayer();
            List<Player> players = player.getWorld().getPlayers();

            //When the server is empty and the player is on the prisoners list the player is kicked
            if(prisionerPlayers.contains(player.getName()) && players.size() == 1){
                player.kick(Component.text("You can not access when other player are not playing"));
            }
        }, 20L);
    }
}
