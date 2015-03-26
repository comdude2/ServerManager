package net.mcviral.dev.plugins.servermanager.security.bans;

import java.util.LinkedList;
import java.util.UUID;

import net.mcviral.dev.plugins.servermanager.main.ServerManager;

public class BanController {
	
	@SuppressWarnings("unused")
	private ServerManager server = null;
	private LinkedList <Ban> bans = new LinkedList <Ban> ();
	
	public BanController(ServerManager server){
		this.server = server;
		this.setBans(new LinkedList <Ban> ());
	}
	
	public Ban banPlayer(UUID uuid, long bannedAt, long expires, String reason, boolean permanent, String bannedBy){
		return null;
	}
	
	public boolean isBanned(UUID uuid){
		return false;
	}
	
	public Ban getBannedPlayer(UUID uuid){
		return null;
	}

	public LinkedList <Ban> getBans() {
		return bans;
	}

	public void setBans(LinkedList <Ban> bans) {
		this.bans = bans;
	}
	
}
