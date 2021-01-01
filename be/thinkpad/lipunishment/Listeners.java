package be.thinkpad.lipunishment;

import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.avaje.ebeaninternal.server.expression.LuceneAwareExpression;

import be.thinkpad.lipunishment.commands.Commands;
import fr.maxtherobot.inventory.InventoryAPI;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("unused")
public class Listeners implements Listener {
	private Inventory r = new InventoryAPI("§cLinealPunishment - Freeze", 27, LiPunishmentMain.freezeMenu).create();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) throws ClassNotFoundException, SQLException {
		Player player = event.getPlayer();
		String uuid = LiPunishmentMain.getOfflineUUID(player.getName());
		event.setJoinMessage(null);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) throws ClassNotFoundException, SQLException {
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		if(player.getOpenInventory().getTitle().contains("Report de")) {
			if(clicked.getType().equals(Material.IRON_AXE)) {
				LiPunishmentMain.broadcastReported(0, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				Commands.isGuiUsed = 0;
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
			}
			else if(clicked.getType().equals(Material.WEB)) {
				LiPunishmentMain.broadcastReported(1, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.DIAMOND_SWORD)) {
				LiPunishmentMain.broadcastReported(2, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.BOW)) {
				LiPunishmentMain.broadcastReported(3, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.FEATHER)) {
				LiPunishmentMain.broadcastReported(4, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.SKULL_ITEM)) {
				LiPunishmentMain.broadcastReported(5, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.COMMAND)) {
				LiPunishmentMain.broadcastReported(6, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.STICK)) {
				LiPunishmentMain.broadcastReported(7, Commands.reported.getName(), Commands.server, player.getName());
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				player.sendMessage("§aMerci d'avoir signalé ce joueur. §cRappel : tout abus sera santionné.");
				Commands.isGuiUsed = 0;
			}
			else if(clicked.getType() == null) return;
		}
		else if(player.getOpenInventory().getTitle().contains("Lineal Punishment - Freeze")) {
			if(clicked.getType().equals(Material.BARRIER)) {
				player.sendMessage("§c§lVous avez été freeze !");
				event.setCancelled(true);
			}
			else if(clicked.getType() == null) return;
		}
		else if(player.getOpenInventory().getTitle().contains("Logs de")) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if(inv.getTitle().contains("Report de")) {
			Commands.isGuiUsed = 0;
		}
	}
	@EventHandler
	public void onFreezeClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getPlayer();
		if(LiPunishmentMain.freezedPlayers.contains(player)) {
			if(inv.getTitle().contains("Freeze")) {
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				new BukkitRunnable() {

					@Override
					public void run() {
						player.openInventory(LiPunishmentMain.freezed);						
					}
					
				}.runTaskLater(LiPunishmentMain.getInstance(), 2);
				
				return;
			}
		}
		return;
	}
	
}
