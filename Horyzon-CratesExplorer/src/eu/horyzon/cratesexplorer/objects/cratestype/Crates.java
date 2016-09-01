package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.TreeSet;

import org.bukkit.Material;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public abstract class Crates {
	protected String id;
	protected int spanTime, repeat;
	protected double pourcentToSpawn;
	protected boolean firework, particleSpawn;
	protected Material material;
	protected TreeSet<Reward> rewards = new TreeSet<Reward>();

	public static Crates getCrates(String id){
		for(Crates showCase : CratesExplorer.showCases) {
			if(showCase.id.equals(id))
				return showCase;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public int getSpanTime() {
		return spanTime;
	}

	public Material getMaterial() {
		return material;
	}

	public double getPourcent() {
		return pourcentToSpawn;
	}

	public Boolean hasRepeat() {
		return repeat > 0;
	}

	public int getRepeat() {
		return repeat;
	}

	public abstract TreeSet<?> getCrates();

	public abstract Boolean isCrates(Object showCase);

	public abstract void spawnCrates(int amount);

	public abstract void spawnRandomCrates();

	public abstract void spawnAllCrates();

	public abstract void unspawnCrates();

	public abstract void playAnimation();
}