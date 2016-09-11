package eu.horyzon.cratesexplorer.objects.rewardstype;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.utils.FireworkUtils;

public abstract class Reward {
	protected int pourcent;
	protected double amount;
	protected Set<FireworkEffect> firework;

	protected static NumberFormat nf = new DecimalFormat("#.##");

	public int getPourcent() {
		return pourcent;
	}

	public String getAmount() {
		return nf.format(amount);
	}

	public boolean hasFirework() {
		return firework.size() > 0;
	}

	public Set<FireworkEffect> getFirework() {
		return firework;
	}

	public void spawnFirework(Location loc) {
		if(hasFirework())
			new FireworkUtils(firework, loc);
	}

	public abstract void giveReward(Player p);
}