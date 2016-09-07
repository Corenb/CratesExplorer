package eu.horyzon.cratesexplorer.commands;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("info")) {
				s.sendMessage(ChatColor.GOLD + "---====== Chest info ======---");
				for (Crates crates : Crates.cratesList) {
					int spawned = 0;
					int size = crates.getCrates().size();
					Set<Object> cases = crates.getCrates();

					for (Object c : cases) {
						if (crates.isCrates(c))
							spawned++;
					}

					s.sendMessage(ChatColor.GOLD + "  " + crates.getId() + ":");
					s.sendMessage(ChatColor.GOLD + "    Material: " + crates.getMaterial().name());
					s.sendMessage(ChatColor.GOLD + "    Amount:");
					s.sendMessage(ChatColor.GREEN + "      Spawned: " + spawned);
					s.sendMessage(ChatColor.RED + "      Not spawned: " + (size - spawned));
					s.sendMessage(ChatColor.YELLOW + "      Total: " + size);
				}
				s.sendMessage(ChatColor.GOLD + "============================");

				return true;
			} else if (args[0].equalsIgnoreCase("modify")) {
				if (!(s instanceof Player)) {
					s.sendMessage(ChatColor.RED + "You must be in game to do this!");
					return false;
				}

				Player p = (Player) s;

				if (CratesExplorer.modify.containsKey(p.getUniqueId())) {
					CratesExplorer.modify.remove(p.getUniqueId());
					p.sendMessage(ChatColor.RED + "Modify mode disabled");
				} else
					p.sendMessage(ChatColor.RED + "You're not in modify mode!");

				return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("respawn")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates crate : Crates.cratesList)
						crate.respawnCrates();
				} else {
					try {
						Crates.getCrates(args[1]).respawnCrates();
					} catch (IllegalArgumentException e) {
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
					}
				}

				return true;
			} else if (args[1].equalsIgnoreCase("start")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates crate : Crates.cratesList) {
						if (!crate.isRun())
							crate.start();
					}
				} else {
					try {
						Crates crate = Crates.getCrates(args[1]);

						if (!crate.isRun())
							crate.start();
					} catch (IllegalArgumentException e) {
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
					}
				}

				return true;
			} else if (args[1].equalsIgnoreCase("stop")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates crate : Crates.cratesList)
						crate.respawnCrates();
				} else {
					try {
						Crates.getCrates(args[1]).respawnCrates();
					} catch (IllegalArgumentException e) {
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
					}
				}

				return true;
			} else if (args[1].equalsIgnoreCase("restart")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates crate : Crates.cratesList)
						crate.respawnCrates();
				} else {
					try {
						Crates.getCrates(args[1]).respawnCrates();
					} catch (IllegalArgumentException e) {
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
					}
				}

				return true;
			} else if (args[0].equalsIgnoreCase("modify")) {
				if (!(s instanceof Player)) {
					s.sendMessage(ChatColor.RED + "You must be in game to do this!");
					return false;
				}

				try {
					Player p = (Player) s;
					Crates crate = Crates.getCrates(args[1]);

					if (CratesExplorer.modify.containsKey(p.getUniqueId()))
						CratesExplorer.modify.put(p.getUniqueId(), crate.getId());
					else
						CratesExplorer.modify.replace(p.getUniqueId(), crate.getId());
					p.sendMessage(ChatColor.GREEN + "Modify mode enabled for " + crate.getId());
				} catch (IllegalArgumentException e) {
					s.sendMessage(ChatColor.RED + "Can't find showCase!");
				}

				return true;
			} else if (args[0].equalsIgnoreCase("unspawn")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates crate : Crates.cratesList)
						crate.unspawnCrates();
				} else {
					try {
						Crates.getCrates(args[1]).unspawnCrates();
					} catch (IllegalArgumentException e) {
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
					}
				}

				return true;
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("spawn")) {
				if (args[1].equalsIgnoreCase("all")) {
					if (args[2].equalsIgnoreCase("all")) {
						for (Crates crate : Crates.cratesList)
							crate.spawnAllCrates();
					} else {
						try {
							int amount = Integer.parseInt(args[2]);

							for (Crates crate : Crates.cratesList)
								crate.spawnCrates(amount);
						} catch (NumberFormatException e) {
							s.sendMessage(ChatColor.RED + args[1] + " isn't a number!");
						}
					}
				} else {
					try {
						Crates crate = Crates.getCrates(args[1]);

						if (args[2].equalsIgnoreCase("all")) {
							crate.spawnAllCrates();
							;
						} else {
							try {
								crate.spawnCrates(Integer.parseInt(args[1]));
							} catch (NumberFormatException e) {
								s.sendMessage(ChatColor.RED + args[1] + " n'est pas un nombre!");
							}
						}
					} catch (NumberFormatException e) {
						s.sendMessage(ChatColor.RED + "Can't find this ShowCase!");
					}
				}

				return true;
			}
		}

		s.sendMessage(ChatColor.YELLOW + "---------========== LobbyExplorer =========---------");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " info" + ChatColor.GRAY + " Get showcase informations");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " modify {type}" + ChatColor.GRAY + " Add or remove showcase");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " [respawn] [all|type]" + ChatColor.GRAY + " Respawn showcase");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " [spawn|unspawn] [all|type] [all|amount]" + ChatColor.GRAY
				+ " Spawn/unspawn showcase");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " [start|stop|restart] [all|type]" + ChatColor.GRAY
				+ " Start/stop/restart showcase task");
		return false;
	}
}