package xrevolut1onzx.broadcastdonator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class BroadcastDonator extends JavaPlugin {

	// Configuration variables
	public static String mainDirectory = "plugins/BroadcastDonator";
	public static File config = new File(mainDirectory + File.separator
			+ "config.yml");
	public static Properties prop = new Properties();

	/**
	 * String that is used to contain the message that is broadcast throughout
	 * the server
	 */
	public String messageToSend;

	/**
	 * Declares the logger. The logger allows you to write information to the
	 * console and to the server.log file
	 */
	public static final String logPrefix = "[BD] ";
	Logger log = Logger.getLogger("Minecraft");

	// Declaration for permissions support
	public static PermissionHandler permissionHandler;

	/**
	 * /* Plugin's command handler. One command supported (/bd) with the
	 * permission node "broadcastdonator.use" to use the command
	 */
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bd")) { // If the player typed /bd
													// then do the following...
			Player commandTyper = (Player) sender;
			if (!BroadcastDonator.permissionHandler.has(commandTyper,
					"broadcastdonator.use")) {
				return true;
			}
			if (messageToSend != null) {
				getServer().broadcastMessage(messageToSend);
			} else {
				commandTyper.sendMessage(ChatColor.DARK_RED + "Reload the configuration file to load your message!");
				log("Reload the configuration file to load your message!");
			}
			return true;
		} // If this has happened the function will break and return true. if
			// this hasn't happened the a value of false will be returned.
		return false;
	}

	// Called on a clean stop of the server
	public void onDisable() {
		log("Disabled");
	}

	// Called on server start
	public void onEnable() {
		log("Initiating plugin...");
		// Handles the configuration file
		manageConfigFile();
		// Sets up permissions
		setupPermissions();
		log("Initialized");
	}

	// Manages the configuration file
	public void manageConfigFile() {
		new File(mainDirectory).mkdir(); // Creates the plugin's folder
		if (!config.exists()) {
			try {
				config.createNewFile();
				FileOutputStream output = new FileOutputStream(config);
				prop.put(
						"MessageToBroadcast",
						"[Server] Enjoy this server? Consider donating to help fund it! Options available on our website.");
				prop.store(output, "Edit the configurations to your liking");
				output.flush();
				output.close();
				log("Configuration file created. Please go update and reload it.");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			loadConfigFile();
			log("Configuration file loaded");
		}
	}

	public void loadConfigFile() {
		try {
			FileInputStream input = new FileInputStream(config);
			prop.load(input);
			messageToSend = prop.getProperty("MessageToBroadcast");
			input.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void log (String message) {
		log.info(logPrefix + message);
	}

	// Permissions setup method. Called only at server start
	private void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}

		Plugin permissionsPlugin = this.getServer().getPluginManager()
				.getPlugin("Permissions");

		if (permissionsPlugin == null) {
			log("Permission system not detected, defaulting to OP");
			return;
		}

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		log("Permissions found, using permissions instead of OP");
	}

}
