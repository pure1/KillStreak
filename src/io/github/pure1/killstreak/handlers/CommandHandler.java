package io.github.pure1.killstreak.handlers;

import io.github.pure1.killstreak.killStreak;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
				System.out.println(ChatColor.GREEN + "[" + ChatColor.RED + killStreak.pluginName + ChatColor.GREEN + "]" + " Item "
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
				sendPrefixMessage(sender,"You have killed " + KillHandler.getKills((Player) sender) + " player(s).");
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
				sendPrefixMessage(sender, player.getDisplayName() + " has killed "
						+ KillHandler.getKills(player) + " player(s).");
				return;
			}
		}
		sendPrefixMessage(sender, "Sorry, " + string + " is not online, or has not killed anyone.");
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
			sendPrefixMessage(sender, "set tell-kills to " + Boolean.parseBoolean(args[3]));
			killStreak.config.saveConfig();
			break;
		case "broadcast-on-death":
			killStreak.config.setBoolean("broadcast-on-death", Boolean.parseBoolean(args[3]));
			sendPrefixMessage(sender, "set broadcast-on-death to " + Boolean.parseBoolean(args[3]));
			killStreak.config.saveConfig();
			break;
		case "save-streaks":
			killStreak.config.setBoolean("save-streaks", Boolean.parseBoolean(args[3]));
			sendPrefixMessage(sender, "set save-streaks to " + Boolean.parseBoolean(args[3]));
			killStreak.config.saveConfig();
			break;
		case "commands":
			sendPrefixMessage(sender, "please set commands through /ks config commands");
			break;
		default: configSetHelp(sender);
		}
	}



	private static void configGet(CommandSender sender, String[] args) {
		switch(args[2]){
		case "tell-kills":
			sendPrefixMessage(sender, "tell-kills is " + killStreak.config.getBoolean("tell-kills"));
			break;
		case "broadcast-on-death":
			sendPrefixMessage(sender, "broadcast-on-death is " + killStreak.config.getBoolean("broadcast-on-death"));
			break;
		case "save-streaks":
			sendPrefixMessage(sender, "save-streaks is " + killStreak.config.getBoolean("save-streaks"));
			break;
		case "commands":
			sendPrefixMessage(sender, "Current commands are:");
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
					 sendMessage(sender, i + ": " + map.get("" + i).toString());
				 }
			 }
			break;
		default: configGetHelp(sender);
		}
	}



	private static void configCommands(CommandSender sender, String[] args) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Config Help" + ChatColor.GREEN + "]");
		sendMessage(sender, "Sorry I havent got to this bit yet");
	}



	private static void configGetHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Config Help" + ChatColor.GREEN + "]");
		sendMessage(sender, "ussage: /ks config get [variable]");
		sendMessage(sender, "You can get:");
		sendMessage(sender, "commands");
		sendMessage(sender, "tell-kills");
		sendMessage(sender, "broadcast-on-death");
		sendMessage(sender, "save-streaks");
	}



	private static void configSetHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Config Help" + ChatColor.GREEN + "]");
		sendMessage(sender, "ussage: /ks config set [variable] [true/false]");
		sendMessage(sender, "You can set:");
		sendMessage(sender, "tell-kills");
		sendMessage(sender, "broadcast-on-death");
		sendMessage(sender, "save-streaks");
	}

	private static void configCommandsHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Config Help" + ChatColor.GREEN + "]");
		sendMessage(sender, ChatColor.AQUA + "/ks config commands " + ChatColor.YELLOW + "---------" + ChatColor.AQUA + " shows this");
		sendMessage(sender, ChatColor.AQUA + "/ks config commands set " + ChatColor.YELLOW + "-----" + ChatColor.AQUA + " set a command in the config");
		sendMessage(sender, ChatColor.AQUA + "/ks config commands get " + ChatColor.YELLOW + "-----" + ChatColor.AQUA + " get all commands in the config");
		sendMessage(sender, ChatColor.AQUA + "/ks config commands add " + ChatColor.YELLOW + "-----" + ChatColor.AQUA + " add a command to the config");
		sendMessage(sender, ChatColor.AQUA + "/ks config commands remove " + ChatColor.YELLOW + "--" + ChatColor.AQUA + " remove a command from the config");
		
	}

	private static void configHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Config Help" + ChatColor.GREEN + "]");
		sendMessage(sender, ChatColor.AQUA + "/ks config " + ChatColor.YELLOW + "-----------" + ChatColor.AQUA + " shows this");
		sendMessage(sender, ChatColor.AQUA + "/ks config set " + ChatColor.YELLOW + "-------" + ChatColor.AQUA + " set a variable in the config");
		sendMessage(sender, ChatColor.AQUA + "/ks config get " + ChatColor.YELLOW + "-------" + ChatColor.AQUA + " get a variable in the config");
		sendMessage(sender, ChatColor.AQUA + "/ks config commands " + ChatColor.YELLOW + "--" + ChatColor.AQUA + " show commands help");
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
					sendPrefixMessage(sender, "Sorry " + args[3] + " is not an integer.");
				}
				return;
			}
		}
		sendPrefixMessage(sender, "Sorry " + args[2] + " is not online");
	}



	private static void killsRemove(CommandSender sender, String[] args) {
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getDisplayName().equalsIgnoreCase(args[2])){
				KillHandler.removeKills(player);
				return;
			}
		}
		sendPrefixMessage(sender, "Sorry " + args[2] + " is not online");
	}



	private static void killsSet(CommandSender sender, String[] args) {
		for(Player player: Bukkit.getOnlinePlayers()){
			if(player.getDisplayName().equalsIgnoreCase(args[2])){
				try{
					KillHandler.setKills(player, Integer.parseInt(args[3]));
				}catch(Exception e){
					sendPrefixMessage(sender, "Sorry " + args[3] + " is not an integer.");
				}
				return;
			}
		}
		sendPrefixMessage(sender, "Sorry " + args[2] + " is not online");
	}


	private static void killsSetHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Kills Help" + ChatColor.GREEN + "]");
			sendMessage(sender, "ussage: /ks kills set [player] [kills]");
	}



	private static void killsRemoveHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Kills Help" + ChatColor.GREEN + "]");
			sendMessage(sender, "ussage: /ks kills remove [player]");
	}
	


	private static void killsAddHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Kills Help" + ChatColor.GREEN + "]");
		sendMessage(sender, "ussage: /ks kills add [player] [kills]");
	}



	private static void killsHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Kills Help" + ChatColor.GREEN + "]");
		sendMessage(sender, ChatColor.AQUA + "/ks kills " + ChatColor.YELLOW + "---------" + ChatColor.AQUA + " shows this");
		sendMessage(sender, ChatColor.AQUA + "/ks kills set " + ChatColor.YELLOW + "-----" + ChatColor.AQUA + " set someones kills");
		sendMessage(sender, ChatColor.AQUA + "/ks kills add " + ChatColor.YELLOW + "-----" + ChatColor.AQUA + " add to someones kills");
		sendMessage(sender, ChatColor.AQUA + "/ks kills remove " + ChatColor.YELLOW + "--" + ChatColor.AQUA + " remove someones kills");
	}

	private static void argsHelp(CommandSender sender) {
		sendPrefixMessage(sender, ChatColor.GREEN + "[" + ChatColor.RED + "Help" + ChatColor.GREEN + "]");
		sendMessage(sender, ChatColor.AQUA + "/ks " + ChatColor.YELLOW + "-----------" + ChatColor.AQUA + " show your kills");
		sendMessage(sender, ChatColor.AQUA + "/ks [player] " + ChatColor.YELLOW + "--" + ChatColor.AQUA + " show a players kills");
		sendMessage(sender, ChatColor.AQUA + "/ks kills " + ChatColor.YELLOW + "-----" + ChatColor.AQUA + " show kills help");
		sendMessage(sender, ChatColor.AQUA + "/ks config " + ChatColor.YELLOW + "----" + ChatColor.AQUA + " show config help");
		sendMessage(sender, ChatColor.AQUA + "/ks help " + ChatColor.YELLOW + "------" + ChatColor.AQUA + " show this");
	}

	public static void sendMessage(CommandSender sender, String msg){
		sender.sendMessage(ChatColor.AQUA + msg);
	}
	public static void sendPrefixMessage(CommandSender sender, String msg){
		sender.sendMessage(ChatColor.GREEN + "[" + ChatColor.RED + killStreak.pluginName + ChatColor.GREEN + "] " + ChatColor.AQUA + msg);
	}
	// if (cmd.getName().equalsIgnoreCase("killstreak")) {
	// if (args.length == 0) {
	// if (sender instanceof Player) {
	// sendMessage(sender, "[" + pluginName + "]"
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
