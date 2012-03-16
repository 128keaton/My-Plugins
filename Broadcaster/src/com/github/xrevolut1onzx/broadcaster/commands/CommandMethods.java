package com.github.xrevolut1onzx.broadcaster.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.xrevolut1onzx.broadcaster.BroadcastMessage;
import com.github.xrevolut1onzx.broadcaster.Broadcaster;

public class CommandMethods
{
	
	private Broadcaster plugin;
	
	/**
	 * 
	 * @param instance The instance of Broadcaster currently being used to operate the plugin
	 */
	public CommandMethods(Broadcaster instance)
	{
		plugin = instance;
	}
	
	/**
	 * Reloads the plugin on command
	 * @param typer The player that invoked this; null if the console executed the command
	 */
	public void reload(Player typer)
	{
		if (typer == null) // console command
		{
			plugin.onReload();
			plugin.log("Plugin reloaded");
		}
		else // player command
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
	}
	
	/**
	 * Previews a message to the player or the console
	 * @param typer The player that invoked this; null if the console executed the command
	 * @param numberString The message number, in String format
	 */
	public void preview(Player typer, String numberString)
	{
		if (typer == null) // console command
		{
			int number = (Integer.parseInt(numberString)) - 1;
			String rawMessage = plugin.getMessage(number);
			if (plugin.getNumberOfMessages() < (number + 1))
			{
				plugin.log("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
				return;
			}
			if (rawMessage != null)
			{
				String finalMessage = plugin.replaceColors(rawMessage);
				plugin.log("Preview: " + finalMessage);
			}
			else
			{
				plugin.log("Reload the configuration file to preview your message!");
			}
		}
		else // player command
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
	}
	
	public void broadcast(Player typer, String numberString)
	{
		if (typer == null) // console command
		{
			int number = (Integer.parseInt(numberString)) - 1;
			String rawMessage = plugin.getMessage(number);
			BroadcastMessage broadcastMessage = plugin.getBroadcastMessage(number);
			if (plugin.getNumberOfMessages() < (number + 1))
			{
				plugin.log("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
				return;
			}
			if (rawMessage != null)
			{
				if (plugin.numberOfOnlinePlayers() == 0)
				{
					plugin.log("No one online!");
					return;
				}
				String finalMessage = plugin.replaceColors(rawMessage);
				if (plugin.isUsingOp())
				{
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
				}
				else if (plugin.isUsingSuperPerms())
				{
					for (Player player : plugin.getServer().getOnlinePlayers())
					{
						if (!player.hasPermission("broadcaster.exemptfrommessage" + (number + 1)))
						{
							player.sendMessage(finalMessage);
						}
					}
				}
				plugin.log(finalMessage);
			}
			else
			{
				plugin.log("Reload the configuration file to broadcast your message!");
			}
		}
		else // player command
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
	
}
