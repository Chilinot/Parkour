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
import org.bukkit.inventory.ItemStack;

import se.lucasarnstrom.lucasutils.ConsoleLogger;

public class ParkourListener implements Listener {

	private Main plugin;
	private ConsoleLogger logger;

	public ParkourListener(Main instance) {
		this.plugin = instance;
		this.logger = new ConsoleLogger("Listener");
		
		logger.debug("Intitated");
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		if((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
				&& plugin.getConfig().getStringList("worldnames").contains(block.getWorld().getName())) {
			
			Sign sign = (Sign) block.getState();
			
			String line_0 = sign.getLine(0);
			if(plugin.getConfig().getStringList("signs").contains(line_0)) {
				
				int points  = 0;
				int tickets = 0;
				try {
					points  = Integer.parseInt(sign.getLine(1));
					tickets = Integer.parseInt(sign.getLine(2));
				}
				catch(NumberFormatException e) {
					logger.severe("Error while trying to get amount of points from sign! Line 1 = \"" + sign.getLine(1) + "\"");
					e.printStackTrace();
					return;
				}
				
				player.sendMessage("Grattis! Du har fått " + ChatColor.GOLD + points 
						+ ChatColor.WHITE + " poäng och " + ChatColor.GOLD + tickets + ChatColor.WHITE + " stycken biljetter!");
				
				// Tickets
				if(tickets > 0) {
					player.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, tickets));
					player.updateInventory();
				}
				
				// Points
				plugin.getMySQLInterface().updateDatabase(player.getName(), points, line_0.toLowerCase().replace("[", "").replace("]", ""));
				
				// Back to spawn
				player.teleport(player.getWorld().getSpawnLocation());
			}
		}
	}
}
