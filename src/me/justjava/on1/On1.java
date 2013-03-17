package me.justjava.on1;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class On1 extends JavaPlugin{
	public static On1 instance;
	@Override
	public void onEnable(){
		instance = this;
		
		
		
		if(!getConfig().isSet("MySQL.User")){
			 getConfig().set("MySQL.User","USER");
			 getConfig().set("MySQL.Pass","PASS");
			 getConfig().set("MySQL.Host","HOST");
			 getConfig().set("MySQL.DB","DB");
			 On1.instance.getConfig().set("Item.Helm",310);
			 On1.instance.getConfig().set("Item.Brust",311);
			 On1.instance.getConfig().set("Item.Hose",312);
			 On1.instance.getConfig().set("Item.Schuh",313);
			 List<String> l = new ArrayList<String>();
			 l.add("276-1");
			 On1.instance.getConfig().set("Item.Items", l);
			 
			 saveConfig();
		}
		MySQL.connect();
		this.getServer().getPluginManager().registerEvents(new On1Listener(), this);
		this.getCommand("1on1").setExecutor(new Command1on1());
		MySQL.Update("CREATE TABLE IF NOT EXISTS 1on1_stats(name varchar(30), gewonnen int, verloren int)");
		
		
		
	}
	
	@Override
	public void onDisable(){
		for(Player p : On1Data.in1on1){
			On1Data.leave(p);
		}
		
	}
	
}
