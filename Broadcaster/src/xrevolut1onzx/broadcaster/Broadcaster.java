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
	
	private int numberOfMessages;
	private BroadcastMessage[] messages;
	
	/** Called when the plugin starts up */
	public void onEnable()
	{
		log("Initializing...");
		manageConfigFile();
		checkPermissionType();
		handleRecurringMessage();
		log("Initialized");
	}
	
	/** Called when the plugin needs to be reloaded */
	public void onReload()
	{
		log("Reloading...");
		getServer().getScheduler().cancelAllTasks();
		manageConfigFile();
		checkPermissionType();
		handleRecurringMessage();
		log("Reloaded");
	}
	
	/** Called when the plugin is disabled */
	public void onDisable()
	{
		log("Disabling...");
		getServer().getScheduler().cancelAllTasks();
		log("Disabled");
	}
	
	/** Called whenever a command is executed, either player or console */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		// TODO: Handle commands
		return false;
	}
	
	/**
	 * Handles the messages that happen more than once
	 */
	private void handleRecurringMessage()
	{
		for (int i = 0; i < numberOfMessages; i++)
		{
			if (messages[i].getInUse() && messages[i].getRecurring())
			{
				final String rawMessage = messages[i].getMessage();
				final int messageNumber = i + 1;
				int timeDelayInTicks = messages[i].getDelay() * 1200;
				getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
				{
					public void run()
					{
						if (rawMessage != null)
						{
							String finalMessage = new String(rawMessage.replaceAll("&([0-9a-f])", "\u00A7$1"));
							for (Player player : getServer().getOnlinePlayers())
							{
								if (usingSuperPerms)
								{
									if (!player.hasPermission("broadcastdonator.exemptfrommessage" + messageNumber))
									{
										player.sendMessage(finalMessage);
									}
								}
								else if (usingOP) {
									if (!player.isOp())
									{
										player.sendMessage(finalMessage);
									}
								}
							}
							log(finalMessage);
							log("Message broadcasted by repeater");
						}
						else
						{
							log("Reload the configuration file to send your message!");
						}
					}
				}, 60L, timeDelayInTicks);
			}
		}
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
				prop.put("Message1-Is-in-use", "true"); // value must be "true" or "TRUE" to actualy return true
				prop.put("Message1", "[Server] Enjoy this server? Consider donating to help fund it! Options available on our website.");
				prop.put("Message1-Time-between-messages-in-minutes", "30");
				prop.put("Message1-Recurring-Broadcast", "false");
				// second message
				prop.put("Message2-Is-in-use", "false"); // value must be "false" or "FALSE" to actually return false
				prop.put("Message2", "This is my second message");
				prop.put("Message2-Time-between-messages-in-minutes", "15");
				prop.put("Message2-Recurring-Broadcast", "true");
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
			numberOfMessages = Integer.parseInt(prop.getProperty("Number-of-messages"));
			
			messages = new BroadcastMessage[numberOfMessages];
			
			// begin code to retrieve messages from config
			Boolean[] u = new Boolean[numberOfMessages];
			String[] m = new String[numberOfMessages];
			int[] d = new int[numberOfMessages];
			Boolean[] r = new Boolean[numberOfMessages];
			for (int i = 0; i < numberOfMessages; i++)
			{
				u[i] = Boolean.parseBoolean(prop.getProperty("Message" + (i + 1) + "-Is-in-use"));
				m[i] = prop.getProperty("Message" + (i + 1));
				d[i] = Integer.parseInt(prop.getProperty("Message" + (i + 1) + "-Time-between-messages-in-minutes"));
				r[i] = Boolean.parseBoolean(prop.getProperty("Message" + (i + 1) + "-Recurring-Broadcast"));
				
				messages[i] = new BroadcastMessage(u[i], m[i], d[i], r[i]);
			}
			// end code for retrieving messages for config
			
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
	
	/** Gets the current permission type */
	public String getPermissionType()
	{
		return permissionType;
	}
	
	/** Is true if the permission type is OP */
	public Boolean isUsingOP()
	{
		return usingOP;
	}
	
	/** Is true if the permission type is SuperPerms */
	public Boolean isUsingSuperPerms()
	{
		return usingSuperPerms;
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
