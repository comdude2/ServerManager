package net.mcviral.dev.plugins.servermanager.security.commands;

public class RestrictedCommand {
	
	private String name = null;
	private boolean levelBased = false;
	private int level = -1;
	private boolean permissionBased = false;
	private String permission = null;
	private boolean adminBased = false;
	private boolean invisible = false;
	
	public RestrictedCommand(String name, boolean invisible){
		this.setName(name);
		this.setInvisible(invisible);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLevelBased() {
		return levelBased;
	}

	public void setLevelBased(boolean levelBased) {
		this.levelBased = levelBased;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isPermissionBased() {
		return permissionBased;
	}

	public void setPermissionBased(boolean permissionBased) {
		this.permissionBased = permissionBased;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean isAdminBased() {
		return adminBased;
	}

	public void setAdminBased(boolean adminBased) {
		this.adminBased = adminBased;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
}
