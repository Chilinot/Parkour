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

import me.lucasemanuel.parkour.utils.Config;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private DatabaseManager databasemanager;
	private SignManager signmanager;

	public void onEnable() {
		Config.load(this);

		this.getServer().getPluginManager().registerEvents(new ParkourListener(this), this);

		this.databasemanager = new DatabaseManager(this);
		this.signmanager = new SignManager(this);
	}

	public DatabaseManager getDatabaseManager() {
		return this.databasemanager;
	}

	public SignManager getSignManager() {
		return this.signmanager;
	}
}
