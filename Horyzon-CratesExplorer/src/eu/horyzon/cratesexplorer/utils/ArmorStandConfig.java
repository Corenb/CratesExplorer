package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;

public class ArmorStandConfig extends FileConfig {

	public ArmorStandConfig(File f) {
		try {
			load(f);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() {
		
	}
}