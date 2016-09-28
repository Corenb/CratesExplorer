package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.tasks.animations.AnimationPlayEntityOpener;

public class EntityCrate extends Crate {
	public static File dir = new File(CratesExplorer.getInstance().getDataFolder(), "armorstands");

	public EntityCrate(String id, EntityType type, Set<Object> crates, Set<Reward> rewards) {
		super.id = id;
		super.type = type;
		super.crates = crates;
		super.rewards = rewards;
	}

	@Override
	public boolean addCrate(Object crate) {
		return super.addCrate(((Entity) crate).getUniqueId(), dir);
	}

	@Override
	public boolean removeCrate(Object crate) {
		return super.removeCrate(((Entity) crate).getUniqueId(), dir);
	}

	@Override
	public void playAnimation(Object crate, Player player, Reward reward) {
		new AnimationPlayEntityOpener(this, (Entity) crate, player, reward);
	}

	@Override
	protected String parseCrate(Object crate) {
		return ((UUID) crate).toString();
	}
}