package me.justjava.on1;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class On1Listener implements Listener {
	
	@EventHandler
	public void Command(PlayerCommandPreprocessEvent event){
		if(On1Data.in1on1.contains(event.getPlayer())){
			if(!event.getMessage().startsWith("/1on1 leave")){
			event.getPlayer().sendMessage(ChatColor.RED + "Im 1on1 darfst du nur /1on1 leave benutzen");
			event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		try{
			boolean b = false;

			ResultSet rs = MySQL.Query("SELECT * FROM 1on1_stats WHERE `name`='"+player.getName()+"'");
			while(rs.next()){
				b = true;
			}
			if(!b){
				MySQL.Update("INSERT INTO `1on1_stats` (`name`, `gewonnen`, `verloren`) VALUES ('"+player.getName()+"', '0', '0')");
			}
		}catch(Exception e){
			player.sendMessage(ChatColor.RED + "ERROR in der MySQL Verbindung! Melde diesen Fehler einem DEV");
			e.printStackTrace();
		}
		
	}
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(On1Data.in1on1.contains(player)){
				if(player.getHealth()-event.getDamage()<=0){
					event.setCancelled(true);
					On1Data.leave(player);
					Player f = On1Data.in1on1.get(0);
					On1Data.leave(f);
					Bukkit.broadcastMessage(ChatColor.GOLD + "[1on1] " + ChatColor.AQUA + f.getName() + " hat gegen " + player.getName() + " gewonnen!");
					On1Data.addWin(f);
					On1Data.addLose(player);
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
					
					
					
				}
				
			}
			
			
		}
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if(On1Data.in1on1.contains(event.getPlayer())){
			if(On1Data.Status == 1){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "Du must noch auf einen 2. Spieler warten!");
			}
		}
		
	}
	
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		
		if(On1Data.in1on1.contains(event.getPlayer())){
			event.getPlayer().chat("/1on1 leave");
		}
		if(On1Data.warteliste.contains(event.getPlayer())){
			On1Data.warteliste.remove(event.getPlayer());
		}
		
	}
	
	@EventHandler
	public void onkick(PlayerKickEvent event){
		
		if(On1Data.in1on1.contains(event.getPlayer())){
			event.getPlayer().chat("/1on1 leave");
		}
		if(On1Data.warteliste.contains(event.getPlayer())){
			On1Data.warteliste.remove(event.getPlayer());
		}
		
	}
	
}
