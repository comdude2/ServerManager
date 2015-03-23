package net.mcviral.dev.plugins.servermanager.security.commands;

import java.util.LinkedList;

import net.mcviral.dev.plugins.servermanager.main.ServerManager;

public class CommandController {
	
	@SuppressWarnings("unused")
	private ServerManager server = null;
	private LinkedList <RestrictedCommand> commands = new LinkedList <RestrictedCommand> ();
	
	public CommandController(ServerManager server){
		this.server = server;
	}
	
	public LinkedList <RestrictedCommand> getRestrictedCommands(){
		return commands;
	}
	
	public boolean addCommand(RestrictedCommand cmd){
		return false;
	}
	
	public boolean removeCommand(RestrictedCommand cmd){
		return false;
	}
	
	public boolean isRestricted(String command){
		for (RestrictedCommand cmd : commands){
			if (command.contains(cmd.getName())){
				return true;
			}
		}
		return false;
	}
	
}
