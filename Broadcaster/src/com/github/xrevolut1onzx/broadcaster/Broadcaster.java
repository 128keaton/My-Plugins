package com.github.xrevolut1onzx.broadcaster;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xrevolut1onzx.broadcaster.commands.CommandMethods;


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
	
	/** Contains all of the methods used to execute the commands*/
	private CommandMethods commandMethods = new CommandMethods(this);
	
	/** Called when the plugin starts up */
	public void onEnable()
	{
		pluginMetrics(this);
		manageConfigFile();
		checkPermissionType();
		handleRecurringMessage();
		log("Enabled");
	}
	
	/** Called when the plugin needs to be reloaded */
	public void onReload()
	{
		log("Reloading...");
		cancelAllBroadcasterMessages();
		manageConfigFile();
		checkPermissionType();
		handleRecurringMessage();
		log("Reloaded");
	}
	
	/** Called when the plugin is disabled */
	public void onDisable()
	{
		cancelAllBroadcasterMessages();
		log("Disabled");
	}
	
	/** Called whenever a command is executed, either player or console */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("broadcaster"))
		{
			if (args.length == 0)
				return false;
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("reload"))
				{
					commandMethods.reload(player);
					return true;
				}
				return false;
			}
			else if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("preview"))
				{
					commandMethods.preview(player, args[1]);
					return true;
				}
				else if (args[0].equalsIgnoreCase("broadcast"))
				{
					commandMethods.broadcast(player, args[1]);
					return true;
				}
				return false;
			}
			return false;
		}
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
				String message = messages[i].getMessage();
				BroadcastMessage broadcastMessage = messages[i];
				int messageNumber = i + 1;
				int timeDelayInTicks = messages[i].getDelay() * 1200;
				long offset = messages[i].getOffsetDelay() * 20;
				scheduleRecurringMessage(message, broadcastMessage, messageNumber, timeDelayInTicks, offset);
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
		final String CONFIG_FILE_PATH = "plugins/" + this.getName() + "/config.yml";
		File file = new File(CONFIG_FILE_PATH);
		if (!file.exists())
		{
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
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
	 * Schedules a recurring message using Bukkit's async task scheduler and assigns the BroadcastMessage the corresponding task ID returned from the scheduler
	 * @param rawMessage The message drawn straight from the configuration file, still needs to be parsed for colors
	 * @param broadcastMessage The BroadcastMessage associated with what's being scheduled
	 * @param messageNumber The message number according to the end-user, not the number of what's in the array
	 * @param timeDelayInTicks The time delay between messages
	 * @param offset The initial delay between plugin initiation and the first message in ticks, not seconds
	 */
	public void scheduleRecurringMessage(final String rawMessage, final BroadcastMessage broadcastMessage, final int messageNumber, int timeDelayInTicks, long offset)
	{
		broadcastMessage.setTaskNumber(getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				if (rawMessage != null)
				{
					if (numberOfOnlinePlayers() == 0)
					{
						return;
					}
					String finalMessage = replaceColors(rawMessage);
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
		}, offset, timeDelayInTicks));
	}
	
	/**
	 * Cancels all of the BroadcastMessage tasks
	 */
	private void cancelAllBroadcasterMessages()
	{
		for(int i = 0; i < messages.length; i++)
		{
			cancelBroadcasterMessage(i);
		}
	}
	
	/**
	 * Cancels a specific BroadcasterMessage
	 * @param i The position in the array of BroadcasterMessages
	 */
	private void cancelBroadcasterMessage(int i)
	{
		getServer().getScheduler().cancelTask(messages[i].getTaskNumber());
	}
	
	/**
	 * Handles the usage tracking for this plugin
	 * @param plugin The Broadcaster instance to pass in
	 */
	private void pluginMetrics(Broadcaster plugin)
	{
		try
		{
			Metrics metrics = new Metrics(plugin);
			metrics.start();
		}
		catch (IOException e)
		{
			// Failed to submit the stats :-(
		}
	}
	
	/** 
	 * Used to print to the console
	 * Automatically adds the plugin's prefix
	 */
	public void log(String m)
	{
		log.info(LOG_PREFIX + m);
	}
	
	public String replaceColors(String message)
	{
		return message.replaceAll("&([0-9a-f])", "\u00A7$1");
	}
	
	/** Returns the number of online players */
	public int numberOfOnlinePlayers()
	{
		Player[] onlinePlayers = getServer().getOnlinePlayers();
		return onlinePlayers.length;
	}
	
}
