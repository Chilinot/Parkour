/**
 *  Name: DatabaseManager.java
 *  Date: 18:21:21 - 18 aug 2012
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.lucasemanuel.parkour.utils.ConsoleLogger;

public class DatabaseManager {
	
	private ConsoleLogger logger;
	
	private final String host;
	private final int port;
	private final String database;
	private final String tablename;
	private final String username;
	private final String password;
	
	public DatabaseManager(Main instance) {
		this.logger = new ConsoleLogger(instance, "DatabaseManager");
		
		this.host = instance.getConfig().getString("host");
		this.port = instance.getConfig().getInt("port");
		this.database = instance.getConfig().getString("database");
		this.tablename = instance.getConfig().getString("tablename");
		this.username = instance.getConfig().getString("username");
		this.password = instance.getConfig().getString("password");
		
		logger.debug("Initiated");
	}
	
	/*
	 * Yes this should have its own thread! Fuck you, im lazy!
	 */
	public String[][] getLatestStats() {
		
		String[][] stats = new String[8][2];
		
		Connection con = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, username, password);
		}
		catch (SQLException | ClassNotFoundException e) {
			logger.severe(e.getMessage());
			return stats;
		}
		
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM " + this.tablename + " ORDER BY points DESC LIMIT 0,8");
			
			int j = 0;
			while(rs.next() && j < 8) {
				stats[j] = new String[] {rs.getString("playername"), "" + rs.getInt("points")};
				j++;
			}
		}
		catch(SQLException e) {
			logger.severe("SQLException while getting leaderboard: " + e.getMessage());
		}
		
		return stats;
	}
	
	public void updateDatabase(String playername, int points) {
		new UpdateDatabaseThread("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password, this.tablename, playername, points);
	}
}

class UpdateDatabaseThread extends Thread {
	
	private Connection connection;
	private final String tablename;
	private final String playername;
	private final int pointsToAdd;
	
	public UpdateDatabaseThread(String url, String username, String password, String tablename, String playername, int points) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			this.connection = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		this.tablename = tablename;
		this.playername = playername;
		this.pointsToAdd = points;
		
		if(this.connection != null)
			start();
	}
	
	public void run() {
		
		try {
			Statement stmt = this.connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.tablename + " WHERE playername='" + playername + "'");
			
			if(rs.next() && rs.getString("playername") != null) {
				int points = rs.getInt("points");
				stmt.executeUpdate("UPDATE " + this.tablename + " SET points=" + (points + this.pointsToAdd) + " WHERE playername='" + this.playername + "'");
			}
			else {
				stmt.executeUpdate("INSERT INTO " + this.tablename + " VALUES('" + playername + "', " + this.pointsToAdd + ")");
			}
			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}