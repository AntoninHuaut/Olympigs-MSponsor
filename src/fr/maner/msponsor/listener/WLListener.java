package fr.maner.msponsor.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import fr.maner.msponsor.MSponsor;

public class WLListener implements Listener {

	private MSponsor pl;

	public WLListener(MSponsor pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {
		if(pl.isWhitelistEnable() && !pl.isInWhitelist(e.getName()))
			e.disallow(Result.KICK_OTHER, pl.getKickMsg());
	}

}
