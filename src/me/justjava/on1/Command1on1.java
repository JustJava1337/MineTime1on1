package me.justjava.on1;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command1on1 implements CommandExecutor {
//	1on1_stats(name varchar(30), gewonnen int, verlorenn int)
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,
			String[] args) {
		
		
		if(!(sender instanceof Player)){return false;}
		Player player = (Player) sender;
		if(args.length == 0){
			if(player.hasPermission("1on1.admin")){
				player.sendMessage(ChatColor.RED + "Verwendung: /1on1 <join|leave|stats|set1|set2>");
			}else{
				player.sendMessage(ChatColor.RED + "Verwendung: /1on1 <join|leave|stats>");
			}
		}else{
			if(args[0].equalsIgnoreCase("join")){
				if(!On1.instance.getConfig().isSet("Spawn.1.world")){
					player.sendMessage(ChatColor.RED + "Es wurde noch keine Arena gesetzt!");
					return false;
				}
				if(On1Data.Status == 0){
					On1Data.join(player, 1);
					On1Data.Status = 1;
					player.sendMessage(ChatColor.RED + "Du must noch euf einen 2. Spieler Warten!");
				}else if(On1Data.Status == 1){
					On1Data.join(player, 2);
					On1Data.Status = 2;
					for(Player f : On1Data.in1on1){
						f.sendMessage(ChatColor.GREEN + "Der Kamfp beginnt!");
					}
				}else{
					if(On1Data.warteliste.contains(player)){
						player.sendMessage(ChatColor.RED + "Du bist bereits in der Warteliste!");
						return false;
					}
					On1Data.warteliste.add(player);
					player.sendMessage(ChatColor.GREEN + "Du bist Platz " + ChatColor.GOLD + On1Data.warteliste.size() + ChatColor.GREEN + " in der Warteliste.");
				}
			}else if(args[0].equalsIgnoreCase("leave")){
				if(On1Data.in1on1.contains(player)){
					if(On1Data.Status == 1){
						On1Data.Status =0;
						On1Data.leave(player);
						return false;
					}
					On1Data.leave(player);
					Player f = On1Data.in1on1.get(0);
					Bukkit.broadcastMessage(ChatColor.GOLD + "[1on1] " + ChatColor.AQUA + f.getName() + " hat gegen " + player.getName() + " gewonnen!");
					On1Data.addWin(f);
					On1Data.addLose(player);
					On1Data.leave(f);
					if(On1Data.warteliste.size() == 1){
						On1Data.join(On1Data.warteliste.get(0), 1);
						On1Data.warteliste.remove(0);
						On1Data.Status =1;
					}else if(On1Data.warteliste.size() >=2){
						On1Data.join(On1Data.warteliste.get(0), 1);
						On1Data.join(On1Data.warteliste.get(1), 2);
						On1Data.warteliste.remove(On1Data.in1on1.get(0));
						On1Data.warteliste.remove(On1Data.in1on1.get(1));
						On1Data.Status =2;
					}else{
						On1Data.Status =0;
					}
				}else if(On1Data.warteliste.contains(player)){
					On1Data.warteliste.remove(player);
					player.sendMessage(ChatColor.GREEN + "Du hast die Warteliste verlassen.");
				}else{
					player.sendMessage(ChatColor.RED + "Du bist weder in der Warteliste noch in der Arena!");
				}
			}else if(args[0].equalsIgnoreCase("stats")){
				
				try{
//					1on1_stats(name varchar(30), gewonnen int, verloren int)
					ResultSet rs = MySQL.Query("SELECT * FROM 1on1_stats WHERE `name`='"+player.getName()+"'");
					while(rs.next()){
						player.sendMessage(ChatColor.GOLD + "[]=====[1on1]=====[]");
						player.sendMessage(ChatColor.AQUA + "Gewonnen: " + rs.getInt("gewonnen"));
						player.sendMessage(ChatColor.AQUA + "Verloren: " + rs.getInt("verloren"));
					}
					
				}catch(Exception e){
					player.sendMessage(ChatColor.RED + "ERROR in der MySQL Verbindung! Melde diesen Fehler einem DEV");
					e.printStackTrace();
				}
				
			}else if(args[0].equalsIgnoreCase("set1")){
				if(!player.hasPermission("1on1.admin")){return false;}
				On1.instance.getConfig().set("Spawn.1.world",player.getLocation().getWorld().getName());
				On1.instance.getConfig().set("Spawn.1.x",player.getLocation().getX());
				On1.instance.getConfig().set("Spawn.1.y",player.getLocation().getY());
				On1.instance.getConfig().set("Spawn.1.z",player.getLocation().getZ());
				
				On1.instance.saveConfig();
				player.sendMessage(ChatColor.GREEN + "Der Spawnpunkt für Player 1 wurde gesetzt!");
			}else if(args[0].equalsIgnoreCase("set2")){
				if(!player.hasPermission("1on1.admin")){return false;}
				On1.instance.getConfig().set("Spawn.2.world",player.getLocation().getWorld().getName());
				On1.instance.getConfig().set("Spawn.2.x",player.getLocation().getX());
				On1.instance.getConfig().set("Spawn.2.y",player.getLocation().getY());
				On1.instance.getConfig().set("Spawn.2.z",player.getLocation().getZ());
				
				On1.instance.saveConfig();
				player.sendMessage(ChatColor.GREEN + "Der Spawnpunkt für Player 2 wurde gesetzt!");
			}
			
			
		}
		
		
		
		return false;
	}

}
