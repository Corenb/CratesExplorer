package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;

import eu.horyzon.cratesexplorer.CratesExplorer;

public class ConfigManager {

	public void setup() {
		File dir = CratesExplorer.getInstance().getDataFolder();
		if (!dir.exists())
			dir.mkdirs();

		File containers = new File(dir, "containers");
		if (!containers.exists() && containers.mkdirs())
			extractFile(new File(containers, "container.exemple"));

		File armorstands = new File(dir, "armorstands");
		if (!armorstands.exists() && armorstands.mkdirs())
			extractFile(new File(containers, "armorstand.exemple"));

		loadContainers(containers);
		loadArmorstands(armorstands);
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
		
	}

	public void registerContainers() {
		
	}

	public void loadArmorstands(File f) {
		
	}

	public void registerArmorstands() {
		
	}
}