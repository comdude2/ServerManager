package net.mcviral.dev.plugins.servermanager.main;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Listeners implements Listener{
	
	private ServerManager server = null;
	
	public Listeners(ServerManager server){
		this.server = server;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event){
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event){
		
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event){
		
	}
	
	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event){
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(final AsyncPlayerChatEvent event){
		server.log.info(event.getPlayer().getName() + ": " + ChatColor.BLUE + event.getMessage());
		for (Player p : server.getServer().getOnlinePlayers()){
			if (p.hasPermission("sm.spy")){
				//p.sendMessage(event.getPlayer().getName() + ": " + ChatColor.BLUE + event.getMessage());
			}
		}
	}
	
}
