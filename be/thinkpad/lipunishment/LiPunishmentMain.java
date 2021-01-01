package be.thinkpad.lipunishment;


import java.util.UUID;
import java.util.logging.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import be.thinkpad.lipunishment.commands.Commands;
import fr.maxtherobot.inventory.InventoryAPI;
import fr.maxtherobot.inventory.ItemUtils;

public class LiPunishmentMain extends JavaPlugin implements PluginMessageListener {
	public static ItemStack Reach;
	public static ItemStack AntiKB;
	public static ItemStack KillAura;
	public static ItemStack Aimbot;
	public static ItemStack Fly;
	public static ItemStack Skin;
	public static ItemStack Macro;
	public static ItemStack Alliance;
	public static ItemStack quit;
	public static ItemStack freezeItem;
	public static HashMap<ItemStack, Integer> reportMenu = new HashMap<ItemStack, Integer>();
	public static HashMap<ItemStack, Integer> freezeMenu = new HashMap<ItemStack, Integer>();
	public static ArrayList<Player> freezedPlayers = new ArrayList<Player>();
	public static LiPunishmentMain instance;
	public static Inventory freezed;
	public static Logger logger;
	public static Plugin plugin;
	
	@Override
	public void onEnable() {
		System.out.println("LinealPunishment v1.1 par ThinkPad_ - Initialisation en cours...");
		buildReportGUI();
		buildFreezeGUI();
		System.out.println("ver.A02");
		getServer().getPluginManager().registerEvents((Listener) new Listeners(), this);
		getCommand("report").setExecutor(new Commands());
		getCommand("freeze").setExecutor(new Commands());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		LiPunishmentMain.instance=this;
		logger = Logger.getLogger(LiPunishmentMain.class.getName());
		plugin = this;
	}
	
	 @Override
	  public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	    if (!channel.equals("BungeeCord")) {
	    	logger.info("[LinealReport] Plugin message received. Channel is not BungeeCord. Giving up.");
	      return;
	    }
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    if (subchannel.equals("Report")) {
	    	logger.info("[LinealReport] Plugin report message received. Reading message...");
	    	short len = in.readShort();
	    	byte[] msgbytes = new byte[len];
	    	in.readFully(msgbytes);
	    	DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
	    	try {
				String somedata = msgin.readUTF();
				Bukkit.broadcast(somedata, "punishment.report.view");
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	  }
	 
	 public static void sendReportToServers(String message) {
		 ByteArrayDataOutput out = ByteStreams.newDataOutput();
		 out.writeUTF("Forward"); // So BungeeCord knows to forward it
		 out.writeUTF("ALL");
		 out.writeUTF("Report"); // The channel name to check if this your data

		 ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		 DataOutputStream msgout = new DataOutputStream(msgbytes);
		 try {
		 msgout.writeUTF(message); // You can do anything you want with msgoutS
		 } catch (IOException exception){
		 exception.printStackTrace();
		 }

		 out.writeShort(msgbytes.toByteArray().length);
		 out.write(msgbytes.toByteArray());
		 Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		  player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	 }

	public static void buildReportGUI() {
		Reach = ItemUtils.getItem(Material.IRON_AXE, "§aReach", 0, true);
		AntiKB = ItemUtils.getItem(Material.WEB, "§aAnti-KB", 0, true);
		KillAura = ItemUtils.getItem(Material.DIAMOND_SWORD, "§aKillAura", 0, true);
		Aimbot = ItemUtils.getItem(Material.BOW, "§aBow-Aimbot", 0, true);
		Fly = ItemUtils.getItem(Material.FEATHER, "§aFly", 0, true);
		Skin = ItemUtils.getItem(Material.SKULL_ITEM, "§aSkin incorrect", 3, true);
		Macro = ItemUtils.getItem(Material.COMMAND, "§aMacro", 0, true);
		Alliance = ItemUtils.getItem(Material.STICK, "§aAlliance", 0, true);
		quit = ItemUtils.getItem(Material.BARRIER, "§cQuitter", 0, false);
		reportMenu.put(Reach, 0);
		reportMenu.put(AntiKB, 1);
		reportMenu.put(KillAura, 2);
		reportMenu.put(Aimbot, 3);
		reportMenu.put(Fly, 4);
		reportMenu.put(Skin, 5);
		reportMenu.put(Macro, 6);
		reportMenu.put(Alliance, 7);
		reportMenu.put(quit, 8);
		
		}
	public static void buildFreezeGUI() {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§c§lVous avez été freeze !");
		lore.add("§cVeuillez passer sur le discord");
		lore.add("§c(discord.gg/QX3NFdz)");
		lore.add("§cpour négocier votre sanction.");
		freezeItem = ItemUtils.getAdvencedItem(Material.BARRIER, "!!!", 0, new HashMap<Enchantment, Integer>(), lore, new ArrayList<ItemFlag>());
		freezeMenu.put(freezeItem, 13);
		freezed = new InventoryAPI("§cLineal Punishment - Freeze", 27, freezeMenu).create();
		
	}
	
	

	public static void broadcastReported(int type, String name, String server, String reporter) {
				if(type == 0) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Reach", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Reach");
				}
				else if(type == 1) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6AntiKB", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6AntiKB");
				}
				else if(type == 2) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6KillAura", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6KillAura");
				}
				else if(type == 3) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Aimbot", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Aimbot");
				}
				else if(type == 4) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Fly", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Fly");
				}
				else if(type == 5) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Skin incorrect", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Skin incorrect");
				}
				else if(type == 6) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Macro", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Macro");
				}
				else if(type == 7) {
					Bukkit.broadcast("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Alliance", "punishment.report.view");
					sendReportToServers("§5 >>REPORT §9"+reporter+" §c a report §b" + name + " §c dans le serveur §e" + server + " §cpour §6Alliance");
				}
							
	}
	public static String getUUID(Player player) {
		UUID u = player.getUniqueId();
		String uu = u.toString();
		String uuid = uu.replace("-", "");
		return uuid;
	}
	public static String getOfflineUUID(String args) {
		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(args);
		String u = player.getUniqueId().toString();
		String uuid = u.replace("-", "");
		return uuid;
	}
	
	public static LiPunishmentMain getInstance() {
		return instance;
	}
	
	@Override
	public void onDisable() {
	}
}