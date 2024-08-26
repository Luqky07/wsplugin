package pws.Luqky.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pws.Luqky.config.ConfigPrisonerPlayers;
import pws.Luqky.utils.MessageUtils;

import java.util.List;

public class JailCommands implements CommandExecutor {

    private final ConfigPrisonerPlayers configPrisonerPlayers;

    public JailCommands(ConfigPrisonerPlayers configPrisonerPlayers) {
        this.configPrisonerPlayers = configPrisonerPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        //Validate permission
        if(!sender.hasPermission("wsplugin.commands.jail")){
            sender.sendMessage(MessageUtils.colorMessage("&c&l", "You do not have permission to use this command!"));
            return true;
        }

        //Validate arguments length
        if(args.length == 0){
            sender.sendMessage(MessageUtils.colorMessage("&c&l", "The command needs at least one argument."));
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();
        Location jailLocation = new Location(world, 300, 300, 300);

        Player prisioner;

        configPrisonerPlayers.reloadConfig();
        List<String> prisionerPlayers = configPrisonerPlayers.getPrisonerPlayers();

        switch (args[0].toLowerCase()){
            case "help": //Show help
                help(sender);
                break;
            case "create": //Create a BEDROCK jail
                createJail(jailLocation, 3, 2, Material.BEDROCK);
                break;
            case "list": //Show de player on the prisoners list
                sender.sendMessage(MessageUtils.colorMessage("&6&l", "The players on the prisoner list are:"));
                for(String p : prisionerPlayers){
                    sender.sendMessage(MessageUtils.colorMessage("&e&o", "- " + p));
                }
                break;
            case "free": //Free a player

                //The option free needs at least 2 arguments to be executed
                if(args.length < 2){
                    sender.sendMessage(MessageUtils.colorMessage("&c&l", "The option free needs at least two arguments."));
                    break;
                }

                //Searching the player
                prisioner = Bukkit.getPlayer(args[1]);
                if(prisioner == null){
                    sender.sendMessage(MessageUtils.colorMessage("&c&l", "The player is not online!"));
                    break;
                }

                //Control to ensure the player is on the prisoner list
                if(!prisionerPlayers.contains(prisioner.getName())){
                    sender.sendMessage(MessageUtils.colorMessage("&c&l", "The player is not on the prisoners list"));
                    break;
                }

                //Control to prevent a player from freeing themselves
                if(sender.getName().equalsIgnoreCase(args[0])){
                    sender.sendMessage(MessageUtils.colorMessage("&c&l", "You cannot free yourself!"));
                    break;
                }

                //Get the location of the target player Respawn
                Location spawn = prisioner.getRespawnLocation();

                //If the target respawn is no founded then the respawn is the world spawn
                if(spawn == null){
                    spawn = prisioner.getWorld().getSpawnLocation();
                }

                //Send a message to the target player
                prisioner.sendMessage(MessageUtils.colorMessage("&c&l", "You are going to be free!"));

                //Teleport the target player to his respawn
                prisioner.teleport(spawn);

                break;
            default:

                //Control to prevent a player from imprisoning themselves
                if(sender.getName().equalsIgnoreCase(args[0])){
                    sender.sendMessage(MessageUtils.colorMessage("&c&l", "You cannot send yourself to the jail!"));
                    break;
                }

                //Searching the player
                prisioner = Bukkit.getPlayer(args[0]);
                if(prisioner == null){
                    sender.sendMessage(MessageUtils.colorMessage("&c&l", "The player is not online!"));
                    break;
                }

                //Creating a jail
                createJail(jailLocation, 3, 2, Material.BEDROCK);

                //Send a message to the target player
                prisioner.sendMessage(MessageUtils.colorMessage("&c&l", "You are going to go to jail!"));

                //Teleport the target player to the jail
                prisioner.teleport(jailLocation);
                break;
        }
        return true;
    }

    public void help(CommandSender sender){
        sender.sendMessage(MessageUtils.colorMessage("&a&l&n", "Commands /jail /jl"));
        sender.sendMessage(MessageUtils.colorMessage("&a&o", "/jail help: Show the help guide"));
        sender.sendMessage(MessageUtils.colorMessage("&a&o", "/jail create: Create a jail in x = 300, y = 300 and z = 300"));
        sender.sendMessage(MessageUtils.colorMessage("&a&o", "/jail list: Show de prisoner list"));
        sender.sendMessage(MessageUtils.colorMessage("&a&o", "/jail [PlayerNickname]: Send a player to the jail"));
        sender.sendMessage(MessageUtils.colorMessage("&a&o", "/jail free [PlayerNickname]: Send a player to his spawn point"));
    }

    private static void createJail(Location location, int size, int thickness, Material material) {
        int outerRadius = size / 2 + thickness; //External radius
        int innerRadius = size / 2; //Internal radius (empty)

        //Iterate external radius
        for (int x = -outerRadius; x <= outerRadius; x++) {
            for (int y = -outerRadius; y <= outerRadius; y++) {
                for (int z = -outerRadius; z <= outerRadius; z++) {
                    Location blockLocation = location.clone().add(x, y, z);
                    int distanceToOuter = Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));

                    //Place the material if the block is in the outside layer but outside de inside layer
                    if (distanceToOuter <= outerRadius && distanceToOuter > innerRadius) {
                        Block block = blockLocation.getBlock();
                        if (!block.getType().equals(material)) {
                            block.setType(material);
                        }
                    }
                }
            }
        }

        //Place a torch in de middle on the floor
        Block block = location.clone().add(0, -1, 0).getBlock();
        if (!block.getType().equals(Material.TORCH)) {
            block.setType(Material.TORCH);
        }
    }
}
