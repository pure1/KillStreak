package io.github.pure1.killstreak.handlers;

import io.github.pure1.killstreak.killStreak;

import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class KillHandler {

	private static Map<String, Object> kills;
	private static Map<String, Object> commands;
	public static void loadKills(){
		commands = killStreak.config.getHashMap("commands");
		if(kills == null){
			kills = killStreak.kills.getHashMap("kills");
		}
	}
	
	public static void addKill(PlayerDeathEvent e) {
		Player killer = e.getEntity().getKiller();
		UUID pid = killer.getUniqueId();
		kills.put(pid.toString(), getKills(killer)+1);
		if(killStreak.config.getBoolean("tell-kills"))
			killer.sendMessage("[" + killStreak.pluginName + "] " + " You have " + getKills(killer) + " kill(s).");
		rewardPlayer(e);
	}
	public static void addKills(OfflinePlayer player, int k) {
		UUID pid = player.getUniqueId();
		kills.put(pid.toString(), getKills(player)+k);
		if(killStreak.config.getBoolean("tell-kills"))
			try{
				player.getPlayer().sendMessage("[" + killStreak.pluginName + "] " + " Your kills have been set to " + getKills(player));
			}catch(NullPointerException e){}
	}
	public static void setKills(OfflinePlayer player, int k) {
		UUID pid = player.getUniqueId();
		kills.put(pid.toString(), k);
		if(killStreak.config.getBoolean("tell-kills"))
		try{
			player.getPlayer().sendMessage("[" + killStreak.pluginName + "] " + " Your kills have been set to " + getKills(player));
		}catch(NullPointerException e){}
	}

	private static void rewardPlayer(PlayerDeathEvent e) {
		Player killer = e.getEntity().getKiller();
		int kills = getKills(killer);
		System.out.println(commands.get("1"));
		try{
			String command = commands.get("" + kills).toString();
			CommandHandler.stringToCommand(command, e);
		}catch(NullPointerException ex){
			ex.printStackTrace();
		}
	}

	public static void Death(Player entity) {
		if(killStreak.config.getBoolean("broadcast-on-death"))
			entity.getServer().broadcastMessage("[" + killStreak.pluginName + "] " + entity.getDisplayName() + " died with " + getKills(entity) + " kill(s).");
		kills.remove(entity.getUniqueId().toString());
	}

	public static int getKills(OfflinePlayer player) {
		UUID pid = player.getUniqueId();
		try{
			return Integer.parseInt(kills.get(pid.toString()).toString());
		}catch(NullPointerException e){
			return 0;
		}
	}
	public static void saveKills(){
		killStreak.kills.setHashMap("kills", kills);
	}

	public static void removeKills(OfflinePlayer player) {
		kills.remove(player.getUniqueId().toString());
		try{
			player.getPlayer().sendMessage("[" + killStreak.pluginName + "] " + " Your kills have been set to " + getKills(player));
		}catch(NullPointerException e){}
	}

}
