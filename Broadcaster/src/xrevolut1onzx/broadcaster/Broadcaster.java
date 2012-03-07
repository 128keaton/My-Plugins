package xrevolut1onzx.broadcaster;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Broadcaster extends JavaPlugin
{	
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
	
	/** Number of messages currently being broadcasted */
	private int numberOfMessages;
	/** All of the messages in an array */
	private BroadcastMessage[] messages;
	
	/** Handles the commands for the console */
	private ConsoleCommandHandler consoleCommandHandler = new ConsoleCommandHandler(this);
	/** Handles the commands for the players */
	private CommandHandler commandHandler = new CommandHandler(this);
	
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
		this.saveConfig();
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
		this.saveConfig();
		log("Disabled");
	}
	
	/** Called whenever a command is executed, either player or console */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		
		if (player == null) // console commands
		{
			if (cmd.getName().equalsIgnoreCase("broadcaster"))
			{
				if (args.length == 0) // no arguments
					return false;
				else if (args.length == 1)
				{
					if (args[0].equalsIgnoreCase("reload"))
					{
						consoleCommandHandler.reload();
						return true;
					}
					return false;
				}
				else if (args.length == 2)
				{
					if (args[0].equalsIgnoreCase("preview"))
					{
						consoleCommandHandler.preview(args[1]);
						return true;
					}
					else if (args[0].equalsIgnoreCase("broadcast"))
					{
						consoleCommandHandler.broadcast(args[1]);
						return true;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		else // player commands
		{
			if (cmd.getName().equalsIgnoreCase("broadcaster"))
			{
				Player commandTyper = (Player) sender;
				if (args.length == 0) // no arguments
					return false;
				else if (args.length == 1)
				{
					if (args[0].equalsIgnoreCase("reload"))
					{
						commandHandler.reload(commandTyper);
					}
					return false;
				}
				else if (args.length == 2)
				{
					if (args[0].equalsIgnoreCase("preview"))
					{
						commandHandler.preview(commandTyper, args[1]);
						return true;
					}
					else if (args[0].equalsIgnoreCase("broadcast"))
					{
						commandHandler.broadcast(commandTyper, args[1]);
						return true;
					}
					return false;
				}
				return false;
			}
			return false;
		}
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
							if (numberOfOnlinePlayers() == 0)
							{
								return;
							}
							String finalMessage = new String(rawMessage.replaceAll("&([0-9a-f])", "\u00A7$1"));
							for (Player player : getServer().getOnlinePlayers())
							{
								if (usingSuperPerms)
								{
									if (!player.hasPermission("broadcaster.exemptfrommessage" + messageNumber))
									{
										player.sendMessage(finalMessage);
									}
								}
								else if (usingOP)
								{
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
	 * Manages everything to do with the configuration file
	 */
	public void manageConfigFile()
	{
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		permissionType = getConfig().getString("Permission-type");
		numberOfMessages = getConfig().getInt("Number-of-messages");
		
		messages = new BroadcastMessage[numberOfMessages];
		
		Boolean u;
		String m;
		int d;
		Boolean r;
		
		for (int i = 0; i < numberOfMessages; i++)
		{
			u = getConfig().getBoolean("Message" + (i + 1) + ".Is-In-Use");
			m = getConfig().getString("Message" + (i + 1) + ".Message");
			d = getConfig().getInt("Message" + (i + 1) + ".Delay-in-minutes");
			r = getConfig().getBoolean("Message" + (i + 1) + ".Recurring-broadcast");
			
			messages[i] = new BroadcastMessage(u, m, d, r);
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
	
	/** Gets the String of the message requested */
	public String getMessage(int i)
	{
		return messages[i].getMessage();
	}
	
	/** Gets the number of messages currently in the system */
	public int getNumberOfMessages()
	{
		return numberOfMessages;
	}
	
	/** 
	 * Used to print to the console
	 * Automatically adds the plugin's prefix
	 */
	public void log(String m)
	{
		log.info(LOG_PREFIX + m);
	}
	
	/** Returns the number of online players */
	public int numberOfOnlinePlayers() {
		Player[] onlinePlayers = getServer().getOnlinePlayers();
		return onlinePlayers.length;
	}
	
}
