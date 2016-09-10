package eu.horyzon.cratesexplorer;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import eu.horyzon.cratesexplorer.commands.Commands;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.listeners.PlayerModify;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import eu.horyzon.cratesexplorer.tasks.CrateTask;
import eu.horyzon.cratesexplorer.utils.FileManager;

public class CratesExplorer extends JavaPlugin {
	public static CrateTask task;
	private static CratesExplorer instance;

	public void onEnable() {
		instance = this;
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		// INITIALIZE FILES
		File[] dirs = (File[]) Arrays
				.asList(new File(getDataFolder(), "containers"), new File(getDataFolder(), "armorstands")).toArray();

		for (File dir : dirs) {
			new FileManager(dir).setup();
		}

		Configuration config = getConfig();

		if (config.getBoolean("debug", false)) {
			Logger log = getLogger();

			log.info(Crates.cratesList.size() + " differents crates registered");

			getCommand("cratesexplorer").setExecutor(new Commands());

			getServer().getPluginManager().registerEvents(new PlayerModify(), this);
		}

		getServer().getPluginManager().registerEvents(new PlayerExplore(), this);

		task = new CrateTask(this);
	}

	public void onDisable() {
		task.cancel();

		for (Crates crate : Crates.cratesList) {
			try {
				crate.unspawnCrates();
			} catch (IllegalStateException e) {
			}
		}

		Crates.cratesList.clear();
	}

	public static CratesExplorer getInstance() {
		return instance;
	}
}