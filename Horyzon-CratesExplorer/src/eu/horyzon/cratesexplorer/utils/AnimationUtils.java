package eu.horyzon.cratesexplorer.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.tasks.AnimArmorstand;
import net.md_5.bungee.api.ChatColor;

public class AnimationUtils {

	public static void createArmorStand(Location loc, String gain) {
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

		as.setCustomName(ChatColor.GOLD + "+ " + gain);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		as.setGravity(false);

		new AnimArmorstand(as).runTaskTimerAsynchronously(CratesExplorer.getInstance(), 0, 1);
	}
}