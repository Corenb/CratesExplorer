package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.utils.FileManager;

public class ContainerCrates extends Crates {
	public static File dir = new File(CratesExplorer.getInstance().getDataFolder(), "containers");

	public ContainerCrates(String id, Material material, int useTime, int spawnTime, double pourcent, Effect effect,
			Set<Object> containers, Set<Reward> rewards) {
		super.id = id;
		super.material = material;
		super.useTime = useTime;
		super.spawnTime = spawnTime;
		super.pourcent = pourcent;
		super.effect = effect;
		super.rewards = rewards;
		super.crates = containers;

		if (super.canRepeat())
			super.repeat = new HashMap<UUID, Long>();

		start();
		cratesList.add(this);
	}

	@Override
	public boolean addCrate(Object crate) {
		boolean added = crates.add(crate);

		if (added)
			FileManager.addLocation(new File(dir, id), parseLoc(((BlockState) crate)));

		return added;
	}

	@Override
	public boolean removeCrate(Object crate) {
		boolean removed = crates.remove(crate);

		if (removed)
			FileManager.removeLocation(new File(dir, id), parseLoc((BlockState) crate));

		return removed;
	}

	private static String parseLoc(BlockState container) {
		return String.join(":", container.getWorld().getName(), Integer.toString(container.getX()),
				Integer.toString(container.getY()), Integer.toString(container.getZ()),
				((DirectionalContainer) container.getData()).getFacing().name());
	}

	@Override
	public Set<Object> getCrates() {
		return crates;
	}

	@Override
	public Boolean isSpawned(Object crate) {
		BlockState c = (BlockState) crate;

		return c.equals(c.getBlock().getState());
	}

	@Override
	public void playAnimation(Object crate, Player p) {
		// TODO Stub de la méthode généré automatiquement

	}

	@Override
	public void spawnCrates(int amount) {
		Set<Object> randomContainers = new HashSet<Object>();
		Set<Object> copyContainers = new HashSet<Object>(crates);
		Random r = new Random();
		int i = 0;

		while (i < amount && i <= copyContainers.size()) {
			BlockState block = (BlockState) copyContainers.toArray()[r.nextInt(copyContainers.size())];

			copyContainers.remove(block);
			randomContainers.add(block);

			i++;
		}

		spawnCrates(randomContainers);
	}

	@Override
	public void spawnRandomCrates() {
		spawnCrates((int) Math.round(super.pourcent * crates.size()));
	}

	@Override
	public void spawnAllCrates() {
		spawnCrates(crates);
	}

	@Override
	public void unspawnCrates() {
		for (Object block : crates) {
			((BlockState) block).getBlock().setType(Material.AIR);
		}
	}

	public void spawnCrates(Set<Object> blocks) {
		// Play Animation
		if (super.hasEffect()) {
			Bukkit.getServer().getScheduler().runTask(CratesExplorer.getInstance(), new Runnable() {
				@Override
				public void run() {
					for (Object block : blocks) {
						((BlockState) block).getLocation().getWorld().spigot().playEffect(
								((BlockState) block).getLocation().add(0.5, 0.5, 0.5), effect, 0, 0, 0.1F, 0.1F, 0.1F,
								0.01F, 20, 20);
					}
				}
			});
		}

		// Spawn ShowCase
		Bukkit.getServer().getScheduler().runTaskLater(CratesExplorer.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Object block : blocks)
					((BlockState) block).update(true);
			}
		}, 12);
	}
}