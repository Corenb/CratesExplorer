package eu.horyzon.cratesexplorer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ChestUtils {

	private static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String name = "net.minecraft.server." + version + nmsClassString;
		Class<?> nmsClass = Class.forName(name);
		return nmsClass;
	}

	private static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method getHandle = player.getClass().getMethod("getHandle");
		Object nmsPlayer = getHandle.invoke(player);
		Field conField = nmsPlayer.getClass().getField("playerConnection");
		Object conn = conField.get(nmsPlayer);
		return conn;
	}

	private static Object getBlockPosition(Location loc)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> nmsBlockPosition = getNMSClass("BlockPosition");
		Object nmsBlockPositionInstance = nmsBlockPosition
				.getConstructor(new Class[] { Double.TYPE, Double.TYPE, Double.TYPE })
				.newInstance(new Object[] { loc.getX(), loc.getY(), loc.getZ() });

		return nmsBlockPositionInstance;
	}

	private static void setChest(Player player, int open, Block bloc) {
		Location loc = bloc.getLocation();
		try {
			Class<?> nmsBlockPositionClass = getNMSClass("BlockPosition");
			Object nmsBlockPos = getBlockPosition(loc);
			Class<?> nmsPacketBlockAction = getNMSClass("PacketPlayOutBlockAction");
			Class<?> nmsBlock = getNMSClass("Block");
			Object nmsChest = getNMSClass("Blocks").getField(bloc.getType().toString()).get(null);
			;
			Class<?> nmsPacket = getNMSClass("Packet");

			Object nmsPackInstance = nmsPacketBlockAction
					.getConstructor(new Class[] { nmsBlockPositionClass, nmsBlock, Integer.TYPE, Integer.TYPE })
					.newInstance(new Object[] { nmsBlockPos, nmsChest, Integer.valueOf(1), Integer.valueOf(open) });
			Method sendPacket = getConnection(player).getClass().getMethod("sendPacket", nmsPacket);
			sendPacket.invoke(getConnection(player), nmsPackInstance);

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static void setChest(Block bloc, boolean open, double radius) {
		double d = radius * radius;

		for (Player player : bloc.getWorld().getPlayers()) {
			if (player.getLocation().add(0.0D, 0.85D, 0.0D).distanceSquared(bloc.getLocation()) <= d)
				setChest(player, open ? 1 : 0, bloc);
		}
	}
}