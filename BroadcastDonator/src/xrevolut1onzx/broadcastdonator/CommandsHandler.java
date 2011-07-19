package xrevolut1onzx.broadcastdonator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandsHandler {

	/**
	 * Declarations to handle command execution
	 */
	public BroadcastDonator plugin;

	public CommandsHandler(BroadcastDonator instance) {
		plugin = instance;
	}

	public void preview(Player commandTyper) {
		if (PermissionManager.usingPermissions) {
			if (PermissionManager.permissionHandler.has(commandTyper,
					"broadcastdonator.use")) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					commandTyper.sendMessage(ChatColor.DARK_RED + "Preview: "
							+ ChatColor.WHITE + finalMessage);
				} else {
					commandTyper
							.sendMessage(ChatColor.DARK_RED
									+ "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		} else {
			if (commandTyper.isOp()) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					commandTyper.sendMessage(ChatColor.DARK_RED + "Preview: "
							+ ChatColor.WHITE + finalMessage);
				} else {
					commandTyper
							.sendMessage(ChatColor.DARK_RED
									+ "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		}
	}

	public void reload(Player commandTyper) {
		if (PermissionManager.usingPermissions) {
			if (PermissionManager.permissionHandler.has(commandTyper,
					"broadcastdonator.reload")) {
				plugin.onReload();
				plugin.log("Plugin reloaded by " + commandTyper.getName());
				commandTyper.sendMessage(ChatColor.GRAY + "Plugin reloaded");
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		} else {
			if (commandTyper.isOp()) {
				plugin.onReload();
				plugin.log("Plugin reloaded by " + commandTyper.getName());
				commandTyper.sendMessage(ChatColor.GRAY + "Plugin reloaded");
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		}
	}

	public void broadcast(Player commandTyper) {
		if (PermissionManager.usingPermissions) {
			if (PermissionManager.permissionHandler.has(commandTyper,
					"broadcastdonator.use")) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					for (Player player : plugin.getServer().getOnlinePlayers()) {
						if (!PermissionManager.permissionHandler.has(player,
								"broadcastdonator.exemptfrommessage")) {
							player.sendMessage(finalMessage);
						}
					}
					plugin.log(finalMessage);
					plugin.log("Manual command used by "
							+ commandTyper.getName());
				} else {
					commandTyper
							.sendMessage(ChatColor.DARK_RED
									+ "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		} else {
			if (commandTyper.isOp()) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					for (Player player : plugin.getServer().getOnlinePlayers()) {
						player.sendMessage(finalMessage);
					}
					plugin.log(finalMessage);
					plugin.log("Manual command used by "
							+ commandTyper.getName());
				} else {
					commandTyper
							.sendMessage(ChatColor.DARK_RED
									+ "Reload the configuration file to load your message!");
					plugin.log("Reload the configuration file to load your message!");
				}
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		}
	}

}
