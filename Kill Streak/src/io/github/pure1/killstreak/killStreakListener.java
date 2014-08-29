package io.github.pure1.killstreak;

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
		Player p = e.getEntity().getPlayer();
		if(e.getEntity().getKiller() instanceof Player){
			KillHandler.addKill(e);
		}
		KillHandler.Death(e.getEntity());
	}
}