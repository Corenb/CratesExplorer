package eu.horyzon.cratesexplorer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public class ParseUtils {

	public static Location parseLocation(String[] l) {
		return new Location(Bukkit.getWorld(l[0]), Integer.parseInt(l[1]), Integer.parseInt(l[2]), Integer.parseInt(l[3]));
	}

	public static String parseLocation(Location loc) {
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		return String.join(":", world, Integer.toString(x), Integer.toString(y), Integer.toString(z));
	}

	public static EulerAngle parseAngle(String[] a) {
		return EulerAngle.ZERO.add(Double.parseDouble(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]));
	}

	public static String parseAngle(EulerAngle a) {
		return String.join("%", Double.toString(a.getX()), Double.toString(a.getY()), Double.toString(a.getZ()));
	}
}