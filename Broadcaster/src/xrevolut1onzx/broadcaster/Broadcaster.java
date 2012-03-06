package xrevolut1onzx.broadcaster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Broadcaster extends JavaPlugin
{
	
	/** The string version of the root of the plugin's directory */
	private final String MAIN_DIRECTORY = "plugins/Broadcaster";
	/** The configuration file */
	private File config = new File(MAIN_DIRECTORY + File.separator + "config.yml");
	private Properties prop = new Properties();
	
	/**
	 * Contains the current permissions type
	 * based on what's received from the config file
	 */
	private String permissionType;
	/** True if using SuperPerms */
	private Boolean usingSuperPerms;
	/** True if using OP */
	private Boolean usingOP;
	
	/** Used to log all events and prints to console */
	private Logger log = Logger.getLogger("Minecraft");
	/** The string that goes before every message that's sent to the console */
	private final String LOG_PREFIX = "[Broadcaster] ";
	
	/** Called when the plugin starts up */
	public void onEnable()
	{
		log("Initializing...");
		// TODO: Handle initialization
		log("Initialized");
	}
	
	/** Called when the plugin needs to be reloaded */
	public void onReload()
	{
		log("Reloading...");
		// TODO: Handle reloading
		log("Reload complete");
	}
	
	/** Called when the plugin is disabled */
	public void onDisable()
	{
		log("Disabling...");
		// TODO: Handle disabling (shouldn't be much)
		log("Disabled");
	}
	
	/** Called whenever a command is executed, either player or console */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		// TODO: Handle commands
		return false;
	}
	
	/** 
	 * Sets the permission type based on what's received
	 * from the config file. Only call after the permissionType
	 * has been set, not before
	 */
	public void checkPermissionType() {
		usingOP = false;
		usingSuperPerms = false;
		if (permissionType == null)
		{
			log("Please reload the plugin to start using it");
			return;
		}
		if (permissionType.equalsIgnoreCase("SuperPerms"))
			usingSuperPerms = true;
		else if (permissionType.equalsIgnoreCase("OP"))
			usingOP = true;
	}
	
	/**
	 * Manages the configuration file
	 * Creates one with default parameters if one doesn't exist
	 * and otherwise loads the existing one
	 */
	public void manageConfigFile()
	{
		new File(MAIN_DIRECTORY).mkdir(); // Creates the plugin's folder
		if (!config.exists())
		{
			try
			{
				config.createNewFile();
				FileOutputStream output = new FileOutputStream(config);
				prop.put("Permission-Manager", "SuperPerms");
				prop.put("Number-of-messages", "2");
				// first message
				prop.put("Message1", "[Server] Enjoy this server? Consider donating to help fund it! Options available on our website.");
				prop.put("Message1-Recurring-Broadcast", "false");
				prop.put("Message1-Time-between-messages-in-minutes", "30");
				// second message
				prop.put("Message2", "This is my second message");
				prop.put("Message2-Recurring-Broadcast", "true");
				prop.put("Message2-Time-between-messages-in-minutes", "15");
				prop.store(output, "Edit the configurations to your liking"); // generic exit comment and creates the configuration file
				output.flush();
				output.close();
				log("Configuration file created. Please configure and reload it.");
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			loadConfigFile();
		}
	}
	
	/** Loads the configuration file */
	public void loadConfigFile()
	{
		try
		{
			FileInputStream input = new FileInputStream(config);
			prop.load(input);
			permissionType = prop.getProperty("Permission-Manager");
			// TODO: Handle message variables
			input.close();
		}
		catch (FileNotFoundException ex)
		{
			manageConfigFile(); // Since there's no file, manageConfigFile() will make a new one
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/** 
	 * Used to print to the console
	 * Automatically adds the plugin's prefix
	 */
	private void log(String m)
	{
		log.info(LOG_PREFIX + m);
	}
	
	/** Returns the number of online players */
	public int numberOfOnlinePlayers() {
		Player[] onlinePlayers = getServer().getOnlinePlayers();
		return onlinePlayers.length;
	}
	
}
