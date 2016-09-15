package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import eu.horyzon.cratesexplorer.CratesExplorer;

public class FileUtils {
	private static FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return !name.contains(".");
		}
	};

	public static void setup(File f) {
		if (!(f.exists()) && f.mkdirs()) {
			CratesExplorer.getInstance().saveResource(f.getName() + "/" + f.getName() + ".example", false);
		}

		for (File c : f.listFiles(filter))
			new CratesUtils(c).load();
	}

	public static void addCrate(File f, Object crate) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		List<Object> locList = new ArrayList<Object>(config.getList("crates"));

		locList.add(crate.toString());
		config.set("crates", locList);
		try {
			config.save(f);
		} catch (IOException e) {
		}
	}

	public static void removeCrate(File f, Object crate) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		List<Object> locList = new ArrayList<Object>(config.getList("crates"));

		locList.remove(crate);
		config.set("crates", locList);
		try {
			config.save(f);
		} catch (IOException e) {
		}
	}
}