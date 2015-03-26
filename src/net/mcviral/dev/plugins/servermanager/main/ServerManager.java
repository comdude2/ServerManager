package net.mcviral.dev.plugins.servermanager.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.mcviral.dev.plugins.servermanager.util.ErrorReporter;
import net.mcviral.dev.plugins.servermanager.util.Log;
//import net.mcviral.dev.plugins.servermanager.util.Mailer;
import net.mcviral.dev.plugins.servermanager.security.SecurityManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ServerManager extends JavaPlugin{
	
	public Log log = null;
	public ErrorReporter errorreporter = null;
	private SecurityManager security = null;
	private Listeners listeners = null;
	private WorldGuardPlugin worldguard = null;
	private boolean loadedBefore = false;
	
	public void onEnable(){
		log = new Log(this.getDescription().getName());
		errorreporter = new ErrorReporter(this, log, "errors/");
		listeners = new Listeners(this);
		worldguard = null;
		this.saveDefaultConfig();
		makeDirectories();
		security = new SecurityManager(this);
		if (!loadedBefore){
			this.getServer().getPluginManager().registerEvents(listeners, this);
		}
		worldguard = isworldGuardLoaded();
		if (worldguard == null){
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		for (Player p : this.getServer().getOnlinePlayers()){
			this.getSecurity().loadUser(p.getUniqueId());
		}
		this.getLogger().info(this.getDescription().getName() + " Enabled!");
	}
	
	public void onDisable(){
		this.getLogger().info(this.getDescription().getName() + " Disabled!");
	}
	
	private WorldGuardPlugin isworldGuardLoaded() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	
	public WorldGuardPlugin getWorldGuard(){
		return worldguard;
	}
	
	public void makeDirectories(){
		File folder = new File(this.getDataFolder() + "/data/");
		if (!folder.exists()){
			folder.mkdirs();
		}
		folder = new File(this.getDataFolder() + "/DB/upload/");
		if (!folder.exists()){
			folder.mkdirs();
		}
		folder = new File(this.getDataFolder() + "/DB/download/");
		if (!folder.exists()){
			folder.mkdirs();
		}
		folder = new File(this.getDataFolder() + "/security/commands/");
		if (!folder.exists()){
			folder.mkdirs();
		}
	}
	
	public SecurityManager getSecurity(){
		return security;
	}
	
	private boolean exists(String path){
		File f = new File("");
		String p = f.getAbsolutePath() + "/" + path;
		f = new File(p);
		if (f.exists()){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private void saveLibraries(JavaPlugin p, String path) {
		File dfile = null;
		File dataFolder = null;
        dataFolder = new File(p.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
 
        if (dfile == null) {
            dfile = new File(dataFolder.getPath(), "location.yml");
        }
 
        InputStream inputStream = null;
        OutputStream outputStream = null;
 
        if (!exists(path)) {
            try {
                // read this file into InputStream
                inputStream = p.getResource("location.yml");
 
                // write the inputStream to a FileOutputStream
                outputStream = new FileOutputStream(dfile);
 
                int read = 0;
                byte[] bytes = new byte[1024];
 
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
 
                System.out.println("Done!");
 
            } catch (IOException e) {
                e.printStackTrace();
                this.getServer().getLogger().severe(ChatColor.RED + "Could not create location.yml!");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        // outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
 
                }
            }
 
        }
    }
	
	public String me(){
		return (ChatColor.WHITE + "[" + ChatColor.RED + "ServerManager" + ChatColor.WHITE + "] ");
	}
	
}
