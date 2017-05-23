package ch.fhnw.cose.tvseries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Actor {
	public Actor(int id, String name, String imdbId) {
		this.id = id;
		this.name = name;
		this.imdbId = imdbId;
	}
	
	public int id;
	public String name;
	public String imdbId;
}
