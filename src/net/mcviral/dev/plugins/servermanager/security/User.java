package net.mcviral.dev.plugins.servermanager.security;

import java.util.UUID;

public class User {
	
	private UUID uuid = null;
	private int oobInfractions = 0;
	
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
	
}
