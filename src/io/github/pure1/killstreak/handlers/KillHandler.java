package io.github.pure1.killstreak.handlers;

import io.github.pure1.killstreak.killStreak;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
	public static void addKills(Player player, int k) {
		UUID pid = player.getUniqueId();
		kills.put(pid.toString(), getKills(player)+k);
		if(killStreak.config.getBoolean("tell-kills"))
			player.sendMessage("[" + killStreak.pluginName + "] " + " Your kills have been set to " + getKills(player));
	}
	public static void setKills(Player player, int k) {
		UUID pid = player.getUniqueId();
		kills.put(pid.toString(), k);
		if(killStreak.config.getBoolean("tell-kills"))
			player.sendMessage("[" + killStreak.pluginName + "] " + " Your kills have been set to " + getKills(player));
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

	public static int getKills(Player player) {
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

	public static void removeKills(Player player) {
		kills.remove(player.getUniqueId().toString());
		player.sendMessage("[" + killStreak.pluginName + "] " + " Your kills have been set to " + getKills(player));
	}

}
