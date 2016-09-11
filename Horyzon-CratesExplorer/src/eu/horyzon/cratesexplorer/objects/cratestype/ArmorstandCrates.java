package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.utils.AnimationUtils;
import eu.horyzon.cratesexplorer.utils.ArmorStandObject;
import eu.horyzon.cratesexplorer.utils.FileUtils;

public class ArmorstandCrates extends Crates {
	public static File dir = new File(CratesExplorer.getInstance().getDataFolder(), "armorstands");

	public ArmorstandCrates(String id, Material material, int useTime, int spawnTime, double pourcent, Effect effect,
			Sound sound, Set<Object> armorstands, Set<Reward> rewards) {
		super.id = id;
		super.material = material;
		super.useTime = useTime;
		super.spawnTime = spawnTime;
		super.pourcent = pourcent;
		super.effect = effect;
		super.sound = sound;
		super.rewards = rewards;
		super.crates = armorstands;

		try {
			start();
		} catch (IllegalArgumentException e) {
			spawnRandomCrates();
		}

		cratesList.add(this);
	}

	@Override
	public boolean addCrate(Object as) {
		ArmorStandObject crate = null;
		try {
			crate = getCrate((ArmorStand) as);
			return false;
		} catch (NullPointerException e) {
			crate = new ArmorStandObject((ArmorStand) as);
		}

		FileUtils.addCrate(new File(dir, id), crate.deserialize());
		crates.add(crate);

		return true;
	}

	@Override
	public boolean removeCrate(Object as) {
		ArmorStandObject crate = null;
		try {
			crate = getCrate((ArmorStand) as);
		} catch (NullPointerException e) {
			return false;
		}

		FileUtils.removeCrate(new File(dir, id), crate.deserialize());
		crates.remove(crate);

		return true;
	}

	@Override
	public boolean isCrate(Object crate) {
		try {
			return crates.contains(getCrate((ArmorStand) crate));
		} catch (NullPointerException e) {
			return false;
		}
	}

	private ArmorStandObject getCrate(ArmorStand as) throws NullPointerException {
		for (Object crate : crates) {
			if(((ArmorStandObject) crate).isArmorstand(as))
				return (ArmorStandObject) crate;
		}

		throw new NullPointerException();
	}

	@Override
	public boolean isSpawned(Object as) {
		return ((ArmorStandObject) as).isValid();
	}

	@Override
	public void spawnCrates(Set<Object> crates) {
		if (hasEffect())
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Object crate : crates)
						((ArmorStandObject) crate).getLocation().getWorld().spigot().playEffect(
								((ArmorStandObject) crate).getLocation().clone().add(-0.5, 0.5, -0.5), effect, 0, 0, 0.1F, 0.1F, 0.1F, 0.5F,
								20, 30);
				}
			}.runTaskAsynchronously(CratesExplorer.getInstance());

		// Spawn ShowCase
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Object crate : crates)
					((ArmorStandObject) crate).spawn();
			}
		}.runTaskLater(CratesExplorer.getInstance(), 12L);
	}

	@Override
	public void unspawnCrates() {
		for (Object armorstand : crates) {
			if (PlayerExplore.isInUse(armorstand))
				return;
			unspawnCrate((ArmorStandObject) armorstand);
		}
	}

	public void unspawnCrate(ArmorStandObject as) {
		as.remove();
	}

	@Override
	public void playAnimation(Object crate, Player p) {
		ArmorStand armorstand = (ArmorStand) crate;
		Location loc = armorstand.getLocation();
		Reward reward = getReward();

		if (sound != null)
			p.playSound(armorstand.getLocation(), sound, 20, 20);

		reward.giveReward(p);
		AnimationUtils.createArmorStand(loc.clone().add(0, -0.4, 0), reward.getAmount());

		new BukkitRunnable() {
			@Override
			public void run() {
				reward.spawnFirework(loc.clone().add(0, 1.7, 0));

				if (respawn())
					unspawnCrate(getCrate(armorstand));

				PlayerExplore.removeUse(armorstand);
			}
		}.runTaskLater(CratesExplorer.getInstance(), 60);
	}
}