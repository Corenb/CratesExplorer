package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;

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

	public abstract void giveReward(Player p);
}