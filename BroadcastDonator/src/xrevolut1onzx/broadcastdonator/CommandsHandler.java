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
		if (BroadcastDonator.usingSuperPerms) {
			if (commandTyper.hasPermission("broadcastdonator.use")) {
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
		} else if (BroadcastDonator.usingPermissions) {
			if (BroadcastDonator.permissionHandler.has(commandTyper,
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
		} else if (BroadcastDonator.usingOp) {
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
		if (BroadcastDonator.usingSuperPerms) {
			if (commandTyper.hasPermission("broadcastdonator.reload")) {
				plugin.onReload();
				plugin.log("Plugin reloaded by " + commandTyper.getName());
				commandTyper.sendMessage(ChatColor.GRAY + "Plugin reloaded");
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		} else if (BroadcastDonator.usingPermissions) {
			if (BroadcastDonator.permissionHandler.has(commandTyper,
					"broacastdonator.reload")) {
				plugin.onReload();
				plugin.log("Plugin reloaded by " + commandTyper.getName());
				commandTyper.sendMessage(ChatColor.GRAY + "Plugin reloaded");
			} else {
				commandTyper
						.sendMessage("You don't have permission to use this command.");
			}
		} else if (BroadcastDonator.usingOp) {
			if (BroadcastDonator.usingOp) {
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
		if (BroadcastDonator.usingSuperPerms) {
			if (commandTyper.hasPermission("broadcastdonator.use")) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					for (Player player : plugin.getServer().getOnlinePlayers()) {
						if (!player
								.hasPermission("broadcastdonator.exemptfrommessage")) {
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
		} else if (BroadcastDonator.usingPermissions) {
			if (BroadcastDonator.permissionHandler.has(commandTyper, "broadcastdonator.use")) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					for (Player player : plugin.getServer().getOnlinePlayers()) {
						if (!BroadcastDonator.permissionHandler.has(commandTyper, "broadcastdonator.exemptfrommessage")) {
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
		} else if (BroadcastDonator.usingOp) {
			if (commandTyper.isOp()) {
				if (plugin.rawMessage != null) {
					String finalMessage = new String(
							plugin.rawMessage.replaceAll("&([0-9a-f])",
									"\u00A7$1"));
					for (Player player : plugin.getServer().getOnlinePlayers()) {
						if (!player.isOp()) {
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
		}
	}

}
