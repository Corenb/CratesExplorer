package eu.horyzon.cratesexplorer.utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.ArmorStandObject;
import eu.horyzon.cratesexplorer.objects.cratestype.ArmorstandCrates;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrates;
import eu.horyzon.cratesexplorer.objects.rewardstype.CurrencyReward;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.currencydispenser.CurrencyManager;

public class CratesUtils extends YamlConfiguration {
	protected String name;

	public CratesUtils(File f) {
		this.name = f.getName();
		loadConfiguration(f);
	}

	public void load() {
		Material material = Material.valueOf(getString("material").toUpperCase());
		int useTime = getInt("useTime");
		int spawnTime = getInt("spawnTime");
		double pourcentSpawn = getDouble("pourcentSpawn");
		Effect effect = null;
		Sound sound = null;

		try {
			effect = Effect.valueOf(getString("effect").toUpperCase());
			sound = Sound.valueOf(getString("sound").toUpperCase());
		} catch (NullPointerException e) {
		}

		Set<Reward> rewards = loadRewards();

		if (material.isBlock()) {
			Set<Object> crates = new HashSet<Object>();

			for (String locStr : getStringList("crates")) {
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

			new ContainerCrates(name, material, useTime, spawnTime, pourcentSpawn, effect, sound, crates,
					rewards);
		} else if (material.equals(Material.ARMOR_STAND)) {
			Set<Object> armorstands = new HashSet<Object>();

			for (String locStr : getStringList("crates")) {
				String[] locSplit = locStr.split(":");
				armorstands.add(new ArmorStandObject(locSplit));
			}

			new ArmorstandCrates(name, material, useTime, spawnTime, pourcentSpawn, effect, sound, armorstands,
					rewards);
		}
	}

	private Set<Reward> loadRewards() {
		Map<String, FireworkEffect> fireworksList = loadFirework();
		Set<Reward> rewards = new HashSet<Reward>();

		for (String pourcent : getConfigurationSection("rewards").getKeys(false)) {
			String path = "rewards." + pourcent + ".";
			try {
				int pourcentage = Integer.parseInt(pourcent);
				double amount = getDouble(path + "amount");
				CurrencyManager currency = CurrencyManager.existCurrency(getString(path + "type"))
						? CurrencyManager.getCurrency(getString(path + "type"))
						: CurrencyManager.registerCurrency(getString(path + "type"));
				String[] fireworkNames = getString(path + "firework").split(":");
				Set<FireworkEffect> fireworks = new HashSet<FireworkEffect>();

				for (String fireworkName : fireworkNames)
					fireworks.add(fireworksList.get(fireworkName));

				Reward reward = new CurrencyReward(currency, amount, pourcentage, fireworks);

				rewards.add(reward);
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
				fwb.withColor(Color.fromRGB(Integer.parseInt(color)));
			}

			for (String fade : getString(path + "fade").split(",")) {
				fwb.withFade(Color.fromRGB(Integer.parseInt(fade)));
			}

			fwb.flicker(getBoolean(path + "flicker"));
			fwb.trail(getBoolean(path + "trail"));
			fwb.with(Type.valueOf(getString(path + "type")));

			fireworks.put(firework, fwb.build());
		}

		return fireworks;
	}
}