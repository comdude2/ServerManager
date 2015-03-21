package net.mcviral.dev.plugins.servermanager.security;

import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

import net.mcviral.dev.plugins.servermanager.main.ServerManager;
import net.mcviral.dev.plugins.servermanager.security.commands.CommandController;
import net.mcviral.dev.plugins.servermanager.util.FileManager;

public class SecurityManager {
	
	private ServerManager server = null;
	@SuppressWarnings("unused")
	private CommandController cmdcontroller = null;
	private LinkedList <User> users = new LinkedList <User> ();
	
	public SecurityManager(ServerManager server){
		this.server = server;
		cmdcontroller = new CommandController(server);
	}
	
	public LinkedList <User> getUsers() {
		return users;
	}
	
	public void setUsers(LinkedList <User> users) {
		this.users = users;
	}
	
	public void createUser(UUID uuid){
		File folder = new File(server.getDataFolder() + "/users/" + uuid.toString() + "/");
		if (!folder.exists()){
			folder.mkdirs();
		}
		@SuppressWarnings("unused")
		FileManager fm = new FileManager(server, "users/" + uuid.toString() + "/", "userdetails");
	}
	
}
