package fr.maner.msponsor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import fr.maner.msponsor.MSponsor;

public class WLConfig {

	private enum ConfigErrEnum {

		CREATION("Impossible de créer le fichier de configuration"),
		INVALID("Le fichier de configuration est invalide"),
		READ("Impossible de lire le fichier de configuration"),
		WRITE("Impossible d'écrire dans le fichier de configuration");

		private String str;
		private ConfigErrEnum(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	private MSponsor pl;
	private File file = null;

	public WLConfig(MSponsor pl) {
		this.pl = pl;
		this.file = new File(pl.getDataFolder() + "/wlExtra.json");

		saveDefaultConfig();
	}

	private void saveDefaultConfig() {
		if(file.exists())
			return;

		try {
			file.createNewFile();
		} catch (IOException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.CREATION.toString());
			e.printStackTrace();
			return;
		}

		savePlayers(null);
	}

	public Set<String> getPlayers() {
		HashSet<String> set = new HashSet<String>();
		JsonObject json = getJson();

		if(json != null)
			getJsonPlayer(json).forEach(p -> set.add(p.getAsString()));

		return set;
	}

	public void savePlayers() {
		savePlayers(pl.getExtraWL());
	}

	private void savePlayers(Set<String> players) {
		JsonObject json;
		JsonArray array = new JsonArray();

		if(players == null)
			json = new JsonObject();
		else {
			json = getJson();
			if(json == null)
				return;
			
			players.forEach(p -> array.add(new JsonPrimitive(p)));
		}

		json.add("Players", array);

		try {
			Files.write(Paths.get(file.toURI()), json.toString().getBytes());
		} catch (IOException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.WRITE.toString());
			e.printStackTrace();
		}
	}

	private JsonArray getJsonPlayer(JsonObject json) {
		try {
			return json.get("Players").getAsJsonArray();
		} catch(IllegalStateException | NullPointerException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.INVALID.toString());
			e.printStackTrace();
			return null;
		}
	}

	private JsonObject getJson() {
		try {
			return new JsonParser().parse(getContent()).getAsJsonObject();
		} catch(IllegalStateException | JsonParseException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.INVALID.toString());
			e.printStackTrace();
			return null;
		}
	}

	private String getContent() {
		try {
			return new String(Files.readAllBytes(Paths.get(file.toURI())));
		} catch (IOException e) {
			pl.getLogger().log(Level.SEVERE, ConfigErrEnum.READ.toString());
			e.printStackTrace();
			return null;
		}
	}
}
