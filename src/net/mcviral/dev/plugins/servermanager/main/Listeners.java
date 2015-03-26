package net.mcviral.dev.plugins.servermanager.main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.mcviral.dev.plugins.servermanager.security.User;
import net.mcviral.dev.plugins.servermanager.security.bans.Ban;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
		User u = server.getSecurity().loadUser(event.getPlayer().getUniqueId());
		if (u == null){
			u = server.getSecurity().createUser(event.getPlayer().getUniqueId());
		}
		if (event.getPlayer().hasPermission("sm.cmdonjoin")){
			if (u.getCommandsOnJoin() != null){
				if (u.getCommandsOnJoin().size() > 0){
					for (String cmd : u.getCommandsOnJoin()){
						event.getPlayer().chat(cmd);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		User u = server.getSecurity().getUser(event.getPlayer().getUniqueId());
		if (u != null){
			server.getSecurity().getUsers().remove(u);
			server.getSecurity().saveUser(u);
		}else{
			//User did not have an instance of User? Wut?
		}
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
											b = server.getSecurity().getBanController().banPlayer(event.getPlayer().getUniqueId(), new Date().getTime(), 0L, "AUTO-BAN: Going out of bounds (Hacking).", false, "ServerManager Plugin");
											event.getPlayer().kickPlayer("You have been banned.");
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInventoryOpen(InventoryOpenEvent event){
		if (event.getPlayer() instanceof Player){
			if (event.getInventory().getHolder() instanceof Chest){
				Chest chest = (Chest) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof DoubleChest){
				DoubleChest chest = (DoubleChest) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof Furnace){
				Furnace chest = (Furnace) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof Hopper){
				Hopper chest = (Hopper) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof BrewingStand){
				BrewingStand chest = (BrewingStand) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof Dispenser){
				Dispenser chest = (Dispenser) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof Sign){
				Sign chest = (Sign) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}else if (event.getInventory().getHolder() instanceof Dropper){
				Dropper chest = (Dropper) event.getInventory().getHolder();
				Location c = chest.getLocation();
				Location p = event.getPlayer().getLocation();
				boolean tooFar = cancelMove(p, c);
				if (tooFar){
					Player player = (Player) event.getPlayer();
					event.setCancelled(true);
					cancelInventory(player);
				}
			}
		}
	}
	
	private void cancelInventory(Player p){
		if (!p.hasPermission("sm.bypass.inviopen")){
			//Warn player and active admins.
			User u = server.getSecurity().getUser(p.getUniqueId());
			if (u == null){
				//user doesn't exist, u w0t?
			}
			if (u.getOobInfractions() >= 50){
				Ban b = null;
				b = server.getSecurity().getBanController().banPlayer(p.getUniqueId(), new Date().getTime(), 0L, "AUTO-BAN: Opening a container not within your reach (Hacking).", false, "ServerManager Plugin");
				p.kickPlayer("You have been banned.");
				if (b != null){
					server.getServer().broadcast(server.me() + ChatColor.YELLOW + p.getName() + ChatColor.RED + " has been banned for getting too many out of bounds infractions (True reason: opening a container more than 5 blocks away) (suspected hacking).", "sm.moderator");
				}
			}else{
				u.setOobInfractions(u.getOobInfractions() + 1);
				p.sendMessage(server.me() + ChatColor.RED + "You are interacting with an object greater than or equal to 5 blocks from your current location." + ChatColor.DARK_RED + " Out of bounds.");
				p.sendMessage(server.me() + ChatColor.RED + "If yo persist," + ChatColor.DARK_RED + " you could be banned automatically.");
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
