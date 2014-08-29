/*
 * If my code looks messy or something like that, it's my code, it's like handwriting.
 * We all code differently  and this is how I code.
 * If there is anything that really could be done better, I welcome suggestions.
 */

package io.github.pure1.killstreak;

import io.github.pure1.killstreak.handlers.CommandHandler;
import io.github.pure1.killstreak.handlers.KillHandler;
import io.github.pure1.killstreak.listeners.killStreakListener;
import io.github.pure1.plugin.utils.ConfigAccessor;
import io.github.pure1.plugin.utils.Metrics;

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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("KillStreak"))
			CommandHandler.command(sender, args);
		return false;
	}
}
