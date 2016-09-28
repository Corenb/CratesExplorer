package eu.horyzon.cratesexplorer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.listeners.PlayerModify;
import eu.horyzon.cratesexplorer.objects.cratestype.Crate;
import eu.horyzon.cratesexplorer.objects.cratestype.RunnableCrate;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("modify")) {
				if (!(s instanceof Player)) {
					s.sendMessage(ChatColor.RED + "You must be in game to do this!");
					return false;
				}

				Player p = (Player) s;

				if (PlayerModify.modify.containsKey(p.getUniqueId())) {
					try {
						Crate crate = Crate.getCratesfromId(PlayerModify.modify.remove(p.getUniqueId()));
						if (crate instanceof RunnableCrate)
							((RunnableCrate) crate).start();
					} catch (NullPointerException e) {
					}
					p.sendMessage(ChatColor.RED + "Modify mode disabled");
				} else
					p.sendMessage(ChatColor.RED + "You're not in modify mode!");

				return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("modify")) {
				if (!(s instanceof Player)) {
					s.sendMessage(ChatColor.RED + "You must be in game to do this!");
					return false;
				}

				try {
					Player p = (Player) s;
					Crate crate = Crate.getCratesfromId(args[1]);

					p.sendMessage(ChatColor.GREEN + "Modify mode enabled for " + crate.getId());

					try {
						Crate lastCrate = Crate.getCratesfromId(PlayerModify.modify.put(p.getUniqueId(), crate.getId()));
						if (lastCrate instanceof RunnableCrate)
							((RunnableCrate) lastCrate).start();
						if (crate instanceof RunnableCrate)
							((RunnableCrate) crate).stop();
					} catch (NullPointerException e) {
					}
				} catch (IllegalArgumentException e) {
					s.sendMessage(ChatColor.RED + "Can't find showCase!");
				}

				return true;
			}
		}

		s.sendMessage(ChatColor.YELLOW + "/" + label + " modify {type}" + ChatColor.GRAY + " Add or remove showcase");
		return false;
	}
}