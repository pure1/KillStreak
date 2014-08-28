package io.github.pure1.killstreak;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CommandHandler {

	public static void stringToCommand(String command, PlayerDeathEvent e) {
			command.replace("&name", e.getEntity().getKiller().getDisplayName());
			command.replace("&kills", "1"+ KillHandler.getKills(e.getEntity().getKiller()));
//			command.replace("&rName", e.getEntity().getServer().getOnlinePlayers);
			String[] commands = command.split("&cmd");
			for(String c : commands){
				if(command.contains("ksd")){
					String[] d = c.split(" ");
					ItemStack i = new ItemStack(Material.getMaterial(d[1].toUpperCase()), Integer.parseInt(d[2]));
					e.getEntity().getWorld().dropItemNaturally(e.getEntity().getEyeLocation().add(0d, 1d, 0d), i);
					System.out.println("[" + killStreak.pluginName + "]" + " Item " + i.getType().name() + " dropped");
				}else{
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), c);
				}
			}
	}

}
