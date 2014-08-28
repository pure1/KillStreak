/*
 * If my code looks messy or something like that, it's my code, it's like handwriting.
 * We all code differently  and this is how I code.
 * If there is anything that really could be done better, I welcome suggestions.
 */

package io.github.pure1.killstreak;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class killStreak extends JavaPlugin {

	/*
	 * This update:
	 */

	/*
	 * TODO
	 * 
	 * COMMAND TO SET STREAKS INGAME.
	 */

	/*
	 * SET TRUE ON RELEASE COMPILE
	 */
	public static final boolean released = false;

	// Variables
	public final killStreakListener psl = new killStreakListener(this);
	public final Logger logger = Logger.getLogger("Minecraft");
	public final static String pluginName = "KillStreak";
	public static ConfigAccessor config;
	public static ConfigAccessor kills;

	@Override
	// when plugin is enabled
	public void onEnable() {

		// config stuff thanks to someone who I can't remember.
		config = new ConfigAccessor(this, "config.yml");
		config.saveDefaultConfig();
		config.getConfig();
		kills = new ConfigAccessor(this, "kills.yml");
		kills.saveDefaultConfig();
		kills.getConfig();
		PluginManager pm = getServer().getPluginManager();
		config.saveConfig();
		kills.saveConfig();
		KillHandler.loadKills();
		pm.registerEvents(this.psl, this);
		// Initiate variables.
		// ###//

		if (released) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				// Failed to submit the stats :-(
			}
		}

		this.logger.info("[" + pluginName + "] " + pluginName + " Loaded.");

	}

	@Override
	// when plugin is disabled
	public void onDisable() {
		KillHandler.saveKills();
		kills.saveConfig();
		this.logger.info("[" + pluginName + "] " + pluginName + " Disabled.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("killstreak")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					sender.sendMessage("[" + pluginName + "]"
							+ " You have killed "
							+ KillHandler.getKills((Player) sender)
							+ " player(s).");
				}
			}
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("config")) {
					if (args.length > 1)
						if (args[1].equalsIgnoreCase("get")) {
							if (args.length > 2) {
								System.out.println("poe");
								if (args[2].equalsIgnoreCase("commands")) {
									// /current commands
									Map<String, Object> map = config
											.getHashMap("commands");
									Object[] a = map.keySet().toArray();
									int last = 0;
									int l = 0;
									for (int i = 0; i < a.length; i++) {
										if (Integer.parseInt(a[i].toString()) > last) {
											last = Integer.parseInt(a[i]
													.toString());
											l = i;
										}
									}
									for (int i = 0; i <= Integer.parseInt(a[l]
											.toString()); i++) {
										if (!(map.get("" + i) == null)) {
											sender.sendMessage(i
													+ ": "
													+ map.get("" + i)
															.toString());
										}
									}
									return false;
								}
							}
						}
				}

			}
		}
		return false;
	}
}
