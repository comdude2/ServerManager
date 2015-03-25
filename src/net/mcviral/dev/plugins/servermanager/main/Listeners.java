package net.mcviral.dev.plugins.servermanager.main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.mcviral.dev.plugins.servermanager.security.User;
import net.mcviral.dev.plugins.servermanager.security.bans.Ban;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Listeners implements Listener{
	
	private ServerManager server = null;
	
	public Listeners(ServerManager server){
		this.server = server;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event){
		if (server.getSecurity().getBanController().isBanned(event.getPlayer().getUniqueId())){
			Ban b = server.getSecurity().getBanController().getBannedPlayer(event.getPlayer().getUniqueId());
			if (b != null){
				if (b.isPermanent()){
					event.disallow(Result.KICK_BANNED, "You are banned permanently for: " + b.getReason());
				}else{
					event.disallow(Result.KICK_BANNED, "You are banned for: " + b.getReason() + " until: " + timestampToString(b.getExpires()));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event){
		if (server.getSecurity().getBanController().isBanned(event.getPlayer().getUniqueId())){
			Ban b = server.getSecurity().getBanController().getBannedPlayer(event.getPlayer().getUniqueId());
			if (b != null){
				if (b.isPermanent()){
					event.setReason("You are banned permanently for: " + b.getReason());
				}else{
					event.setReason("You are banned for: " + b.getReason() + " until: " + timestampToString(b.getExpires()));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event){
		if (!event.isCancelled()){
			if (!event.getPlayer().hasPermission("sm.bypass.movecheck")){
				Location from = event.getFrom();
				Location to = event.getTo();
				boolean cancel = false;
				cancel = cancelMove(from, to);
				event.setCancelled(cancel);
				if (!cancel){
					ApplicableRegionSet set = server.getWorldGuard().getRegionManager(to.getWorld()).getApplicableRegions(to);
					if (set != null){
						if (set.size() > 0){
							for (ProtectedRegion r : set.getRegions()){
								if (r.getId().contains("OOB")){
									if (!event.getPlayer().hasPermission("sm.bypass.oob")){
										//Warn player and active admins.
										User u = server.getSecurity().getUser(event.getPlayer().getUniqueId());
										if (u == null){
											//user doesn't exist, u w0t?
										}
										if (u.getOobInfractions() >= 50){
											Ban b = null;
											b = server.getSecurity().getBanController().banPlayer();
											event.getPlayer().kickPlayer("");
											if (b != null){
												server.getServer().broadcast(server.me() + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED + " has been banned for getting too many out of bounds infractions (suspected hacking).", "sm.moderator");
											}
										}else{
											u.setOobInfractions(u.getOobInfractions() + 1);
											event.getPlayer().sendMessage(server.me() + ChatColor.RED + "You are not authorised to enter this area, it has been deemed" + ChatColor.DARK_RED + " Out of bounds.");
											event.getPlayer().sendMessage(server.me() + ChatColor.RED + "If yo persist, you will be teleported away and" + ChatColor.DARK_RED + " you could be banned automatically.");
										}
										cancel = true;
										break;
									}
								}
							}
						}else{
							//No regions
						}
					}else{
						//No regions
					}
				}
				if (cancel){
					server.getServer().broadcast(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED + " moved 5 blocks or more, this could be a sign that they're hacking.", "sm.admin");
				}
			}
		}
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
	
	public boolean cancelMove(Location from, Location to){
		int totalBlocks = 0;
		totalBlocks += Math.abs(from.getBlockX()-to.getBlockX());
		totalBlocks += Math.abs(from.getBlockY()-to.getBlockY());
		totalBlocks += Math.abs(from.getBlockZ()-to.getBlockZ());
		if (totalBlocks >= 5){
			//Hacker!
			return true;
		}
		return false;
	}
	
	private String timestampToString(long input){
        Date date = new Date(input);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return(cal.get(Calendar.YEAR)
                + "/" + cal.get(Calendar.MONTH)
                + "/" + cal.get(Calendar.DATE)
                + " " + cal.get(Calendar.HOUR)
                + ":" + cal.get(Calendar.MINUTE)
                + ":" + cal.get(Calendar.SECOND)
                + (cal.get(Calendar.AM_PM)==0?"AM":"PM")
                );
 
    }
	
}
