/**
 *  Name: ParkourListener.java
 *  Date: 18:09:11 - 18 aug 2012
 * 
 *  Author: LucasEmanuel @ bukkit forums
 *  
 *  
 *  Description:
 *  
 *  
 *  
 * 
 * 
 */

package me.lucasemanuel.parkour;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ParkourListener implements Listener {
	
	private Main plugin;
	private ConsoleLogger logger;
	
	public ParkourListener(Main instance) {
		this.plugin = instance;
		this.logger = new ConsoleLogger(instance, "EventListener");
		
		logger.debug("Initiated");
	}
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onInteract(PlayerInteractEvent event) {
		
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		if((block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) 
				&& block.getWorld().getName().equals(this.plugin.getConfig().getString("worldname"))) {
			
			Sign sign = (Sign) block.getState();
			
			String firstline = sign.getLine(0).toLowerCase();
			if(firstline.equals("[parkour]")) {
				
				int points = Integer.parseInt(sign.getLine(1));
				
				this.plugin.getDatabaseManager().updateDatabase(player.getName(), points);
				
				player.teleport(player.getWorld().getSpawnLocation());
				
				player.sendMessage(ChatColor.GREEN + "Grattis! Du fick " + ChatColor.LIGHT_PURPLE + points + ChatColor.GREEN + " poäng!");
			}
		}
	}
}
