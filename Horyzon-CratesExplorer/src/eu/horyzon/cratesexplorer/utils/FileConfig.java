package eu.horyzon.cratesexplorer.utils;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class FileConfig extends YamlConfiguration {

	public abstract void load();

	public void edit(String path, Object object) {
		set(path, object);
	}

	/*public void loadContainers() {
		for (File containerFile : containerDir.listFiles()) {
			if (containerFile.getName().endsWith(".sc"))
				return;

			try {
				load(containerFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
				return;
			}

			String id = containerFile.getName().substring(0, containerFile.getName().length() - 3);
			Material material = Material.valueOf(getString("material"));
			int repeat = getInt("repeat");
			int spanTime = getInt("spanTime");
			double pourcentToSpawn = getDouble("pourcentToSpawn");
			boolean firework = getBoolean("firework");
			boolean particleSpawn = getBoolean("particleSpawn");

			TreeSet<Reward> rewards = loadRewards();

			
		}
	}

	public void loadArmorstands() {
		for (File entityFile : armorstandDir.listFiles()) {
			if (entityFile.getName().endsWith(".sc"))
				return;

			try {
				load(entityFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
				return;
			}

			String id = entityFile.getName().substring(0, entityFile.getName().length() - 3);
			Material material = Material.valueOf(getString("material"));
			int repeat = getInt("repeat");
			int spanTime = getInt("spanTime");
			double pourcentToSpawn = getDouble("pourcentToSpawn");
			boolean firework = getBoolean("firework");
			boolean particleSpawn = getBoolean("particleSpawn");
			
		}
	}

	public TreeSet<Reward> loadRewards() {
		TreeSet<Reward> rewards = new TreeSet<Reward>();

		for (String reward : getStringList("rewards")) {
			String[] split = reward.split(":");

			if(Material.getMaterial(split[0]) != null)
				rewards.add(new ItemReward());
			else
				rewards.add(new CurrencyReward(CurrencyManager.getCurrency(split[0]), Double.parseDouble(split[1]), Integer.parseInt(split[2])));
		}

		return rewards;
	}

	public void editContainers(Set<String> containers) {
		
	}

	public void editArmorstands(Set<String> containers) {
		
	}*/
}