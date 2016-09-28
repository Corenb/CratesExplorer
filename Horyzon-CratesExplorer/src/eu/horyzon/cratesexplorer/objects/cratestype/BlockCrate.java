package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.tasks.animations.AnimationPlayBlockOpener;

public class BlockCrate extends RunnableCrate {
	public static File dir = new File(CratesExplorer.getInstance().getDataFolder(), "containers");

	public BlockCrate(String id, Material type, Effect effect, int cooldownPlayer, int cooldownCrate, double pourcent,
			Set<Object> crates, Set<Reward> rewards) {
		super(id, type, effect, cooldownPlayer, cooldownCrate, pourcent, crates, rewards);
	}

	@Override
	public boolean addCrate(Object crate) {
		return super.addCrate(crate, dir);
	}

	@Override
	public boolean removeCrate(Object crate) {
		return super.removeCrate(crate, dir);
	}

	@Override
	public void playAnimation(Object crate, Player player, Reward reward) {
		new AnimationPlayBlockOpener(this, (BlockState) crate, player, reward);
	}

	@Override
	public void unspawnCrates() {
		for (Object block : getCrates()) {
			if (!PlayerExplore.isInUse(block))
				unspawnCrate((BlockState) block);
		}
	}

	@Override
	public void unspawnCrate(BlockState crate) {
		crate.getBlock().setType(Material.AIR);
	}

	@Override
	protected String parseCrate(Object crate) {
		BlockState block = (BlockState) crate;
		return String.join(":", block.getWorld().getName(), Integer.toString(block.getX()),
				Integer.toString(block.getY()), Integer.toString(block.getZ()));
	}
}