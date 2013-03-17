package me.justjava.on1;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class On1Data {
	public static HashMap<Player,ItemStack[]> inv = new HashMap<Player,ItemStack[]>();
	public static HashMap<Player,ItemStack[]> a = new HashMap<Player,ItemStack[]>();
	public static HashMap<Player,Location> loc = new HashMap<Player,Location>();
	public static List<Player> warteliste = new ArrayList<Player>();
	public static List<Player> in1on1 = new ArrayList<Player>();
	public static int Status = 0;
	
	public static void join(Player player, int t){
		player.setHealth(20);
		player.setFoodLevel(20);
		Location loc = new Location(Bukkit.getWorld(On1.instance.getConfig().getString("Spawn."+t+".world")), On1.instance.getConfig().getDouble("Spawn."+t+".x"), On1.instance.getConfig().getDouble("Spawn."+t+".y"), On1.instance.getConfig().getDouble("Spawn."+t+".z"));
		On1Data.inv.put(player, player.getInventory().getContents());
		On1Data.a.put(player, player.getInventory().getArmorContents());
		On1Data.loc.put(player, player.getLocation());
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[]{});
		player.teleport(loc);
		ItemStack[] is = new ItemStack[]{new ItemStack(On1.instance.getConfig().getInt("Item.Helm"),1),new ItemStack(On1.instance.getConfig().getInt("Item.Brust"),1),new ItemStack(On1.instance.getConfig().getInt("Item.Hose"),1),new ItemStack(On1.instance.getConfig().getInt("Item.Schuh"),1)};
		player.getInventory().setArmorContents(is);
		for(Iterator it = On1.instance.getConfig().getStringList("Item.Items").iterator();it.hasNext();){
			String[] s = it.next().toString().split("-");
			ItemStack is2 = new ItemStack(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
			player.getInventory().addItem(is2);
		}
		On1Data.in1on1.add(player);
		
	}
	
	public static void leave(Player player){
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[]{});
		On1Data.in1on1.remove(player);
		player.getInventory().setArmorContents(On1Data.a.get(player));
		On1Data.a.remove(player);
		player.getInventory().setContents(On1Data.inv.get(player));
		On1Data.inv.remove(player);
		player.teleport(On1Data.loc.get(player));
		On1Data.loc.remove(player);
		
	}
	public static void addWin(Player player){
//		MySQL.Update("INSERT INTO `1on1_stats` (`name`, `gewonnen`, `verloren`) VALUES ('"+player.getName()+"', '0', '0')");
		try{
			int win = 0;
			ResultSet rs = MySQL.Query("SELECT * FROM 1on1_stats WHERE `name`='"+player.getName()+"'");
			while(rs.next()){
				win = rs.getInt("gewonnen");
			}
			win++;
			//UPDATE `1on1_stats` SET `gewonnen` = '"+win+"' WHERE `1on1_stats`.`name` = '"+player.getName()+"'
			MySQL.Update("UPDATE `1on1_stats` SET `gewonnen` = '"+win+"' WHERE `1on1_stats`.`name` = '"+player.getName()+"'");
			
		}catch(Exception e){
			player.sendMessage(ChatColor.RED + "ERROR in der MySQL Verbindung! Melde diesen Fehler einem DEV");
			e.printStackTrace();
		}
	}
	
	public static void addLose(Player player){
		try{
			int win = 0;
			ResultSet rs = MySQL.Query("SELECT * FROM 1on1_stats WHERE `name`='"+player.getName()+"'");
			while(rs.next()){
				win = rs.getInt("verloren");
			}
			win++;
			//UPDATE `1on1_stats` SET `gewonnen` = '"+win+"' WHERE `1on1_stats`.`name` = '"+player.getName()+"'
			MySQL.Update("UPDATE `1on1_stats` SET `verloren` = '"+win+"' WHERE `1on1_stats`.`name` = '"+player.getName()+"'");
			
		}catch(Exception e){
			player.sendMessage(ChatColor.RED + "ERROR in der MySQL Verbindung! Melde diesen Fehler einem DEV");
			e.printStackTrace();
		}
		
	}
	
	
	
}
