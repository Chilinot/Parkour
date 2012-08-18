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

public class DatabaseManager {
	
	private ConsoleLogger logger;
	
	private final String host;
	private final int port;
	private final String database;
	private final String tablename;
	private final String username;
	private final String password;
	
	private Connection connection;
	
	public DatabaseManager(Main instance) {
		this.logger = new ConsoleLogger(instance, "DatabaseManager");
		
		this.host = instance.getConfig().getString("host");
		this.port = instance.getConfig().getInt("port");
		this.database = instance.getConfig().getString("database");
		this.tablename = instance.getConfig().getString("tablename");
		this.username = instance.getConfig().getString("username");
		this.password = instance.getConfig().getString("password");
		
		this.connection = getConnection();
		
		if(this.connection == null) {
			logger.severe("Could not connect to the datbase!");
		}
		
		logger.debug("Initiated");
	}
	
	private Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			
			return DriverManager.getConnection(url, username, password);
		}
		catch (SQLException | ClassNotFoundException e) {
			logger.severe("Exception caught while connecting to database! :: " + e.getMessage());
			return null;
		}
	}
	
	public void updateDatabase(String playername, int points) {
		new UpdateDatabaseThread(this.connection, this.tablename, playername, points);
	}
}

class UpdateDatabaseThread extends Thread {
	
	private final Connection connection;
	private final String tablename;
	private final String playername;
	private final int pointsToAdd;
	
	public UpdateDatabaseThread(Connection connection, String tablename, String playername, int points) {
		this.connection = connection;
		this.tablename = tablename;
		this.playername = playername;
		this.pointsToAdd = points;
		
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