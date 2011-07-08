package xrevolut1onzx.broadcastdonator;

import java.io.File;
import java.util.List;

import org.bukkit.util.config.Configuration;

public class Config {
	private static BroadcastDonator plugin;

	public Config(BroadcastDonator instance) {
		plugin = instance;
	}

	public String directory = "plugins" + File.separator
			+ "BroadcastDonator";
	File file = new File(directory + File.separator + "config.yml");

	public void configCheck() {
		new File(directory).mkdir();

		if (!file.exists()) {
			try {
				file.createNewFile();
				addDefaults();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			loadkeys();
		}
	}

	private void write(String root, Object x) {
		Configuration config = load();
		config.setProperty(root, x);
		config.save();
	}

	private Boolean readBoolean(String root) {
		Configuration config = load();
		return config.getBoolean(root, true);
	}

	private Double readDouble(String root) {
		Configuration config = load();
		return config.getDouble(root, 0);
	}

	private List<String> readStringList(String root) {
		Configuration config = load();
		return config.getKeys(root);
	}

	private String readString(String root) {
		Configuration config = load();
		return config.getString(root);
	}

	private Configuration load() {

		try {
			Configuration config = new Configuration(file);
			config.load();
			return config;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addDefaults() {
		plugin.log.info("Generating configuration file because one wasn't found...");
		write(plugin.messageToSendRaw, "[&4Server&f] Do you enjoy this server? Consider donating! Options available on the website.");
		loadkeys();
	}

	private void loadkeys() {
		plugin.log.info("Loading configuration file...");
		plugin.messageToSendRaw = readString(plugin.messageToSendRaw);
	}
}