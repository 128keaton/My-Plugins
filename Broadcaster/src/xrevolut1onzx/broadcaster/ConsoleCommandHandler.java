package xrevolut1onzx.broadcaster;

import org.bukkit.entity.Player;

public class ConsoleCommandHandler
{
	
	/** Used to reference the Broadcaster class */
	private Broadcaster plugin;
	
	/**
	 * Builds a new ConsoleCommandHandler
	 * @param plugin A Broadcaster instance to be referenced
	 */
	public ConsoleCommandHandler(Broadcaster plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Reloads the Broadcaster plugin
	 * when requested by the console
	 */
	public void reload()
	{
		plugin.onReload();
		plugin.log("Plugin reloaded");
	}
	
	/**
	 * Sends a preview to the console
	 * when requested by the console
	 * @param number The number of the message, message1 = 1, message2 = 2, etc
	 */
	public void preview(String numberString)
	{
		int number = Integer.parseInt(numberString);
		number--; // the message number in the array is one less than shown to the end-user
		String rawMessage = plugin.getMessage(number);
		if (plugin.getNumberOfMessages() < (number + 1))
		{
			plugin.log("You don't have that many messages! You currently have " + plugin.getNumberOfMessages() + " messages");
			return;
		}
		if (rawMessage != null)
		{
			String finalMessage = new String(rawMessage.replaceAll("&([0-9a-f])", "\u00A7$1"));
			plugin.log("Preview: " + finalMessage);
		}
		else
		{
			plugin.log("Reload the configuration file to preview your message!");
		}
	}
	
	/**
	 * Broadcasts a message to the relevant players
	 * @param numberString The number of the message
	 */
	public void broadcast(String numberString)
	{
		int number = Integer.parseInt(numberString);
		number--; // the message number in the array is one less than shown to the end-user
		String rawMessage = plugin.getMessage(number);
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
			String finalMessage = new String(rawMessage.replaceAll("&([0-9a-f])", "\u00A7$1"));
			if (plugin.isUsingOP())
			{
				for (Player player : plugin.getServer().getOnlinePlayers())
				{
					if (!player.isOp())
					{
						player.sendMessage(finalMessage);
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
}
