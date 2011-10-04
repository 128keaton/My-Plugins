package xrevolut1onzx.broadcastdonator;

import org.bukkit.entity.Player;

public class ConsoleCommandHandler {

	public BroadcastDonator plugin;

	public ConsoleCommandHandler(BroadcastDonator instance) {
		plugin = instance;
	}
	
	public void preview() {
		if (plugin.rawMessage != null) {
			String finalMessage = new String(
					plugin.rawMessage.replaceAll("&([0-9a-f])",
							"\u00A7$1"));
			plugin.log("Preview: " + finalMessage);
		} else {
			plugin.log("Reload the configuration file to load your message!");
		}
	}

	public void broadcast() {
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
		} else {
			plugin.log("Reload the configuration file to load your message!");
		}
	}
	
	public void reload() {
		plugin.onReload();
		plugin.log("Plugin reloaded");
	}
}
