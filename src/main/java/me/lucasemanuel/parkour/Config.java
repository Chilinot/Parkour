package me.lucasemanuel.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
	public static void load(JavaPlugin plugin) {
		
		@SuppressWarnings("serial")
		HashMap<String, Object> defaults = new HashMap<String, Object>() {{
			put("debug", false);
			
			put("worldnames", new ArrayList<String>(){{
				add("world");
			}});
			
			put("signs",      new ArrayList<String>(){{
				add("[parkour]");
				add("[pussel]");
			}});
			
			// Database
			put("database.host",      "localhost");
			put("database.port",      3306);
			put("database.database",  "parkour");
			put("database.username",  "username");
			put("database.password",  "password");
		}};
		
		
		FileConfiguration config = plugin.getConfig();
		
		boolean save = false;
		for(Entry<String, Object> entry : defaults.entrySet()) {
			if(!config.contains(entry.getKey())) {
				config.set(entry.getKey(), entry.getValue());
				save = true;
			}
		}
		
		if(save) {
			plugin.saveConfig();
		}
	}
}
