package eu.horyzon.cratesexplorer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import eu.horyzon.cratesexplorer.commands.Commands;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import eu.horyzon.cratesexplorer.utils.FileManager;

public class CratesExplorer extends JavaPlugin {
	private static CratesExplorer instance;

	public static Map<UUID, String> modify = new HashMap<UUID, String>();

	public void onEnable() {
		instance = this;
		Configuration config = getConfig();

		// INITIALIZE FILES
		File[] dirs = (File[]) Arrays
				.asList(new File(getDataFolder(), "containers"), new File(getDataFolder(), "armorstands")).toArray();

		for (File dir : dirs) {
			new FileManager(dir).setup();
		}

		if (config.getBoolean("debug", false)) {
			getCommand("lobbyexplorer").setExecutor(new Commands());
		}
	}

	public void onDisable() {
		for (Crates crate : Crates.cratesList) {
			crate.stop();
			crate.unspawnCrates();
		}
	}

	public static CratesExplorer getInstance() {
		return instance;
	}
}