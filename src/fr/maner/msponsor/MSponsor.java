package fr.maner.msponsor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maner.msponsor.cmd.WLCmd;
import fr.maner.msponsor.listener.WLListener;
import fr.maner.msponsor.run.RefresWLRun;
import fr.maner.msponsor.utils.WLConfig;

public class MSponsor extends JavaPlugin {

	private boolean activeWL = true;
	public String urlRQ;
	private String kickMsg;
	private List<Integer> idGames;
	private Set<String> wlExtra = new HashSet<String>();
	private Set<String> wlWeb = new HashSet<String>();
	private RefresWLRun refreshWLRun;
	private WLConfig wlConfig;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		this.wlConfig = new WLConfig(this);
		this.refreshWLRun = new RefresWLRun(this);
		loadConfig();

		getServer().getPluginCommand("msponsor").setExecutor(new WLCmd(this));
		getServer().getPluginManager().registerEvents(new WLListener(this), this);
		getServer().getScheduler().runTaskTimer(this, refreshWLRun, 0L, 20L * 60);
	}

	public void loadConfig() {
		reloadConfig();

		kickMsg = ChatColor.translateAlternateColorCodes('&',getConfig().getString("KickMsg"));
		idGames = getConfig().getIntegerList("GamesId");
		urlRQ = getConfig().getString("RQUrl");
		wlExtra = wlConfig.getPlayers();
		refreshWLRun.refreshWL(null);
	}
	
	public boolean isInWhitelist(String s) {
		return wlWeb.contains(s) || wlExtra.contains(s);
	}
	
	public boolean isWhitelistEnable() {
		return activeWL;
	}
	
	public void toggleWhitelistStatus() {
		activeWL = !activeWL;
	}
	
	public String getRQUrl() {
		return urlRQ;
	}
	
	public String getKickMsg() {
		return kickMsg;
	}
	
	public List<Integer> getGamesId() {
		return idGames;
	}
	
	public Set<String> getExtraWL() {
		return wlExtra;
	}
	
	public Set<String> getWebWL() {
		return wlWeb;
	}
	
	public void setWLWeb(Set<String> wlWeb) {
		this.wlWeb = wlWeb;
	}
	
	public void refreshWL(CommandSender sender) {
		refreshWLRun.refreshWL(sender);
	}
	
	public WLConfig getWLConfig() {
		return wlConfig;
	}
}
