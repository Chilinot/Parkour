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

import se.lucasarnstrom.lucasutils.ConsoleLogger;

public class Main extends JavaPlugin {
	
	private ConsoleLogger logger;
	
	private MySQLInterface mysql;

	public void onEnable() {
		Config.load(this);
		ConsoleLogger.init(this);
		
		this.logger = new ConsoleLogger("Main");
		
		logger.debug("Initiating MySQL-driver");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		logger.debug("Initiating managers...");
		this.mysql = new MySQLInterface(this);

		logger.debug("Registering eventlisteners...");
		this.getServer().getPluginManager().registerEvents(new ParkourListener(this), this);
		
		logger.debug("Plugin is initiated!");
	}
	
	public MySQLInterface getMySQLInterface() {
		return mysql;
	}
}
