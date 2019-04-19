package fr.maner.msponsor.cmd;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maner.msponsor.MSponsor;

public class WLCmd implements CommandExecutor {

	private MSponsor pl;

	public WLCmd(MSponsor pl) {
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("msponsor"))
			return false;

		if (args.length < 1) {
			if(checkPerms(sender, "help"))
				sender.sendMessage("§6» §c/wl §7<§eextra §7| §ekick §7| §elist §7| §erefresh §7| §ereload §7| §etoggle§7>");
		}

		else if(args[0].equals("extra")) {
			if(!checkPerms(sender, args[0]))
				return false;

			if(args.length < 2)
				sender.sendMessage("§6» §c/wl " + args[0] + " §7<§eadd §7| §elist §7| §eremove§7>");

			else if(args[1].equals("list")) 
				showWL(sender, pl.getExtraWL());
			
			else if(args[1].equals("add") || args[1].equals("remove")) {
				if(args.length < 3) {
					sender.sendMessage("§6» §c/wl " + args[0] + " " + args[1] + " §7<§epseudo§7>");
					return true;
				}

				if(args[1].equals("add")) {
					if(pl.getExtraWL().contains(args[2]))
						sender.sendMessage("§6» §c" + args[2] + " est déjà présent dans cette liste");
					else {
						pl.getExtraWL().add(args[2]);
						sender.sendMessage("§6» §a" + args[2] + " a été ajouté dans la liste");
						pl.getWLConfig().savePlayers();
					}
				} else {
					if(!pl.getExtraWL().contains(args[2]))
						sender.sendMessage("§6» §c" + args[2] + " n'est pas présent dans cette liste");
					else {
						pl.getExtraWL().remove(args[2]);
						sender.sendMessage("§6» §a" + args[2] + " a été retiré de la liste");
						pl.getWLConfig().savePlayers();
					}
				}
			}
		}
		
		else if(args[0].equals("kick")) {
			if(!checkPerms(sender, args[0]))
				return false;

			int nbKick = 0;
			
			for(Player p : Bukkit.getOnlinePlayers())
				if(!pl.isInWhitelist(p.getName())) {
					p.kickPlayer(pl.getKickMsg());
					nbKick++;
				}
			
			sender.sendMessage("§6» §e" + nbKick + " joueurs ont été kick");
		}

		else if(args[0].equals("list")) {
			if(!checkPerms(sender, args[0]))
				return false;

			if(pl.getWebWL().isEmpty())
				sender.sendMessage("§6» §eLa liste des sponsors est vide");
			else 
				showWL(sender, pl.getWebWL());
		}

		else if(args[0].equals("refresh")) {
			if(checkPerms(sender, args[0]))
				pl.refreshWL(sender);
		}

		else if(args[0].equals("reload")) {
			if(checkPerms(sender, args[0])) {
				pl.loadConfig();
				sender.sendMessage("§6» §eLa configuration a été rechargée");
			}
		}
		
		else if(args[0].equals("toggle")) {
			if(!checkPerms(sender, args[0]))
				return false;

			pl.toggleWhitelistStatus();

			if(pl.isWhitelistEnable())
				sender.sendMessage("§6» §aLa whitelist a été réactivée");
			else
				sender.sendMessage("§6» §cLa whitelist a été désactivée temporairement");
		}

		return true;
	}
	
	private void showWL(CommandSender sender, Set<String> list) {
		boolean color = false;

		for(String el : list) {
			sender.sendMessage((color ? "§e" : "§6") + el);
			color = !color;
		}

		sender.sendMessage("§aCette liste comporte §e" + list.size() + " §ajoueur(s)");
	}

	private boolean checkPerms(CommandSender sender, String str) {
		if(!sender.hasPermission("msponsor.command." + str)) {
			sender.sendMessage("§6» §cVous n'avez pas la permission d'effectuer cette commande");
			return false;
		}

		return true;
	}
}
