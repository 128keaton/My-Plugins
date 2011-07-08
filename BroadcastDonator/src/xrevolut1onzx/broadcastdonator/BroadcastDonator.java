package xrevolut1onzx.broadcastdonator;

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

	// Variable declaration
	Logger log = Logger.getLogger("Minecraft");
	
	public static PermissionHandler permissionHandler;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("bd")){ // If the player typed /bd then do the following...
			Player commandTyper = (Player)sender;
			if (!BroadcastDonator.permissionHandler.has(commandTyper, "broadcastdonator.use")) {
			      return true;
			}
			getServer().broadcastMessage("[" + ChatColor.DARK_RED + "Server" + ChatColor.WHITE + "] " + ChatColor.RED + "Enjoy EvoServer? Consider donating to fund the server! Options available on the website.");
			return true;
		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}

	public void onDisable() {

	}

	public void onEnable() {
		log.info("[BD] Initiating plugin...");
		setupPermissions();
		log.info("[BD] Initialized");
	}
	
	// Permissions setup
	private void setupPermissions() {
	    if (permissionHandler != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        log.info("[BD] Permission system not detected, defaulting to OP");
	        return;
	    }
	    
	    permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	    log.info("[BD] Permissions found, using permissions instead of OP");
	}

}
