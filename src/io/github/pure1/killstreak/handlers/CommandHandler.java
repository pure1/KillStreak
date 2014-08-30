package io.github.pure1.killstreak.handlers;

import io.github.pure1.killstreak.killStreak;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CommandHandler {

	public static void stringToCommand(String command, PlayerDeathEvent e) {
		command.replace("&name", e.getEntity().getKiller().getDisplayName());
		command.replace("&kills",
				"1" + KillHandler.getKills(e.getEntity().getKiller()));
		// command.replace("&rName",
		// e.getEntity().getServer().getOnlinePlayers);
		String[] commands = command.split("&cmd");
		for (String c : commands) {
			if (command.contains("ksd")) {
				String[] d = c.split(" ");
				ItemStack i = new ItemStack(Material.getMaterial(d[1]
						.toUpperCase()), Integer.parseInt(d[2]));
				e.getEntity()
						.getWorld()
						.dropItemNaturally(
								e.getEntity().getEyeLocation().add(0d, 1d, 0d),
								i);
				System.out.println("[" + killStreak.pluginName + "]" + " Item "
						+ i.getType().name() + " dropped");
			} else {
				Bukkit.getServer()
						.dispatchCommand(Bukkit.getConsoleSender(), c);
			}
		}
	}



	public static void command(CommandSender sender, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player && sender.hasPermission("killstreak.killstreak")) {
				sender.sendMessage("[" + killStreak.pluginName + "]"
						+ " You have killed "
						+ KillHandler.getKills((Player) sender) + " player(s).");
			}
			return;
		}
		switch (args[0]) {
		case "help":
			if(sender.hasPermission("killstreak.help"))
				argsHelp(sender);
			break;
		case "kills":
			argsKills(sender, args);
			break;
		case "config":
			argsConfig(sender, args);
			break;
		default:
			if(sender.hasPermission("killstreak.player"))
				argsPlayer(sender, args[0]);
		}

	}

	private static void argsPlayer(CommandSender sender, String string) {
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getDisplayName().equalsIgnoreCase(string)){
				sender.sendMessage("[" + killStreak.pluginName + "]"
						+ player.getDisplayName() + " has killed "
						+ KillHandler.getKills(player) + " player(s).");
				return;
			}
		}
		sender.sendMessage("[" + killStreak.pluginName + "] Sorry, " + string + " is not online, or has not killed anyone.");
	}

	/*
	 * 
	 * KillStreak:
	 * 	#playername 
	 * 	help 
	 * 	kills: 
	 * 		set 
	 * 		remove 
	 * 		add 
	 * 	config: 
	 * 		set  
	 * 		get
	 * 		commands:
	 * 			get
	 * 			set
	 * 			add
	 * 			remove
	 */
	private static void argsConfig(CommandSender sender, String[] args) {
		if(args.length == 1){
			if(sender.hasPermission("killstreak.config.help"))
				configHelp(sender);
			return;
		}
		if(args.length == 2){
			switch(args[1]){
			case "set": 
				if(sender.hasPermission("killstreak.config.set"))
					configSetHelp(sender);
				break;
			case "get": 
				if(sender.hasPermission("killstreak.config.get"))
					configGetHelp(sender);
				break;
			case "commands": 
				if(sender.hasPermission("killstreak.config.commands.help"))
				configCommandsHelp(sender);
				break;
			default:
				if(sender.hasPermission("killstreak.config.help"))
					configHelp(sender);
			}
		}else{
			switch(args[1]){
			case "set":
				if(sender.hasPermission("killstreak.config.set"))
					configSet(sender, args);
				break;
			case "get":
				if(sender.hasPermission("killstreak.config.get"))
					configGet(sender, args);
				break;
			case "commands":	
				configCommands(sender, args);
				break;
			default: 
				if(sender.hasPermission("killstreak.config.help"))
					configHelp(sender);
			}
		}
	}
	
	private static void configSet(CommandSender sender, String[] args) {
		
		switch(args[2]){
		case "tell-kills":
			killStreak.config.setBoolean("tell-kills", Boolean.parseBoolean(args[3]));
			sender.sendMessage("[" + killStreak.pluginName + "] set tell-kills to " + Boolean.parseBoolean(args[3]));
			killStreak.config.saveConfig();
			break;
		case "broadcast-on-death":
			killStreak.config.setBoolean("broadcast-on-death", Boolean.parseBoolean(args[3]));
			sender.sendMessage("[" + killStreak.pluginName + "] set broadcast-on-death to " + Boolean.parseBoolean(args[3]));
			killStreak.config.saveConfig();
			break;
		case "save-streaks":
			killStreak.config.setBoolean("save-streaks", Boolean.parseBoolean(args[3]));
			sender.sendMessage("[" + killStreak.pluginName + "] set save-streaks to " + Boolean.parseBoolean(args[3]));
			killStreak.config.saveConfig();
			break;
		case "commands":
			sender.sendMessage("[" + killStreak.pluginName + "] please set commands through /ks config commands");
			break;
		default: configSetHelp(sender);
		}
	}



	private static void configGet(CommandSender sender, String[] args) {
		switch(args[2]){
		case "tell-kills":
			sender.sendMessage("[" + killStreak.pluginName + "]  tell-kills is " + killStreak.config.getBoolean("tell-kills"));
			break;
		case "broadcast-on-death":
			sender.sendMessage("[" + killStreak.pluginName + "]  broadcast-on-death is " + killStreak.config.getBoolean("broadcast-on-death"));
			break;
		case "save-streaks":
			sender.sendMessage("[" + killStreak.pluginName + "]  save-streaks is " + killStreak.config.getBoolean("save-streaks"));
			break;
		case "commands":
			sender.sendMessage("[" + killStreak.pluginName + "] Current commands are:");
			 Map<String, Object> map = killStreak.config.getHashMap("commands");
			 Object[] a = map.keySet().toArray();
			 int last = 0;
			 int l = 0;
			 for (int i = 0; i < a.length; i++) {
				 if (Integer.parseInt(a[i].toString()) > last) {
					 last = Integer.parseInt(a[i].toString());
					 l = i;
				 }
			 }
			 for (int i = 0; i <= Integer.parseInt(a[l].toString()); i++) {
				 if (!(map.get("" + i) == null)) {
					 sender.sendMessage(i + ": " + map.get("" + i).toString());
				 }
			 }
			break;
		default: configGetHelp(sender);
		}
	}



	private static void configCommands(CommandSender sender, String[] args) {
		sender.sendMessage("[" + killStreak.pluginName + " config help" + "]");
		sender.sendMessage("Sorry I havent got to this bit yet");
	}



	private static void configGetHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " config help" + "]");
		sender.sendMessage("ussage: /ks config get [variable]");
		sender.sendMessage("You can get:");
		sender.sendMessage("commands");
		sender.sendMessage("tell-kills");
		sender.sendMessage("broadcast-on-death");
		sender.sendMessage("save-streaks");
	}



	private static void configSetHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " config help" + "]");
		sender.sendMessage("ussage: /ks config set [variable] [true/false]");
		sender.sendMessage("You can set:");
		sender.sendMessage("tell-kills");
		sender.sendMessage("broadcast-on-death");
		sender.sendMessage("save-streaks");
	}

	private static void configCommandsHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " config help" + "]");
		sender.sendMessage("/ks config commands --------- shows this");
		sender.sendMessage("/ks config commands set ----- set a command in the config");
		sender.sendMessage("/ks config commands get ----- get all commands in the config");
		sender.sendMessage("/ks config commands add ----- add a command to the config");
		sender.sendMessage("/ks config commands remove -- remove a command from the config");
		
	}

	private static void configHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " config help" + "]");
		sender.sendMessage("/ks config ----------- shows this");
		sender.sendMessage("/ks config set ------- set a variable in the config");
		sender.sendMessage("/ks config get ------- get a variable in the config");
		sender.sendMessage("/ks config commands -- show commands help");
	}
	
	private static void argsKills(CommandSender sender, String[] args) {
		if(args.length == 1){
			if(sender.hasPermission("killstreak.kills.help"))
				killsHelp(sender);
			return;
		}
		if(args.length == 2){
			switch(args[1]){
			case "set": 
				if(sender.hasPermission("killstreak.kills.set"))
					killsSetHelp(sender);
				break;
			case "remove":
				if(sender.hasPermission("killstreak.kills.remove"))
					killsRemoveHelp(sender);
				break;
			case "add":
				if(sender.hasPermission("killstreak.kills.add"))
					killsAddHelp(sender);
				break;
			default: 
				if(sender.hasPermission("killstreak.kills.help"))
				killsHelp(sender);
			}
		}else{
			switch(args[1]){
			case "set":
				if(sender.hasPermission("killstreak.kills.set"))
					killsSet(sender, args);
				break;
			case "remove":
				if(sender.hasPermission("killstreak.kills.remove"))
					killsRemove(sender, args);
				break;
			case "add": 
				if(sender.hasPermission("killstreak.kills.add"))
					killsAdd(sender, args);
				break;
			default:
				if(sender.hasPermission("killstreak.kills.help"))
					killsHelp(sender);
			}
		}
	}
	
	private static void killsAdd(CommandSender sender, String[] args) {
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getDisplayName().equalsIgnoreCase(args[2])){
				try{
					KillHandler.addKills(player, Integer.parseInt(args[3]));
				}catch(Exception e){
					sender.sendMessage("[" + killStreak.pluginName + "] Sorry " + args[3] + " is not an integer.");
				}
				return;
			}
		}
		sender.sendMessage("[" + killStreak.pluginName + "] Sorry " + args[2] + " is not online");
	}



	private static void killsRemove(CommandSender sender, String[] args) {
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getDisplayName().equalsIgnoreCase(args[2])){
				KillHandler.removeKills(player);
				return;
			}
		}
		sender.sendMessage("[" + killStreak.pluginName + "] Sorry " + args[2] + " is not online");
	}



	private static void killsSet(CommandSender sender, String[] args) {
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getDisplayName().equalsIgnoreCase(args[2])){
				try{
					KillHandler.setKills(player, Integer.parseInt(args[3]));
				}catch(Exception e){
					sender.sendMessage("[" + killStreak.pluginName + "] Sorry " + args[3] + " is not an integer.");
				}
				return;
			}
		}
		sender.sendMessage("[" + killStreak.pluginName + "] Sorry " + args[2] + " is not online");
	}


	private static void killsSetHelp(CommandSender sender) {
			sender.sendMessage("[" + killStreak.pluginName + " kills help" + "]");
			sender.sendMessage("ussage: /ks kills set [player] [kills]");
	}



	private static void killsRemoveHelp(CommandSender sender) {
			sender.sendMessage("[" + killStreak.pluginName + " kills help" + "]");
			sender.sendMessage("ussage: /ks kills remove [player]");
	}
	


	private static void killsAddHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " kills help" + "]");
		sender.sendMessage("ussage: /ks kills add [player] [kills]");
	}



	private static void killsHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " kills help" + "]");
		sender.sendMessage("/ks kills --------- shows this");
		sender.sendMessage("/ks kills set ----- set someones kills");
		sender.sendMessage("/ks kills add ----- add to someones kills");
		sender.sendMessage("/ks kills remove -- remove someones kills");
	}

	private static void argsHelp(CommandSender sender) {
		sender.sendMessage("[" + killStreak.pluginName + " Help" + "]");
		sender.sendMessage("/ks  ---------- show your kills");
		sender.sendMessage("/ks [player] -- show a players kills");
		sender.sendMessage("/ks kills ----- show kills help");
		sender.sendMessage("/ks config ---- show config help");
		sender.sendMessage("/ks help ------ show this");
	}

	// if (cmd.getName().equalsIgnoreCase("killstreak")) {
	// if (args.length == 0) {
	// if (sender instanceof Player) {
	// sender.sendMessage("[" + pluginName + "]"
	// + " You have killed "
	// + KillHandler.getKills((Player) sender)
	// + " player(s).");
	// }
	// }
	// if (args.length > 0) {
	// if (args[0].equalsIgnoreCase("config")) {
	// if (args.length > 1)
	// if (args[1].equalsIgnoreCase("get")) {
	// if (args.length > 2) {
	// System.out.println("poe");
	// if (args[2].equalsIgnoreCase("commands")) {
	// // /current commands

	// return false;
	// }
	// }
	// }
	// }
	//
	// }
	// }

}
