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

import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

import com.google.common.io.ByteStreams;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrates;
import eu.horyzon.cratesexplorer.objects.rewardstype.CurrencyReward;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.currencydispenser.CurrencyManager;

public class FileManager extends YamlConfiguration {
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
			extractFile(new File(f, f.getName() + ".example"));
		}

		for (File c : f.listFiles(filter)) {
			loadConfiguration(c);
			Material mat = Material.valueOf(getString("material").toUpperCase());
			int useTime = getInt("useTime");
			int spawnTime = getInt("spawnTime");
			double pourcentSpawn = getDouble("pourcentSpawn");
			Effect effect = isSet("effect") && !getString("effect").equalsIgnoreCase("none")
					? Effect.valueOf(getString("effect").toUpperCase()) : null;

			TreeSet<Reward> rewards = loadRewards();

			if(mat.isBlock()) {
				TreeSet<BlockState> crates = new TreeSet<BlockState>();

				for(String locStr : getConfigurationSection("crates").getKeys(false)) {
					String[] split = locStr.split(":");
					Location loc = new Location(CratesExplorer.getInstance().getServer().getWorld(split[0]), Double.parseDouble(split[1]),
							Double.parseDouble(split[2]), Double.parseDouble(split[3]));

					loc.getBlock().setType(mat);

					Block block = loc.getBlock();
					BlockState state = block.getState();

					if (state.getData() instanceof DirectionalContainer) {
						MaterialData data = state.getData();
						DirectionalContainer cont = (DirectionalContainer) data;

						cont.setFacingDirection(BlockFace.valueOf(split[4]));
						state.setData(cont);
						state.update();

						crates.add(state);
					}
				}
				new ContainerCrates(c.getName(), useTime, spawnTime, pourcentSpawn, effect, crates, rewards);
			} else {
				
			}
		}
	}

	private void extractFile(File f) {
		try {
			f.createNewFile();
			InputStream inputStream = getClass().getResourceAsStream(f.getName());
			OutputStream outputStream = new FileOutputStream(f);
			ByteStreams.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TreeSet<Reward> loadRewards() {
		boolean fw = isSet("fireworks");
		Map<String, FireworkEffect> fireworks = fw ? loadFirework() : null;
		TreeSet<Reward> rewards = new TreeSet<Reward>();

		for (String pourcent : getConfigurationSection("rewards").getKeys(false)) {
			String path = "rewards." + pourcent + ".";
			try {
				int pourcentage = Integer.parseInt(pourcent);
				double amount = Double.parseDouble(getString(path + "amount"));
				CurrencyManager currency = CurrencyManager.existCurrency(getString(path + "type"))
						? CurrencyManager.getCurrency(getString(path + "type"))
						: CurrencyManager.registerCurrency(getString(path + "type"));
				FireworkEffect firework = fw ? fireworks.get(getString(path + "firework")) : null;

				rewards.add(new CurrencyReward(currency, amount, pourcentage, firework));
			} catch (IllegalArgumentException e) {
				CratesExplorer.getInstance().getLogger()
						.warning("Error with reward of type " + getString(path + "type"));
			}
		}

		return rewards;
	}

	private Map<String, FireworkEffect> loadFirework() {
		Map<String, FireworkEffect> fireworks = new HashMap<String, FireworkEffect>();

		for (String firework : getConfigurationSection("fireworks").getKeys(false)) {
			String path = "fireworks." + firework + ".";
			Builder fwb = FireworkEffect.builder();

			for (String color : getString(path + "colors").split(",")) {
				fwb.withColor(FireworkColors.valueOf(color).getColor());
			}

			for (String fade : getString(path + "fade").split(",")) {
				fwb.withFade(FireworkColors.valueOf(fade.toUpperCase()).getColor());
			}

			fwb.flicker(getBoolean(path + "flicker"));
			fwb.trail(getBoolean(path + "trail"));
			fwb.with(Type.valueOf(getString(path + "type")));

			fireworks.put(firework, fwb.build());
		}

		return fireworks;
	}
}