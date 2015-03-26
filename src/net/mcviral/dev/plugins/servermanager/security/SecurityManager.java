package net.mcviral.dev.plugins.servermanager.security;

import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

import net.mcviral.dev.plugins.servermanager.main.ServerManager;
import net.mcviral.dev.plugins.servermanager.security.bans.BanController;
import net.mcviral.dev.plugins.servermanager.security.commands.CommandController;
import net.mcviral.dev.plugins.servermanager.util.FileManager;

public class SecurityManager {
	
	private ServerManager server = null;
	private CommandController cmdcontroller = null;
	private BanController bancontroller = null;
	private LinkedList <User> users = new LinkedList <User> ();
	
	public SecurityManager(ServerManager server){
		this.server = server;
		this.users = new LinkedList <User> ();
		cmdcontroller = new CommandController(server);
	}
	
	public CommandController getCommandController(){
		return cmdcontroller;
	}
	
	public BanController getBanController(){
		return bancontroller;
	}
	
	public LinkedList <User> getUsers() {
		return users;
	}
	
	public void setUsers(LinkedList <User> users) {
		this.users = users;
	}
	
	public User getUser(UUID uuid){
		for (User u : users){
			if (u.getUUID().equals(uuid)){
				return u;
			}
		}
		return null;
	}
	
	public User createUser(UUID uuid){
		File folder = new File(server.getDataFolder() + "/users/" + uuid.toString() + "/");
		if (!folder.exists()){
			folder.mkdirs();
		}
		@SuppressWarnings("unused")
		FileManager fm = new FileManager(server, "users/" + uuid.toString() + "/", "userdetails");
		return null;
	}
	
	public User loadUser(UUID uuid){
		return null;
	}
	
	public void saveUser(User u){
		
	}
	
}
