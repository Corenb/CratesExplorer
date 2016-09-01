package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public class ContainerCrates extends Crates {
	private TreeSet<BlockState> containers;

	public ContainerCrates(String id, int repeat, int spanTime, double pourcentToSpawn, boolean firework,
			boolean particleSpawn, TreeSet<BlockState> containers, TreeSet<Reward> rewards) {
		super.id = id;
		super.repeat = repeat;
		super.spanTime = spanTime;
		super.pourcentToSpawn = pourcentToSpawn;
		super.firework = firework;
		super.particleSpawn = particleSpawn;
		this.containers = containers;
		super.rewards = rewards;
	}

	public boolean registerContainer(BlockState container) {
		if (addContainer(container)) {
			List<String> crates = CratesExplorer.getConfiguration().getStringList("showcase." + super.id + ".case");

			crates.add(String.join(":", container.getWorld().getName(), Integer.toString(container.getX()),
					Integer.toString(container.getY()), Integer.toString(container.getZ()),
					((DirectionalContainer) container.getData()).getFacing().name()));

			CratesExplorer.getConfiguration().set("showcase." + super.id + ".case", crates);

			return true;
		}

		return false;
	}

	public boolean addContainer(BlockState container) {
		return containers.add(container);
	}

	@Override
	public TreeSet<?> getCrates() {
		return containers;
	}

	@Override
	public Boolean isCrates(Object showCase) {
		return containers.contains(showCase);
	}

	@Override
	public void playAnimation() {
		// TODO Stub de la méthode généré automatiquement

	}

	@Override
	public void spawnCrates(int amount) {
		TreeSet<BlockState> randomContainers = new TreeSet<BlockState>();
		TreeSet<BlockState> copyContainers = new TreeSet<BlockState>(containers);
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
		spawnCrates((int) Math.round(super.pourcentToSpawn * containers.size()));
	}

	@Override
	public void spawnAllCrates() {
		spawnCrates(containers);
	}

	@Override
	public void unspawnCrates() {
		for (BlockState block : containers) {
			block.getBlock().setType(Material.AIR);
		}
	}

	public void spawnCrates(TreeSet<BlockState> blocks) {
		// Play Animation
		for (BlockState block : blocks) {
			block.getLocation().getWorld().spigot().playEffect(block.getLocation().add(0.5, 0.5, 0.5), Effect.SMALL_SMOKE, 0, 0, 0.1F, 0.1F, 0.1F, 0.01F, 20, 20);;
		}

		// Spawn ShowCase
		Bukkit.getServer().getScheduler().runTaskLater(CratesExplorer.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (BlockState block : blocks)
					block.update(true);
			}
		}, 12);
	}
}