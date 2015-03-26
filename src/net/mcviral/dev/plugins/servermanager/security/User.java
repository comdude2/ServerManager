package net.mcviral.dev.plugins.servermanager.security;

import java.util.LinkedList;
import java.util.UUID;

public class User {
	
	private UUID uuid = null;
	private int oobInfractions = 0;
	private LinkedList <String> commandsOnJoin = new LinkedList <String> ();
	
	public User(){
		
	}
	
	public UUID getUUID(){
		return uuid;
	}

	public int getOobInfractions() {
		return oobInfractions;
	}

	public void setOobInfractions(int oobInfractions) {
		this.oobInfractions = oobInfractions;
	}

	public LinkedList <String> getCommandsOnJoin() {
		return commandsOnJoin;
	}

	public void setCommandsOnJoin(LinkedList <String> commandsOnJoin) {
		this.commandsOnJoin = commandsOnJoin;
	}
	
}
