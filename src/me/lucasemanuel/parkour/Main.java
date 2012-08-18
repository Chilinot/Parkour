/**
 *  Name: Main.java
 *  Date: 17:52:00 - 18 aug 2012
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

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private DatabaseManager databasemanager;
	
	public void onEnable() {
		Config.load(this);
		
		this.getServer().getPluginManager().registerEvents(new ParkourListener(this), this);
		
		this.databasemanager = new DatabaseManager(this);
	}
	
	public DatabaseManager getDatabaseManager() {
		return this.databasemanager;
	}
}
