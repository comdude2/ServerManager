package net.mcviral.dev.plugins.servermanager.main;

import net.mcviral.dev.plugins.servermanager.util.ErrorReporter;
import net.mcviral.dev.plugins.servermanager.util.Log;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerManager extends JavaPlugin{
	
	public Log log = new Log(this.getDescription().getName());
	public ErrorReporter errorreporter = new ErrorReporter(this, log, "errors/");
	private SecurityManager security = null;
	
	public void onEnable(){
		security = new SecurityManager();
	}
	
	public void onDisable(){
		
	}
	
	public SecurityManager getSecurity(){
		return security;
	}
	
}
