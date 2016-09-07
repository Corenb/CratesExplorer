package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrates;
import eu.horyzon.cratesexplorer.objects.rewardstype.CurrencyReward;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.currencydispenser.CurrencyManager;

public class FileManager {
	File f;
	FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return !name.contains(".");
		}
	};

	public FileManager(File f) {
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
			Effect effect = config.isSet("effect") && !config.getString("effect").equalsIgnoreCase("none")
					? Effect.valueOf(config.getString("effect").toUpperCase()) : null;

			Set<Reward> rewards = loadRewards(config);

			if (material.isBlock()) {
				Set<BlockState> crates = new HashSet<BlockState>();

				for (String locStr : config.getStringList("crates")) {
					String[] split = locStr.split(":");
					Location loc = new Location(CratesExplorer.getInstance().getServer().getWorld(split[0]),
							Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));

					BlockState state = loc.getBlock().getState();
					state.setType(material);

					if (state.getData() instanceof DirectionalContainer) {
						MaterialData data = state.getData();
						DirectionalContainer cont = (DirectionalContainer) data;

						cont.setFacingDirection(BlockFace.valueOf(split[4]));
						state.setData(cont);
						state.update();

						crates.add(state);
					}
				}

				new ContainerCrates(c.getName(), material, useTime, spawnTime, pourcentSpawn, effect, new HashSet<Object>(crates), rewards);
			} else {

			}
		}
	}

	private Set<Reward> loadRewards(YamlConfiguration config) {
		boolean fw = config.isSet("fireworks");
		Map<String, FireworkEffect> fireworks = fw ? loadFirework(config) : null;
		Set<Reward> rewards = new HashSet<Reward>();

		for (String pourcent : config.getConfigurationSection("rewards").getKeys(false)) {
			String path = "rewards." + pourcent + ".";
			try {
				int pourcentage = Integer.parseInt(pourcent);
				double amount = config.getDouble(path + "amount");
				CurrencyManager currency = CurrencyManager.existCurrency(config.getString(path + "type"))
						? CurrencyManager.getCurrency(config.getString(path + "type"))
						: CurrencyManager.registerCurrency(config.getString(path + "type"));
				FireworkEffect firework = fw ? fireworks.get(config.getString(path + "firework")) : null;

				Reward reward = new CurrencyReward(currency, amount, pourcentage, firework);

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
				fwb.withColor(FireworkColors.valueOf(color).getColor());
			}

			for (String fade : config.getString(path + "fade").split(",")) {
				fwb.withFade(FireworkColors.valueOf(fade.toUpperCase()).getColor());
			}

			fwb.flicker(config.getBoolean(path + "flicker"));
			fwb.trail(config.getBoolean(path + "trail"));
			fwb.with(Type.valueOf(config.getString(path + "type")));

			fireworks.put(firework, fwb.build());
		}

		return fireworks;
	}
}