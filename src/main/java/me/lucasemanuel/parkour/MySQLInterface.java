package me.lucasemanuel.parkour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import se.lucasarnstrom.lucasutils.ConsoleLogger;

public class MySQLInterface {
	
	private ConsoleLogger logger;
	
	private final String URL;
	private final String TABLENAME;
	private final String USERNAME;
	private final String PASSWORD;

	public MySQLInterface(Main instance) {
		logger = new ConsoleLogger("MySQLInterface");
		
		FileConfiguration config = instance.getConfig();
		
		this.URL = "jdbc:mysql://" + config.getString("database.host") + 
				":" + config.getInt("database.port") + 
				"/" + config.getString("database.database");
		
		this.TABLENAME = config.getString("database.tablename");
		this.USERNAME  = config.getString("database.username");
		this.PASSWORD  = config.getString("database.password");
		
		logger.debug("Initiated");
	}
	
	public void updateDatabase(String playername, int points) {
		logger.debug("Updating stats for player=\"" + playername + "\" points=" + points);
		new UpdateDatabaseThread(URL, TABLENAME, USERNAME, PASSWORD, playername, points);
	}
	
	private class UpdateDatabaseThread extends Thread {
		
		private final String CREATE_TABLE_S;
		private final String UPDATE_S;
		
		private final String URL;
		private final String USERNAME;
		private final String PASSWORD;
		
		private final String PLAYERNAME;
		private final int    POINTS;

		public UpdateDatabaseThread(String url,
				String tablename,
				String username, 
				String password,
				String playername, 
				int points) {
			
			this.CREATE_TABLE_S = "CREATE TABLE IF NOT EXISTS " + tablename + " ("
					+ "playername VARCHAR(255) PRIMARY KEY NOT NULL, "
					+ "points INT(255))";
			
			this.UPDATE_S = "INSERT INTO " + tablename + " VALUES( ? , ? ) "
					+ "ON DUPLICATE KEY UPDATE points=points + ?";
			
			this.URL        = url;
			this.USERNAME   = username;
			this.PASSWORD   = password;
			
			this.PLAYERNAME = playername;
			this.POINTS     = points;
			
			start();
		}
		
		public void run() {
			try {
				Connection con = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
				
				// Creates the stats-table if it doesn't exist in the database.
				PreparedStatement create_table = con.prepareStatement(CREATE_TABLE_S);
				create_table.execute();
				create_table.close();
				
				// Insert or update the stats.
				PreparedStatement insert_update = con.prepareStatement(UPDATE_S);
				insert_update.setString(1, this.PLAYERNAME);
				insert_update.setInt(   2, this.POINTS);
				insert_update.setInt(   3, this.POINTS);
				insert_update.executeUpdate();
				insert_update.close();
				
				con.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
