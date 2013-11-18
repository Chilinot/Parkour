package me.lucasemanuel.parkour;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
	public static void load(JavaPlugin plugin) {
		
		@SuppressWarnings("serial")
		HashMap<String, Object> defaults = new HashMap<String, Object>() {{
			put("debug", false);
			put("worldname", "world");
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
