package net.mcviral.dev.plugins.servermanager.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.mcviral.dev.plugins.servermanager.util.ErrorReporter;
import net.mcviral.dev.plugins.servermanager.util.Log;
//import net.mcviral.dev.plugins.servermanager.util.Mailer;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerManager extends JavaPlugin{
	
	public Log log = new Log(this.getDescription().getName());
	public ErrorReporter errorreporter = new ErrorReporter(this, log, "errors/");
	private SecurityManager security = null;
	
	public void onEnable(){
		this.saveDefaultConfig();
		//jarSetup();
		security = new SecurityManager();
		this.getLogger().info(this.getDescription().getName() + " Enabled!");
	}
	
	public void onDisable(){
		this.getLogger().info(this.getDescription().getName() + " Disabled!");
	}
	
	public SecurityManager getSecurity(){
		return security;
	}
	
	//public Mailer getMailer(){
		//return mailer;
	//}
	
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
	
}
