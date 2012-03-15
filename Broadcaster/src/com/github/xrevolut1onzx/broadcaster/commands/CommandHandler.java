package com.github.xrevolut1onzx.broadcaster.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.xrevolut1onzx.broadcaster.BroadcastMessage;
import com.github.xrevolut1onzx.broadcaster.Broadcaster;


public class CommandHandler
{
	
	/** The Broadcaster instance used throughout CommandHandler */
	private Broadcaster plugin;
	
	/**
	 * Creates a new CommandHandler
	 * @param instance The Broadcaster instance to reference
	 */
	public CommandHandler(Broadcaster instance)
	{
		plugin = instance;
	}
	
	/**
	 * Reloads the plugin
	 * @param typer The typer which requested this command
	 */
	public void reload(Player typer)
	{
		if (plugin.isUsingOp())
		{
			if (typer.isOp())
			{
				plugin.onReload();
				typer.sendMessage("Plugin reloaded");
			}
			else
				typer.sendMessage("You don't have permission to use this command.");
		}
		else if (plugin.isUsingSuperPerms())
		{
			if (typer.hasPermission("broadcaster.reload"))
			{
				plugin.onReload();
				typer.sendMessage("Plugin reloaded");
			}
			else
				typer.sendMessage("You don't have permission to use this command.");
		}
	}
	
	/**
	 * Previews a specific messages
	 * @param typer The typer which requested this command
	 * @param numberString The number of the message to view
	 */
	public void preview(Player typer, String numberString)
	{
		if (plugin.isUsingOp())
		{
			if (typer.isOp())
			{
				int number = (Integer.parseInt(numberString)) - 1;
				String rawMessage = plugin.getMessage(number);
				if (plugin.getNumberOfMessages() < (number + 1))
				{
					typer.sendMessage("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
					return;
				}
				if (rawMessage != null)
				{
					String finalMessage = plugin.replaceColors(rawMessage);
					typer.sendMessage("Preview: " + finalMessage);
				}
				else
				{
					typer.sendMessage(ChatColor.DARK_RED + "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			}
			else
				typer.sendMessage("You don't have permission to use this command.");
		}
		else if (plugin.isUsingSuperPerms())
		{
			if (typer.hasPermission("broadcaster.preview"))
			{
				int number = (Integer.parseInt(numberString)) - 1;
				String rawMessage = plugin.getMessage(number);
				if (plugin.getNumberOfMessages() < (number + 1))
				{
					typer.sendMessage("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
					return;
				}
				if (rawMessage != null)
				{
					String finalMessage = plugin.replaceColors(rawMessage);
					typer.sendMessage("Preview: " + finalMessage);
				}
				else
				{
					typer.sendMessage(ChatColor.DARK_RED + "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			}
			else
				typer.sendMessage("You don't have permission to use this command.");
		}
	}
	
	/**
	 * Manually broadcasts a message
	 * @param typer The typer which requested this command
	 * @param numberString The number of the message to be broadcasted
	 */
	public void broadcast(Player typer, String numberString)
	{
		if (plugin.isUsingOp())
		{
			if (typer.isOp())
			{
				int number = (Integer.parseInt(numberString)) - 1;
				number--; // the message number in the array is one less than shown to the end-user
				String rawMessage = plugin.getMessage(number);
				BroadcastMessage broadcastMessage = plugin.getBroadcastMessage(number);
				if (plugin.getNumberOfMessages() < (number + 1))
				{
					typer.sendMessage("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
					return;
				}
				if (rawMessage != null)
				{
					String finalMessage = plugin.replaceColors(rawMessage);
					for (Player player : plugin.getServer().getOnlinePlayers())
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
					plugin.log(finalMessage);
					plugin.log("Manual command used by " + typer.getName());
				}
				else
				{
					typer.sendMessage(ChatColor.DARK_RED + "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			}
			else
				typer.sendMessage("You don't have permission to use this command.");
		}
		else if (plugin.isUsingSuperPerms())
		{
			if (typer.hasPermission("broadcaster.broadcast"))
			{
				int number = (Integer.parseInt(numberString)) - 1;
				String rawMessage = plugin.getMessage(number);
				if (plugin.getNumberOfMessages() < (number + 1))
				{
					typer.sendMessage("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
					return;
				}
				if (rawMessage != null)
				{
					String finalMessage = plugin.replaceColors(rawMessage);
					for (Player player : plugin.getServer().getOnlinePlayers())
					{
						if (!player.hasPermission("broadcaster.exemptfrommessage" + (number + 1)))
						{
							player.sendMessage(finalMessage);
						}
					}
					plugin.log(finalMessage);
					plugin.log("Manual command used by " + typer.getName());
				}
				else
				{
					typer.sendMessage(ChatColor.DARK_RED + "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			}
			else
				typer.sendMessage("You don't have permission to use this command.");
		}
	}
}
