package eu.horyzon.cratesexplorer.commands;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import eu.horyzon.cratesexplorer.tasks.SpawnTask;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("info")) {
				s.sendMessage(ChatColor.GOLD + "---====== Chest info ======---");
				for (Crates showCase : CratesExplorer.showCases) {
					int spawned = 0;
					int size = showCase.getShowCase().size();
					Set<?> cases = showCase.getShowCase();

					for (Object c : cases) {
						if (showCase.isShowCase(c))
							spawned++;
					}

					s.sendMessage(ChatColor.GOLD + "  " + showCase.getId() + ":");
					s.sendMessage(ChatColor.GOLD + "    Material: " + showCase.getMaterial().name());
					s.sendMessage(ChatColor.GOLD + "    Amount:");
					s.sendMessage(ChatColor.GREEN + "      Spawned: " + spawned);
					s.sendMessage(ChatColor.RED + "      Not spawned: " + (size - spawned));
					s.sendMessage(ChatColor.YELLOW + "      Total: " + size);
				}
				s.sendMessage(ChatColor.GOLD + "      ================");

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
					for (Crates showCase : CratesExplorer.showCases) {
						showCase.unspawnShowCase();
						showCase.spawnRandomShowCase();
					}
				} else {
					Crates showCase = Crates.getShowCase(args[1]);

					if (showCase != null) {
						showCase.unspawnShowCase();
						showCase.spawnRandomShowCase();
					} else {
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
					}
				}

				return true;
			} else if (args[1].equalsIgnoreCase("start")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (SpawnTask task : CratesExplorer.tasks.values()) {
						if (!task.isRun())
							task.start();
					}
				} else {
					SpawnTask task = CratesExplorer.tasks.get(args[1]);

					if (task != null && !task.isRun())
						task.start();
				}

				return true;
			} else if (args[1].equalsIgnoreCase("stop")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates showCase : CratesExplorer.showCases) {
						showCase.unspawnShowCase();
						showCase.spawnRandomShowCase();
					}
				} else {
					Crates showCase = Crates.getShowCase(args[1]);

					if (showCase != null) {
						showCase.unspawnShowCase();
						showCase.spawnRandomShowCase();
					} else
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
				}

				return true;
			} else if (args[1].equalsIgnoreCase("restart")) {
				if (args[1].equalsIgnoreCase("all")) {
					for (Crates showCase : CratesExplorer.showCases) {
						showCase.unspawnShowCase();
						showCase.spawnRandomShowCase();
					}
				} else {
					Crates showCase = Crates.getShowCase(args[1]);

					if (showCase != null) {
						showCase.unspawnShowCase();
						showCase.spawnRandomShowCase();
					} else
						s.sendMessage(ChatColor.RED + "Can't find showCase!");
				}

				return true;
			} else if (args[0].equalsIgnoreCase("modify")) {
				if (!(s instanceof Player)) {
					s.sendMessage(ChatColor.RED + "You must be in game to do this!");
					return false;
				}

				Player p = (Player) s;
				Crates showCase = Crates.getShowCase(args[1]);

				if (showCase == null) {
					p.sendMessage(ChatColor.RED + "Can't find this ShowCase!");
					return false;
				}

				if (CratesExplorer.modify.containsKey(p.getUniqueId()))
					CratesExplorer.modify.put(p.getUniqueId(), args[1]);
				else
					CratesExplorer.modify.replace(p.getUniqueId(), args[1]);
				p.sendMessage(ChatColor.GREEN + "Modify mode enabled for " + args[1]);

				return true;
			} else if (args[0].equalsIgnoreCase("unspawn")) {
				if(args[1].equalsIgnoreCase("all")) {
					for (Crates showCase : CratesExplorer.showCases) {
						showCase.unspawnShowCase();
					}
				} else {
					Crates showCase = Crates.getShowCase(args[1]);

					if (showCase == null)
						s.sendMessage(ChatColor.RED + "Can't find this ShowCase!");
					else
						showCase.unspawnShowCase();
				}

				return true;
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("spawn")) {
				if (args[1].equalsIgnoreCase("all")) {
					if(args[2].equalsIgnoreCase("all")) {
						for(Crates showCase : CratesExplorer.showCases)
							showCase.spawnAllShowCase();
					} else {
						int amount;

						try {
			                amount = Integer.parseInt(args[2]);
			            } catch (NumberFormatException e) {
			                s.sendMessage(ChatColor.RED + args[1] + " isn't a number!");
			                return false;
			            }

						for(Crates showCase : CratesExplorer.showCases) {
							showCase.spawnShowCase(amount);
						}
					}
				} else {
					Crates showCase = Crates.getShowCase(args[1]);

					if (showCase == null)
						s.sendMessage(ChatColor.RED + "Can't find this ShowCase!");
					if(args[2].equalsIgnoreCase("all")) {
						showCase.spawnAllShowCase();
					} else {
						try {
			                showCase.spawnShowCase(Integer.parseInt(args[1]));
			            } catch (NumberFormatException e) {
			                s.sendMessage(ChatColor.RED + args[1] + " n'est pas un nombre!");
			            }
					}
				}

				return true;
			}
		}

		s.sendMessage(ChatColor.YELLOW + "          ----==== LobbyExplorer ====----");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " info" + ChatColor.GRAY + " Get showcase informations");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " modify {type}" + ChatColor.GRAY + " Add or remove showcase");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " [respawn] [all|type]" + ChatColor.GRAY + " Respawn showcase");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " [spawn|unspawn] [all|type] [all|amount]" + ChatColor.GRAY + " Spawn/unspawn showcase");
		s.sendMessage(ChatColor.YELLOW + "/" + label + " [start|stop|restart] [all|type]" + ChatColor.GRAY + " Start/stop/restart showcase task");
		return false;
	}
}