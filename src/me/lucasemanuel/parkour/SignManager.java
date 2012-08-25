/**
 *  Name: SignManager.java
 *  Date: 23:52:45 - 19 aug 2012
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import me.lucasemanuel.parkour.utils.ConsoleLogger;
import me.lucasemanuel.parkour.utils.SLAPI;
import me.lucasemanuel.parkour.utils.SerializedLocation;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class SignManager {

	private Main plugin;
	private ConsoleLogger logger;

	@SuppressWarnings("serial")
	public final HashSet<String> signnames = new HashSet<String>() {
		{
			add("[pkname1]");
			add("[pkname2]");
			add("[pkpoints1]");
			add("[pkpoints2]");
		}
	};

	private HashMap<String, Location> signs;
	private final String DATLOCATION = "plugins/Parkour/signs.dat";

	public SignManager(Main instance) {
		this.plugin = instance;
		this.logger = new ConsoleLogger(instance, "SignManager");

		this.signs = loadSigns();

		logger.debug("Initiated");
	}

	public boolean addSign(String name, Location location) {

		if (this.signnames.contains(name)) {
			this.signs.put(name, location);
			saveSigns();
			return true;
		}

		return false;
	}

	public void updateSigns() {

		String[][] stats = this.plugin.getDatabaseManager().getLatestStats();

		for (int i = 1; i <= 2; i++) {

			String namesignName = "[pkname" + i + "]";
			String pointsignName = "[pkpoints" + i + "]";

			Sign nameSign = (Sign) this.signs.get(namesignName).getBlock().getState();
			Sign pointSign = (Sign) this.signs.get(pointsignName).getBlock().getState();

			if (nameSign != null && pointSign != null) {

				// Clear the signs
				for (int j = 0; j < 4; j++) {
					nameSign.setLine(j, "");
					pointSign.setLine(j, "");
				}

				int startpoint = i == 1 ? 0 : 4;

				for (int j = 0; j < 4; j++) {
					nameSign.setLine(j, stats[startpoint][0]);
					pointSign.setLine(j, stats[startpoint][1]);

					startpoint++;
				}

				nameSign.update();
				pointSign.update();
			}
			else
				break;
		}
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Location> loadSigns() {

		HashMap<String, SerializedLocation> savedsigns = null;

		try {
			savedsigns = (HashMap<String, SerializedLocation>) SLAPI.load(this.DATLOCATION);
		}
		catch (Exception e) {
			logger.severe(e.getMessage());
			return new HashMap<String, Location>();
		}

		HashMap<String, Location> newmap = new HashMap<String, Location>();

		for (Entry<String, SerializedLocation> entry : savedsigns.entrySet()) {
			newmap.put(entry.getKey(), entry.getValue().deserialize());
		}

		return newmap;
	}

	private void saveSigns() {
		HashMap<String, SerializedLocation> tempmap = new HashMap<String, SerializedLocation>();

		for (Entry<String, Location> entry : this.signs.entrySet()) {
			tempmap.put(entry.getKey(), new SerializedLocation(entry.getValue()));
		}

		try {
			SLAPI.save(tempmap, this.DATLOCATION);
		}
		catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}
