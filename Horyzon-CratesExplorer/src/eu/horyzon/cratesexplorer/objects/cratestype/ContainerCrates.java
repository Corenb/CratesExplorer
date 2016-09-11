package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.utils.AnimationUtils;
import eu.horyzon.cratesexplorer.utils.ChestUtils;
import eu.horyzon.cratesexplorer.utils.FileUtils;

public class ContainerCrates extends Crates {
	public static File dir = new File(CratesExplorer.getInstance().getDataFolder(), "containers");

	public ContainerCrates(String id, Material material, int useTime, int spawnTime, double pourcent, Effect effect,
			Sound sound, Set<Object> containers, Set<Reward> rewards) {
		super.id = id;
		super.material = material;
		super.useTime = useTime;
		super.spawnTime = spawnTime;
		super.pourcent = pourcent;
		super.effect = effect;
		super.sound = sound;
		super.rewards = rewards;
		super.crates = containers;

		try {
			start();
		} catch (IllegalArgumentException e) {
			spawnRandomCrates();
		}

		cratesList.add(this);
	}

	@Override
	public boolean addCrate(Object crate) {
		boolean added = crates.add(crate);

		if (added)
			FileUtils.addCrate(new File(dir, id), parseLoc(((BlockState) crate)));

		return added;
	}

	@Override
	public boolean removeCrate(Object crate) {
		boolean removed = crates.remove(crate);

		if (removed)
			FileUtils.removeCrate(new File(dir, id), parseLoc((BlockState) crate));

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
	public boolean isSpawned(Object crate) {
		return ((BlockState) crate).equals(((BlockState) crate).getBlock().getState());
	}

	@Override
	public boolean isCrate(Object crate) {
		return crates.contains(crate);
	}

	@Override
	public void playAnimation(Object crate, Player p) {
		Block bloc = (Block) crate;
		Reward reward = getReward();

		if (sound != null)
			p.playSound(bloc.getLocation(), sound, 20, 20);

		ChestUtils.openChest(bloc, 30);
		reward.giveReward(p);
		AnimationUtils.createArmorStand(bloc.getLocation(), reward.getAmount());

		new BukkitRunnable() {
			@Override
			public void run() {
				reward.spawnFirework(bloc.getLocation().clone().add(0, 0.5, 0));

				if (respawn())
					unspawnCrate(bloc);

				PlayerExplore.removeUse(bloc);
			}
		}.runTaskLater(CratesExplorer.getInstance(), 60);
	}

	@Override
	public void unspawnCrates() {
		for (Object block : crates) {
			if (PlayerExplore.isInUse(block))
				return;
			unspawnCrate(((BlockState) block).getBlock());
		}
	}

	public void spawnCrates(Set<Object> blocks) {
		// Play Animation
		if (hasEffect())
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Object block : blocks)
						((BlockState) block).getLocation().getWorld().spigot().playEffect(
								((BlockState) block).getLocation().add(-0.5, 0.5, -0.5), effect, 0, 0, 0.1F, 0.1F, 0.1F, 0.5F,
								20, 30);
				}
			}.runTaskAsynchronously(CratesExplorer.getInstance());

		// Spawn ShowCase
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Object block : blocks)
					((BlockState) block).update(true);
			}
		}.runTaskLater(CratesExplorer.getInstance(), 12L);
	}

	public void unspawnCrate(Block crate) {
		crate.setType(Material.AIR);
	}
}