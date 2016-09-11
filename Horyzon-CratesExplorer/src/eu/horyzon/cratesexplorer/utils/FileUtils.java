package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.ArmorstandCrates;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrates;
import eu.horyzon.cratesexplorer.objects.rewardstype.CurrencyReward;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.currencydispenser.CurrencyManager;

public class FileUtils {
	protected File f;
	private FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return !name.contains(".");
		}
	};

	public FileUtils(File f) {
		this.f = f;
	}

	public void setup() {
		if (!(f.exists()) && f.mkdirs()) {
			CratesExplorer.getInstance().saveResource(f.getName() + "/" + f.getName() + ".example", false);
		}

		for (File c : f.listFiles(filter)) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(c);
			Material material = Material.valueOf(config.getString("material").toUpperCase());
			int useTime = config.getInt("useTime");
			int spawnTime = config.getInt("spawnTime");
			double pourcentSpawn = config.getDouble("pourcentSpawn");
			Effect effect = null;
			Sound sound = null;

			try {
				effect = Effect.valueOf(config.getString("effect").toUpperCase());
				sound = Sound.valueOf(config.getString("sound").toUpperCase());
			} catch (NullPointerException e) {
			}

			Set<Reward> rewards = loadRewards(config);

			if (material.isBlock()) {
				Set<Object> crates = new HashSet<Object>();

				for (String locStr : config.getStringList("crates")) {
					String[] split = locStr.split(":");
					Location loc = new Location(CratesExplorer.getInstance().getServer().getWorld(split[0]),
							Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));

					loc.getBlock().setType(material);

					BlockState state = loc.getBlock().getState();

					if (state.getData() instanceof DirectionalContainer) {
						MaterialData data = state.getData();
						DirectionalContainer cont = (DirectionalContainer) data;

						cont.setFacingDirection(BlockFace.valueOf(split[4]));
						state.setData(cont);
						state.update();

						crates.add(state);
					}
				}

				new ContainerCrates(c.getName(), material, useTime, spawnTime, pourcentSpawn, effect, sound,
						crates, rewards);
			} else if (material.equals(Material.ARMOR_STAND)){
				Set<Object> armorstands = new HashSet<Object>();

				for (String locStr : config.getStringList("crates")) {
					armorstands.add(new ArmorStandObject(locStr.split(":")));
				}

				new ArmorstandCrates(c.getName(), material, useTime, spawnTime, pourcentSpawn, effect, sound, armorstands, rewards);
			}
		}
	}

	private Set<Reward> loadRewards(YamlConfiguration config) {
		Map<String, FireworkEffect> fireworksList = loadFirework(config);
		Set<Reward> rewards = new HashSet<Reward>();

		for (String pourcent : config.getConfigurationSection("rewards").getKeys(false)) {
			String path = "rewards." + pourcent + ".";
			try {
				int pourcentage = Integer.parseInt(pourcent);
				double amount = config.getDouble(path + "amount");
				CurrencyManager currency = CurrencyManager.existCurrency(config.getString(path + "type"))
						? CurrencyManager.getCurrency(config.getString(path + "type"))
						: CurrencyManager.registerCurrency(config.getString(path + "type"));
				String[] fireworkNames = config.getString(path + "firework").split(":");
				Set<FireworkEffect> fireworks = new HashSet<FireworkEffect>();

				for (String fireworkName : fireworkNames)
					fireworks.add(fireworksList.get(fireworkName));

				Reward reward = new CurrencyReward(currency, amount, pourcentage, fireworks);

				rewards.add(reward);
			} catch (IllegalArgumentException e) {
				CratesExplorer.getInstance().getLogger()
						.warning("Error with reward of type " + config.getString(path + "type"));
			}
		}

		return rewards;
	}

	private Map<String, FireworkEffect> loadFirework(YamlConfiguration config) {
		Map<String, FireworkEffect> fireworks = new HashMap<String, FireworkEffect>();

		for (String firework : config.getConfigurationSection("fireworks").getKeys(false)) {
			String path = "fireworks." + firework + ".";
			Builder fwb = FireworkEffect.builder();

			for (String color : config.getString(path + "colors").split(",")) {
				fwb.withColor(Color.fromRGB(Integer.parseInt(color)));
			}

			for (String fade : config.getString(path + "fade").split(",")) {
				fwb.withFade(Color.fromRGB(Integer.parseInt(fade)));
			}

			fwb.flicker(config.getBoolean(path + "flicker"));
			fwb.trail(config.getBoolean(path + "trail"));
			fwb.with(Type.valueOf(config.getString(path + "type")));

			fireworks.put(firework, fwb.build());
		}

		return fireworks;
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