package net.mcviral.dev.plugins.servermanager.main;

import net.mcviral.dev.plugins.servermanager.util.ErrorReporter;
import net.mcviral.dev.plugins.servermanager.util.Log;
import net.mcviral.dev.plugins.servermanager.util.Mailer;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerManager extends JavaPlugin{
	
	public Log log = new Log(this.getDescription().getName());
	public ErrorReporter errorreporter = new ErrorReporter(this, log, "errors/");
	private SecurityManager security = null;
	private Mailer mailer = new Mailer(this, "admin@mcviral.net", "smtp.office365.com:587", "admin@mcviral.net", "Budgie123bb123=");
	
	public void onEnable(){
		this.saveDefaultConfig();
		this.getLogger().info("Test 1");
		security = new SecurityManager();
		log.info("Testing email...");
		mailer.sendMail("comdude2@msn.com", "Alert", "Test alert!");
		this.getLogger().info(this.getDescription().getName() + " Enabled!");
	}
	
	public void onDisable(){
		this.getLogger().info(this.getDescription().getName() + " Disabled!");
	}
	
	public SecurityManager getSecurity(){
		return security;
	}
	
	public Mailer getMailer(){
		return mailer;
	}
	
}
