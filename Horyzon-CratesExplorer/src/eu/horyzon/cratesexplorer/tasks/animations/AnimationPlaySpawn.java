package eu.horyzon.cratesexplorer.tasks.animations;

import java.util.Set;

import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.RunnableCrate;

public class AnimationPlaySpawn extends BukkitRunnable {
	protected int i = 0;
	protected RunnableCrate crate;
	protected Set<BlockState> block;
	protected int iBlock = 15, iEffect = 0;

	public AnimationPlaySpawn(RunnableCrate crate, Set<BlockState> block) {
		this.crate = crate;
		this.block = block;

		runTaskTimerAsynchronously(CratesExplorer.getInstance(), 0, 1);
	}

	@Override
	public void run() {
		if (i == iEffect && crate.hasEffect())
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Object block : block)
						((BlockState) block).getLocation().getWorld().spigot().playEffect(
								((BlockState) block).getLocation().add(-0.5, 0.5, -0.5), crate.getEffect(), 0, 0, 0.1F, 0.1F, 0.1F,
								0.5F, 20, 30);
				}
			}.runTask(CratesExplorer.getInstance());

		if (i == iBlock)
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Object block : block)
						((BlockState) block).update(true);
				}
			}.runTask(CratesExplorer.getInstance());

		if (i > iBlock && i > iEffect)
			cancel();

		i++;
	}
}