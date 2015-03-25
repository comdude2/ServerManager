package net.mcviral.dev.plugins.servermanager.security.bans;

import java.util.UUID;

public class Ban {
	
	private UUID uuid = null;
	private String reason = null;
	private boolean permanent = false;
	private long bannedAt = 0L;
	private long expires = 0L;
	
	public Ban(){
		
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public long getBannedAt() {
		return bannedAt;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}
	
	
	
}
