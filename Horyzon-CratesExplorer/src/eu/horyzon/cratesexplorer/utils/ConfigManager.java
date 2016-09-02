package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import com.google.common.io.ByteStreams;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.currencydispenser.CurrencyManager;
import net.md_5.bungee.api.ChatColor;

public class ConfigManager extends YamlConfiguration {

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
			loadConfiguration(container);
			loadContainers();
		}

		File armorstands = new File(dir, "armorstands");
		if (!armorstands.exists() && armorstands.mkdirs())
			extractFile(new File(armorstands, "armorstand.example"));

		for (File armorstand : armorstands.listFiles(filter)) {
			loadConfiguration(armorstand);
			loadArmorstands();
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

	public void loadContainers() {
		
		Material mat = Material.valueOf(getString("material").toUpperCase());
		int useTime = getInt("useTime");
		int spawnTime = getInt("spawnTime");
		double pourcentSpawn = getDouble("pourcentSpawn");
		Effect effect = getString("effect").equalsIgnoreCase("none") ? null : Effect.valueOf(getString("effect").toUpperCase());

		TreeSet<Reward> rewards = loadRewards();
	}

	public void loadArmorstands() {

	}

	public TreeSet<Reward> loadRewards() {
		boolean firework = getBoolean("firework");
		Map<String, FireworkEffect> fireworks = loadFirework();
		TreeSet<Reward> rewards = new TreeSet<Reward>();

		for(String pourcent : getConfigurationSection("rewards").getKeys(false)) {
			String path = "rewards." + pourcent + ".";
			try {
				int pourcentage = Integer.parseInt(pourcent);
				int amount = Integer.parseInt(getString(path + "amount"));
				CurrencyManager currency = CurrencyManager.existCurrency(getString(path + "type")) ? CurrencyManager.getCurrency(getString(path + "type")) : CurrencyManager.registerCurrency(getString(path + "type"));
				
			} catch (IllegalArgumentException e) {
				CratesExplorer.getInstance().getLogger().warning("Error with reward of type " + getString(path + "type"));
			}
		}

		return rewards;
	}

	private Map<String, FireworkEffect> loadFirework() {
		Map<String, FireworkEffect> fireworks = new HashMap<String, FireworkEffect>();

		for(String firework : getConfigurationSection("fireworks").getKeys(false)) {
			String path = "fireworks." + firework + ".";
			Builder fwb = FireworkEffect.builder();

			for(String color : getString(path + "colors").split(",")) {
				fwb.withColor(FireworkColors.valueOf(color).getColor());
			}

			for(String fade : getString(path + "fade").split(",")) {
				fwb.withFade(FireworkColors.valueOf(fade.toUpperCase()).getColor());
			}

			fwb.flicker(getBoolean(path + "flicker"));
			fwb.trail(getBoolean(path + "trail"));
			fwb.with(Type.valueOf(getString(path + "type")));

			fireworks.put(firework, fwb.build());
		}

		return fireworks;
	}

	public void registerContainers() {

	}

	public void registerArmorstands() {

	}
}