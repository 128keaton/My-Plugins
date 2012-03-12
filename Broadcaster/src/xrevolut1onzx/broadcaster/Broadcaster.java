package xrevolut1onzx.broadcaster;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xrevolut1onzx.broadcaster.commands.CommandHandler;
import xrevolut1onzx.broadcaster.commands.ConsoleCommandHandler;

public class Broadcaster extends JavaPlugin
{	
	/**
	 * Contains the current permissions type
	 * based on what's received from the config file
	 */
	private String permissionType;
	/** True if using SuperPerms */
	private Boolean usingSuperPerms;
	/** True if using Op */
	private Boolean usingOp;
	
	/** Used to log all events and prints to console */
	private Logger log = Logger.getLogger("Minecraft");
	/** The string that goes before every message that's sent to the console */
	private final String LOG_PREFIX = "[Broadcaster] ";
	
	/** Number of messages currently being broadcasted */
	private int numberOfMessages;
	/** All of the messages in an array */
	private BroadcastMessage[] messages;
	
	/** Handles the commands from the console */
	private ConsoleCommandHandler consoleCommandHandler = new ConsoleCommandHandler(this);
	/** Handles the commands from the players */
	private CommandHandler commandHandler = new CommandHandler(this);
	
	/** Called when the plugin starts up */
	public void onEnable()
	{
		manageConfigFile();
		checkPermissionType();
		handleRecurringMessage();
		log("Enabled");
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
				final BroadcastMessage broadcastMessage = messages[i];
				final int messageNumber = i + 1; // Message number according to the end-user, not the number of the slot in the array
				int timeDelayInTicks = messages[i].getDelay() * 1200;
				long offset = ((long) messages[i].getOffsetDelay()) * 20; // Gets the offset and multiplies by 1 tick (20)
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
								else if (usingOp)
								{
									if (broadcastMessage.getOpSeesMessage())
									{
										if (player.isOp())
										{
											player.sendMessage(finalMessage);
										}
									}
									if (broadcastMessage.getPlayerSeesMessage())
									{
										if (!player.isOp())
										{
											player.sendMessage(finalMessage);
										}
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
				}, offset, timeDelayInTicks);
			}
		}
	}
	
	/** 
	 * Sets the permission type based on what's received
	 * from the config file. Only call after the permissionType
	 * has been set, not before
	 */
	public void checkPermissionType() {
		usingOp = false;
		usingSuperPerms = false;
		if (permissionType == null)
		{
			log("Please reload the plugin to start using it");
			return;
		}
		if (permissionType.equalsIgnoreCase("SuperPerms"))
			usingSuperPerms = true;
		else if (permissionType.equalsIgnoreCase("Op"))
			usingOp = true;
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
		int o;
		Boolean oSM;
		Boolean pSM;
		
		for (int i = 0; i < numberOfMessages; i++)
		{
			u = getConfig().getBoolean("Message" + (i + 1) + ".Is-In-Use");
			m = getConfig().getString("Message" + (i + 1) + ".Message");
			d = getConfig().getInt("Message" + (i + 1) + ".Delay-in-minutes");
			r = getConfig().getBoolean("Message" + (i + 1) + ".Recurring-broadcast");
			o = getConfig().getInt("Message" + (i + 1) + ".Offset-delay");
			oSM = getConfig().getBoolean("Message" + (i + 1) + ".Op.Op-sees-message");
			pSM = getConfig().getBoolean("Message" + (i + 1) + ".Op.Player-sees-message");
			
			messages[i] = new BroadcastMessage(u, m, d, r, o, oSM, pSM);
		}
	}
	
	/** Gets the current permission type */
	public String getPermissionType()
	{
		return permissionType;
	}
	
	/** Is true if the permission type is Op */
	public Boolean isUsingOp()
	{
		return usingOp;
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
	 * Gets the BroadcastMessage according to its position in the array
	 * @param i The position in the array for the requested BroadcastMessage
	 * @return The requested BroadcastMessage
	 */
	public BroadcastMessage getBroadcastMessage(int i)
	{
		return messages[i];
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
	public int numberOfOnlinePlayers()
	{
		Player[] onlinePlayers = getServer().getOnlinePlayers();
		return onlinePlayers.length;
	}
	
}
