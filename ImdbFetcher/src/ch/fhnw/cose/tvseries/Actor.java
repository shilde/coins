package ch.fhnw.cose.tvseries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Actor {
	public Actor(ResultSet rs) throws SQLException {
		this(rs.getInt("idActor"), rs.getString("Name"));	
	}
	
	public Actor(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int id;
	public String name;
}
