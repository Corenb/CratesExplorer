package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public class ContainerCrate extends BlockCrate {

	public ContainerCrate(String id, Material type, Effect effect, int cooldownPlayer, int cooldownCrate, double pourcent,
			Set<Object> crates, Set<Reward> rewards) {
		super(id, type, effect, cooldownPlayer, cooldownCrate, pourcent, crates, rewards);
	}

	@Override
	protected String parseCrate(Object crate) {
		BlockState block = (BlockState) crate;
		return String.join(":", block.getWorld().getName(), Integer.toString(block.getX()),
				Integer.toString(block.getY()), Integer.toString(block.getZ()),
				((DirectionalContainer) block.getData()).getFacing().name());
	}
}