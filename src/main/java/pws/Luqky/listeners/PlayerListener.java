package pws.Luqky.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
        //Load de prisoner list from de config file
        configPrisonerPlayers.reloadConfig();
        List<String> prisionerPlayers = configPrisonerPlayers.getPrisonerPlayers();

        //Get the player who activate de event and the players online
        Player player = event.getPlayer();
        List<Player> players = player.getWorld().getPlayers();

        //When the server is empty and the player is on the prisoners list the player is kicked
        if(prisionerPlayers.contains(player.getName()) && players.isEmpty()){
            player.kickPlayer("You can not access when other player are not playing");
        }
    }

    //Event to prevent prisoners to stay alone in the server when allowed players quit
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        //Load de prisoner list from de config file
        configPrisonerPlayers.reloadConfig();
        List<String> prisionerPlayers = configPrisonerPlayers.getPrisonerPlayers();

        //Get the list of the players who are connected
        List<Player> players = event.getPlayer().getWorld().getPlayers();

        //Search if someone of the players is not a prisoner
        boolean validPlayer = false;
        for(Player p : players){

            //Ignore and continue when the player in the loop is who activated the event
            if(p.getName().equals(event.getPlayer().getName()))
                continue;

            //If it finds some player who is not on the prisioner list set the boolean to true and end the loop
            if(!prisionerPlayers.contains(p.getName())){
                validPlayer = true;
                break;
            }
        }

        //If all the players are in the prisoner list then they are kicked
        if(!validPlayer){
            for(Player p : players){
                p.kickPlayer("You can not access when other player are not playing");
            }
        }
    }
}
