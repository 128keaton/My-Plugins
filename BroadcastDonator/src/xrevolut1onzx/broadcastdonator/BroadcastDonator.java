package xrevolut1onzx.broadcastdonator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
	public String rawMessage;

	// If true, message will repeat at set interval
	public static boolean recurringMessage;
	public static int timeDelay;

	/**
	 * Declares the logger. The logger allows you to write information to the
	 * console and to the server.log file
	 */
	public static final String logPrefix = "[BD] ";
	Logger log = Logger.getLogger("Minecraft");

	// Initiates helping classes
	public PermissionManager permissionManager = new PermissionManager(this);

	public CommandsHandler commandsHandler = new CommandsHandler(this);

	/**
	 * Plugin's command handler. One command supported (/bd) with the permission
	 * node "broadcastdonator.use" to use the command
	 */
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bd")) {
			Player commandTyper = (Player) sender;
			if (args.length == 0) {
				// No arguments
				return false;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("broadcast")) {
					commandsHandler.broadcast(commandTyper);
					return true;

				} else if (args[0].equalsIgnoreCase("reload")) {
					commandsHandler.reload(commandTyper);
					return true;
				} else if (args[0].equalsIgnoreCase("preview")) {
					commandsHandler.preview(commandTyper);
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
		return false;
	}

	// Called on a clean stop of the server
	public void onDisable() {
		getServer().getScheduler().cancelAllTasks();
		log("Disabled");
	}

	// Called when the plugin needs to be reloaded
	public void onReload() {
		log("Reloading...");
		getServer().getScheduler().cancelAllTasks();
		manageConfigFile();
		permissionManager.disablePermissions();
		permissionManager.setupPermissions();
		handleRecurringMessage();
		log("Reloaded");
	}

	// Called on server start
	public void onEnable() {
		log("Initiating plugin...");
		// Handles the configuration file
		manageConfigFile();
		// Sets up permissions
		permissionManager.setupPermissions();
		handleRecurringMessage();
		log("Initialized");
	}

	public void handleRecurringMessage() {
		if (recurringMessage) {
			int timeDelayInTicks = timeDelay * 1200;
			getServer().getScheduler().scheduleAsyncRepeatingTask(this,
					new Runnable() {
						public void run() {
							if (rawMessage != null) {
								String finalMessage = new String(rawMessage
										.replaceAll("&([0-9a-f])", "\u00A7$1"));
								for (Player player : getServer()
										.getOnlinePlayers()) {
									if (PermissionManager.usingPermissions) {
										if (!PermissionManager.permissionHandler
												.has(player,
														"broadcastdonator.exemptfrommessage")) {
											player.sendMessage(finalMessage);
										}
									} else {
										player.sendMessage(finalMessage);
									}
								}
								log(finalMessage);
								log("Message broadcasted by repeater");
							} else {
								log("Reload the configuration file to load your message!");
							}
						}
					}, 60L, timeDelayInTicks);
		}
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
				prop.put("Recurring-Broadcast", "false");
				prop.put("Time-between-messages-in-minutes", "30");
				prop.store(output, "Edit the configurations to your liking");
				output.flush();
				output.close();
				log("Configuration file created. Please configure and reload it.");
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
			rawMessage = prop.getProperty("MessageToBroadcast");
			recurringMessage = Boolean.parseBoolean(prop
					.getProperty("Recurring-Broadcast"));
			timeDelay = Integer.parseInt(prop
					.getProperty("Time-between-messages-in-minutes"));
			input.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void log(String message) {
		log.info(logPrefix + message);
	}

}
