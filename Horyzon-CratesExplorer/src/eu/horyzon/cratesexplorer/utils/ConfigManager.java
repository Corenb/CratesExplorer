package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeSet;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.ByteStreams;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public class ConfigManager {

	public void setup() {
		File dir = CratesExplorer.getInstance().getDataFolder();
		if (!dir.exists())
			dir.mkdirs();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.endsWith(".exemple");
			}
		};

		File containers = new File(dir, "containers");
		if (!containers.exists() && containers.mkdirs())
			extractFile(new File(containers, "container.example"));

		for (File container : containers.listFiles(filter)) {
			loadContainers(container);
		}

		File armorstands = new File(dir, "armorstands");
		if (!armorstands.exists() && armorstands.mkdirs())
			extractFile(new File(armorstands, "armorstand.example"));

		for (File armorstand : armorstands.listFiles(filter)) {
			loadArmorstands(armorstand);
		}
	}

	public void extractFile(File f) {
		try {
			f.createNewFile();
			InputStream inputStream = getClass().getResourceAsStream(f.getName());
			OutputStream outputStream = new FileOutputStream(f);
			ByteStreams.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadContainers(File f) {
		YamlConfiguration cont = YamlConfiguration.loadConfiguration(f);
		
	}

	public void loadArmorstands(File f) {

	}

	public TreeSet<Reward> loadRewards() {
		return null;
	}

	public void registerContainers() {

	}

	public void registerArmorstands() {

	}
}