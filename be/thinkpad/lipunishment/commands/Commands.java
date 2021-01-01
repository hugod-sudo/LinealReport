package be.thinkpad.lipunishment.commands;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import be.thinkpad.lipunishment.LiPunishmentMain;
import fr.maxtherobot.inventory.InventoryAPI;

@SuppressWarnings("unused")
public class Commands implements CommandExecutor {
	String playerOffline = "§cErreur : ce joueur est hors ligne.";
	public static Inventory inv;
	public static String reportInvName = "Report de null";
	public static int isGuiUsed = 0;
	public static Player reported;
	public static String server;
	private String noPerm = "§cVous n'avez pas la permission d'utiliser cette commande";
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("report")) {
				if(args.length == 1) {
					if(Bukkit.getPlayer(args[0]) == null) {
						player.sendMessage(playerOffline);
					}
					else {
						if(isGuiUsed == 0) {
							reportInvName = "Report de " + args[0];
							reported = Bukkit.getPlayer(args[0]);
							server = Bukkit.getServerName();
							inv = new InventoryAPI(reportInvName, 9, LiPunishmentMain.reportMenu).create();
							player.openInventory(inv);
							isGuiUsed = 1;
						}
						else {
							player.sendMessage("§cErreur : Un joueur utilise déjà le gui de report. Veuillez réessayer plus tard ou utiliser la commande /report <joueur> en spécifiant le type de cheat.");
						}
					}
				}
				else if(args.length == 2) {
					if(Bukkit.getPlayer(args[0]) == null) {
						player.sendMessage(playerOffline);
					}
					else  {
						if(args[1].equalsIgnoreCase("killaura")) {
							LiPunishmentMain.broadcastReported(2, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("reach")) {
							LiPunishmentMain.broadcastReported(0, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("antikb")) {
							LiPunishmentMain.broadcastReported(1, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("aimbot")) {
							LiPunishmentMain.broadcastReported(3, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("fly")) {
							LiPunishmentMain.broadcastReported(4, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("skin")) {
							LiPunishmentMain.broadcastReported(5, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("macro")) {
							LiPunishmentMain.broadcastReported(6, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						else if(args[1].equalsIgnoreCase("alliance")) {
							LiPunishmentMain.broadcastReported(7, Bukkit.getPlayer(args[0]).getName(), server, player.getName());
						}
						
 					}
				}
				return true;
			}
			if(cmd.getName().equalsIgnoreCase("freeze")) {
				if(!(player.hasPermission("punishment.freeze"))) {
					player.sendMessage(noPerm);
				}
				else {
					if(args.length != 1) {
						player.sendMessage("§cUsage : /freeze <joueur>");
					}
					else {
						if(Bukkit.getPlayer(args[0]) == null) {
							player.sendMessage(playerOffline);
						}
						else {
							if(Bukkit.getPlayer(args[0]).hasPermission("punishment.freezebypass")) {
								player.sendMessage("§cErreur : ce joueur ne peut pas être freeze.");
							}
							else {
								if(LiPunishmentMain.freezedPlayers.contains(Bukkit.getPlayer(args[0]))) {
									LiPunishmentMain.freezedPlayers.remove(Bukkit.getPlayer(args[0]));
									player.sendMessage("§a" + args[0] + " a été un-freeze !");
									Bukkit.getPlayer(args[0]).closeInventory();
									Bukkit.getPlayer(args[0]).removePotionEffect(PotionEffectType.BLINDNESS);
								}
								else {
									LiPunishmentMain.freezedPlayers.add(Bukkit.getPlayer(args[0]));
									player.sendMessage("§a" + args[0] + " a été freeze !");
									Bukkit.getPlayer(args[0]).openInventory(LiPunishmentMain.freezed);
									Bukkit.getPlayer(args[0]).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 2));
									Bukkit.getPlayer(args[0]).sendMessage("§c§lVous avez été freeze !");
									Bukkit.getPlayer(args[0]).sendMessage("§c§lVous avez été freeze !");
									Bukkit.getPlayer(args[0]).sendMessage("§c§lVous avez été freeze !");
									Bukkit.getPlayer(args[0]).sendMessage("§c§lVous avez été freeze !");
									Bukkit.getPlayer(args[0]).sendMessage("§c§lVous avez été freeze !");
								}
							}
						}
					}
				}
				return true;
			}
		}		
		return false;
	}

}

