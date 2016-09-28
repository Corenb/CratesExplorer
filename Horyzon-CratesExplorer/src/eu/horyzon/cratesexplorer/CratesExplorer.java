package eu.horyzon.cratesexplorer;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import com.huskehhh.mysql.Database;
import com.huskehhh.mysql.mysql.MySQL;

import eu.horyzon.cratesexplorer.commands.Commands;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.listeners.PlayerModify;
import eu.horyzon.cratesexplorer.objects.cratestype.Crate;
import eu.horyzon.cratesexplorer.objects.cratestype.RunnableCrate;
import eu.horyzon.cratesexplorer.tasks.CrateSpawn;
import eu.horyzon.cratesexplorer.utils.FileUtils;
import net.md_5.bungee.api.ChatColor;

public class CratesExplorer extends JavaPlugin {
	public static CrateSpawn task;
	private static Database sql;
	private static CratesExplorer instance;

	public void onEnable() {
		Logger log = getLogger();
		instance = this;
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		// INITIALIZE FILES
		File[] files = (File[]) Arrays.asList(new File(getDataFolder(), "block/block.example"),
				new File(getDataFolder(), "entity/entity.example")).toArray();

		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		for (File file : files)
			new FileUtils(file).setup();

		Configuration config = getConfig();

		// INITIALIZE MySQL
		if (config.getBoolean("mysql.enable")) {
			String host = config.getString("mysql.host");
			String port = config.getString("mysql.port");
			String database = config.getString("mysql.database");
			String username = config.getString("mysql.username");
			String password = config.getString("mysql.password");
			sql = new MySQL(host, port, database, username, password);
		} else {
			log.warning("Please configure mysql in config.");
			onDisable();
			return;
		}

		// INITIALIZE CONNECTION
		try {
			sql.openConnection();

			sql.updateSQL("CREATE TABLE IF NOT EXISTS PremiumConnector (UUID CHARACTER(36) NOT NULL, Name VARCHAR(26) NOT NULL, LastIp VARCHAR(255) NOT NULL, Premium BOOLEAN, PRIMARY KEY (UUID));");

			log.info(ChatColor.GREEN + "Connection to the database etablished!");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			log.warning("Can't connect to database.");
			onDisable();
			return;
		}

		if (config.getBoolean("debug", false)) {
			getLogger().info(Crate.allCrates.size() + " differents crates registered");

			getCommand("cratesexplorer").setExecutor(new Commands());

			getServer().getPluginManager().registerEvents(new PlayerModify(), this);
		}

		getServer().getPluginManager().registerEvents(new PlayerExplore(), this);

		task = new CrateSpawn(this);
	}

	public void onDisable() {
		task.cancel();

		for (RunnableCrate crate : Crate.getRunnableCrates()) {
			crate.unspawnCrates();
		}

		Crate.allCrates.clear();
	}

	public static CratesExplorer getInstance() {
		return instance;
	}
}