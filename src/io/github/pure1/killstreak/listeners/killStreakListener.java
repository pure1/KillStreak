package io.github.pure1.killstreak.listeners;

import io.github.pure1.killstreak.killStreak;
import io.github.pure1.killstreak.handlers.KillHandler;
import io.github.pure1.plugin.utils.ConfigAccessor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class killStreakListener implements Listener {

	public static ConfigAccessor config = killStreak.config;
	public killStreak ks;

	public killStreakListener(killStreak instance) {
		ks = instance;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(e.getEntity().getKiller() instanceof Player && e.getEntity().getKiller().hasPermission("killstreak.kill")){
			KillHandler.addKill(e);
		}
		KillHandler.Death(e.getEntity());
	}
}