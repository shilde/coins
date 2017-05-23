package ch.fhnw.cose.tvseries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Serie {
	public Serie(ResultSet rs) throws SQLException {
		/*this.id = rs.getInt("Id");
		this.name = rs.getString("Name");
		this.canceled = rs.getBoolean("Canceled");
		this.imdbId = rs.getString("ImdbId");*/
		this(rs.getInt("Id"), rs.getString("Name"), rs.getBoolean("Canceled"), rs.getString("ImdbId"));	
	}
	
	public Serie(int id, String name, boolean canceled, String imdbId) {
		this.id = id;
		this.name = name;
		this.canceled = canceled;
		this.imdbId = imdbId;
	}
	
	public int id;
	public String name;
	public boolean canceled;
	public String imdbId;
}