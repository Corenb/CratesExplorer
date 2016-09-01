package eu.horyzon.cratesexplorer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.commands.Commands;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrates;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import eu.horyzon.cratesexplorer.objects.rewardstype.CurrencyReward;
import eu.horyzon.cratesexplorer.objects.rewardstype.ItemReward;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.tasks.SpawnTask;
import eu.horyzon.currencydispenser.CurrencyManager;

public class CratesExplorer extends JavaPlugin {
	private static CratesExplorer instance;
	private static Configuration config;

	public static Set<Crates> showCases = new HashSet<Crates>();
	public static Map<UUID, String> modify = new HashMap<UUID, String>();
	public static Map<String, SpawnTask> tasks = new HashMap<String, SpawnTask>();

	public void onEnable() {
		instance = this;
		config = getConfig();

		// INITIALIZE FILES
		File[] directories = (File[]) Arrays
				.asList(new File(getDataFolder(), "containers"), new File(getDataFolder(), "armorstands")).toArray();

		for (File dir : directories) {
			if (!dir.exists()) {
				dir.mkdirs();
			} else {
				for (File conf : dir.listFiles()) {
					YamlConfiguration config = new YamlConfiguration();

					try {
						config.load(conf);
					} catch (IOException | InvalidConfigurationException e) {
						e.printStackTrace();
						continue;
					}

					String id = config.getName();
					Material material = Material.getMaterial(config.getString("material"));
					int repeat = config.getInt("repeat");
					int spanTime = config.getInt("spanTime");
					double pourcentToSpawn = config.getDouble("pourcentToSpawn");
					boolean firework = config.getBoolean("firework");
					boolean particleSpawn = config.getBoolean("particleSpawn");
				}
			}
		}

		// INITIALIZE CONFIG
		for (String id : config.getConfigurationSection("showcase").getKeys(false)) {
			String path = "showcase." + id + ".";
			Material material = Material.getMaterial(config.getString(path + "material"));
			int repeat = config.getInt(path + "repeat");
			int spanTime = config.getInt(path + "spanTime");
			double pourcentToSpawn = config.getDouble(path + "pourcentToSpawn");
			boolean firework = config.getBoolean(path + "firework");
			boolean particleSpawn = config.getBoolean(path + "particleSpawn");

			// REGISTER REWARDS
			TreeSet<Reward> rewards = new TreeSet<Reward>();
			for (String reward : config.getStringList(path + "rewards")) {
				String[] split = reward.split(":");

				if (Material.getMaterial(split[0]) != null)
					rewards.add(new ItemReward());
				else
					rewards.add(new CurrencyReward(CurrencyManager.getCurrency(split[0]), Double.parseDouble(split[1]),
							Integer.parseInt(split[2])));
			}

			// REGISTER CASES
			if (material.isBlock()) {
				ContainerCrates showCase = new ContainerCrates(id, repeat, spanTime, pourcentToSpawn, firework,
						particleSpawn, new TreeSet<BlockState>(), null);

				for (String showCases : config.getStringList(path + "showcase")) {
					String[] split = showCases.split(":");
					Location loc = new Location(getServer().getWorld(split[0]), Double.parseDouble(split[1]),
							Double.parseDouble(split[2]), Double.parseDouble(split[3]));

					loc.getBlock().setType(material);

					Block block = loc.getBlock();
					BlockState state = block.getState();

					if (state.getData() instanceof DirectionalContainer) {
						MaterialData data = state.getData();
						DirectionalContainer c = (DirectionalContainer) data;

						c.setFacingDirection(BlockFace.valueOf(split[4]));
						state.setData(c);
						state.update();

						showCase.addContainer(state);
						getLogger().warning("Block in " + loc.getWorld() + " at " + loc.getX() + " " + loc.getY() + " "
								+ loc.getZ() + " registered as Container!");
					} else
						throw new IllegalArgumentException("Block in " + loc.getWorld() + " at " + loc.getX() + " "
								+ loc.getY() + " " + loc.getZ() + " must be InventoryHolder.");
				}
			} else {
				// new EntityShowCase();
			}

			// INITIALIZE TASKS
			if (spanTime > 0) {
				SpawnTask task = new SpawnTask(id, spanTime);

				task.start();
				tasks.put(id, task);
			}
		}

		if (config.getBoolean("debug", false)) {
			getCommand("lobbyexplorer").setExecutor(new Commands());
		}
	}

	public void onDisable() {
		for (BukkitRunnable task : tasks.values()) {
			task.cancel();
			;
		}

		tasks.clear();

		for (Crates showcase : showCases) {
			showcase.unspawnShowCase();
		}
	}

	public static CratesExplorer getInstance() {
		return instance;
	}

	public static Configuration getConfiguration() {
		return config;
	}
}