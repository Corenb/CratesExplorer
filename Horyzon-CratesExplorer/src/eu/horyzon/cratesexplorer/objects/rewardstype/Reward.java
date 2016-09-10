package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.utils.InstantFirework;

public abstract class Reward {
	protected int pourcent;
	protected double amount;
	protected FireworkEffect firework;

	public int getPourcent() {
		return pourcent;
	}

	public double getAmount() {
		return amount;
	}

	public boolean hasFirework() {
		return firework != null;
	}

	public FireworkEffect getFirework() {
		return firework;
	}

	protected void spawnFirework(Location loc) {
		new InstantFirework(firework, loc);
	}

	public abstract void giveReward(Player p);

	public abstract void playEffect(Location loc);
}