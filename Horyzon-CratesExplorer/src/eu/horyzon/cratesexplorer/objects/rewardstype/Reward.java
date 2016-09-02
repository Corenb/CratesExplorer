package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public abstract class Reward {
	protected int pourcent;
	protected double amount;
	protected FireworkMeta firework;

	public int getPourcent() {
		return pourcent;
	}

	public double getAmount() {
		return amount;
	}

	public boolean hasFirework() {
		return firework != null;
	}

	public FireworkMeta getFirework() {
		return firework;
	}

	public abstract void giveReward(Player p);
}