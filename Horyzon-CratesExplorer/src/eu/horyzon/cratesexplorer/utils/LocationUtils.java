package eu.horyzon.cratesexplorer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public class LocationUtils {

	public static Location parseLocation(String[] loc) {
		return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
	}

	public static Location parseLocationWithYaw(String[] loc) {
		return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5]));
	}

	public static String parseLocation(Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();

		return String.join(":", world,  Double.toString(x), Double.toString(y), Double.toString(z));
	}

	public static String parseLocationWithYaw(Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();

		return String.join(":", world, Double.toString(x), Double.toString(y), Double.toString(z), Float.toString(yaw), Float.toString(pitch));
	}

	public static EulerAngle parseAngle(String[] a) {
		return EulerAngle.ZERO.add(Double.parseDouble(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]));
	}

	public static String parseAngle(EulerAngle a) {
		return String.join("%", Double.toString(a.getX()), Double.toString(a.getY()), Double.toString(a.getZ()));
	}
}