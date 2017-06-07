package ch.fhnw.cose.tvseries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Serie {
	public Serie(ResultSet rs) throws SQLException {
		/*this.id = rs.getInt("Id");
		this.name = rs.getString("Name");
		this.canceled = rs.getBoolean("Canceled");
		this.imdbId = rs.getString("ImdbId");*/
		this(rs.getInt("Id"), rs.getString("Name"), rs.getInt("State"), rs.getString("ImdbId"));	
	}
	
	public Serie(int id, String name, int state, String imdbId) {
		this.id = id;
		this.name = name;
		this.state = state;
		this.imdbId = imdbId;
	}
	
	public int id;
	public String name;
	public int state;
	public String imdbId;
}