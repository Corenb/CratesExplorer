package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.tasks.AnimArmorStand;
import eu.horyzon.cratesexplorer.utils.ChestReflection;
import eu.horyzon.cratesexplorer.utils.FileManager;
import net.md_5.bungee.api.ChatColor;

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

		if (useTime > 0)
			repeat = new HashMap<UUID, Long>();

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
		Block bloc = (Block) crate;
		Reward reward = getReward();

		if (sound != null)
			p.playSound(bloc.getLocation(), sound, 20, 20);

		ChestReflection.openChest(bloc, 30);
		reward.giveReward(p);
		ArmorStand as = createArmorStand(bloc, reward.getAmount());

		CratesExplorer.getInstance().getServer().getScheduler().runTaskLater(CratesExplorer.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						as.remove();
						reward.playEffect(bloc.getLocation());

						if (respawn()) {
							unspawnCrate(bloc);
							PlayerExplore.removeUse(bloc);
						}
					}
				}, 60);
	}

	private ArmorStand createArmorStand(Block block, double gain) {
		Location loc = block.getLocation().clone();
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0.5, -1.75, 0.5), EntityType.ARMOR_STAND);

		as.setCustomName(ChatColor.GOLD + "+ " + gain);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		as.setGravity(false);

		new AnimArmorStand(as).runTaskTimerAsynchronously(CratesExplorer.getInstance(), 0, 1);

		return as;
	}

	@Override
	public void spawnCrates(int amount) {
		Set<Object> randomContainers = new HashSet<Object>();
		Set<Object> copyContainers = new HashSet<Object>(crates);
		Random r = new Random();

		while (randomContainers.size() < amount && copyContainers.size() > 0) {
			BlockState block = (BlockState) copyContainers.toArray()[r.nextInt(copyContainers.size())];

			copyContainers.remove(block);
			randomContainers.add(block);
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
			unspawnCrate(((BlockState) block).getBlock());
		}
	}

	public void spawnCrates(Set<Object> blocks) {
		// Play Animation
		if (hasEffect()) {
			Bukkit.getServer().getScheduler().runTaskAsynchronously(CratesExplorer.getInstance(), new Runnable() {
				@Override
				public void run() {
					for (Object block : blocks) {
						((BlockState) block).getLocation().getWorld().spigot().playEffect(
								((BlockState) block).getLocation().add(0, 0.5, 0), effect, 0, 0, 0.1F, 0.1F, 0.1F,
								0.5F, 20, 30);
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

	public void unspawnCrate(Block crate) {
		crate.setType(Material.AIR);
	}
}