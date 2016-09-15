package eu.horyzon.cratesexplorer.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public final class ItemStackUtils {
	/*public static String getEnchants(ItemStack i) {
		List<String> e = new ArrayList<String>();
		Map<Enchantment, Integer> en = i.getEnchantments();
		for (Enchantment t : en.keySet()) {
			e.add(t.getName() + "%" + en.get(t));
		}
		return StringUtils.join(e, ",");
	}*/

	//@SuppressWarnings("deprecation")
	public static String deserialize(ItemStack i) {
		try {
			return itemStackToBase64(i);
		} catch (IllegalStateException | ClassNotFoundException | IOException e) {
			return null;
		}
		/*if (i == null)
			return null;

		String[] parts = new String[6];
		parts[0] = i.getType().name();
		parts[1] = Integer.toString(i.getAmount());
		parts[2] = String.valueOf(i.getDurability());
		parts[3] = i.getItemMeta() != null ? serializeIntoFormattingCodes(i.getItemMeta()) : null;
		parts[4] = String.valueOf(i.getData().getData());
		parts[5] = getEnchants(i);
		if (i.getType().equals(Material.SKULL_ITEM)) {

		}

		return StringUtils.join(parts, ";");*/
	}

	//@SuppressWarnings("deprecation")
	public static ItemStack serialize(String d) {
		try {
			return deserializeItemStack(d);
		} catch (IOException e) {
			return null;
		}
		/*if (p.equals(null))
			return null;

		String[] a = p.split(";");
		ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
		i.setDurability((short) Integer.parseInt(a[2]));

		if (i.getType().equals(Material.AIR))
			return i;

		i.setItemMeta(deserializeFromFormattingCodes(a[3]));
		MaterialData data = i.getData();
		data.setData((byte) Integer.parseInt(a[4]));
		i.setData(data);
		if (a.length > 5) {
			String[] parts = a[5].split(",");
			for (String s : parts) {
				String label = s.split("%")[0];
				String amplifier = s.split(":")[1];
				Enchantment type = Enchantment.getByName(label);
				if (type == null) {
					continue;
				}
				int f;
				try {
					f = Integer.parseInt(amplifier);
				} catch (Exception ex) {
					continue;
				}
				i.addEnchantment(type, f);
			}
		}
		return i;*/
	}

	public static ItemStack deserializeItemStack(String data) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
		try {
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack items = (ItemStack) dataInput.readObject();

			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		} finally {
			inputStream.close();
		}
	}

	public static String itemStackToBase64(ItemStack items) throws IllegalStateException, ClassNotFoundException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeObject(items);

			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save itemstack.", e);
		} finally {
			outputStream.close();
		}
	}
}