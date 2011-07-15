package xrevolut1onzx.broadcastdonator;

import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class PermissionManager {
	
	/**
	 * Declarations to handle permissions
	 */
	public static Boolean usingPermissions;
	
	public BroadcastDonator plugin;
	
	public PermissionManager (BroadcastDonator instance) {
		plugin = instance;
	}
	
	public static PermissionHandler permissionHandler;
	
	public void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}

		Plugin permissionsPlugin = plugin.getServer().getPluginManager()
				.getPlugin("Permissions");

		if (permissionsPlugin == null) {
			plugin.log("Permission system not detected, defaulting to OP");
			usingPermissions = false;
			return;
		}

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		usingPermissions = true;
		plugin.log("Successfully hooked into Permissions");
	}
	
	public void disablePermissions() {
		if (permissionHandler == null) {
			return;
		}
		permissionHandler = null;
		usingPermissions = null;
		plugin.log("Unhooked from Permissions successfully");
	}

}
