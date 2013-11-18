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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import se.lucasarnstrom.lucasutils.ConsoleLogger;

public class ParkourListener implements Listener {

	private Main plugin;
	private ConsoleLogger logger;

	public ParkourListener(Main instance) {
		this.plugin = instance;
		this.logger = new ConsoleLogger("Listener");
		
		logger.debug("Intitated");
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		if((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
				&& block.getWorld().getName().equals(this.plugin.getConfig().getString("worldname"))) {
			
			Sign sign = (Sign) block.getState();
			
			if(sign.getLine(0).equalsIgnoreCase("[parkour]")) {
				
			}
		}
	}
}
