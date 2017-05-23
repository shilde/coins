package ch.fhnw.cose.tvseries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseHandler {
	static Connection conn;
	static {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("coinsTeam11");
		dataSource.setPassword("coinsTVseriesCoolhunting");
		dataSource.setDatabaseName("coins");
		dataSource.setServerName("rds-coins-team11.cfvvcqxgfghr.us-west-2.rds.amazonaws.com");
		
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public static java.util.List<Serie> getSeries() throws NamingException, SQLException {

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Id, Name, Canceled, ImdbId FROM Series");

		java.util.ArrayList<Serie> series = new java.util.ArrayList<>();
		
		while(rs.next()) {
			series.add(new Serie(rs));
		}
		
		rs.close();
		stmt.close();
		
		return series;
	}
	
	public static Actor getActorByName(String name) throws NamingException, SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT idActor, Name FROM Actor WHERE Name=?");
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		
		//Statement stmt = conn.createStatement();
		//ResultSet rs = stmt.executeQuery("SELECT idActor, Name FROM Actor WHERE Name='"+name+"'");

		Actor actor = null;
		
		while(rs.next()) {
			actor = new Actor(rs);
			break;
		}
		
		rs.close();
		stmt.close();
		
		return actor;
	}
	
	public static int insertActor(String name) throws NamingException, SQLException {
		//Statement stmt = conn.createStatement();
		PreparedStatement stmt = null;
		try {
		stmt = conn.prepareStatement("INSERT INTO Actor (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		stmt.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Fehler bei " + name);

		}
		//String query = "INSERT INTO Actor (name) VALUES ('"+name.replace("'","''")+"')";
		//int test = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		int id = 0;
		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id = (int)generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
		
		stmt.close();
		
		return id;
	}
	
	public static void insertSeriesActor(int seriesId, int actorId) throws NamingException, SQLException {
		Statement stmt = conn.createStatement();
		String query = "INSERT INTO SeriesActor (ActorId, SeriesId) VALUES ("+actorId+","+seriesId+")";
		stmt.executeUpdate(query);	
	}
	
	public static java.util.List<Serie> getSeriesWithoutImdbId() throws NamingException, SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Id, Name, Canceled, ImdbId FROM Series WHERE ImdbId IS NULL");

		java.util.ArrayList<Serie> series = new java.util.ArrayList<>();
		
		while(rs.next()) {
			series.add(new Serie(rs));
		}
		
		rs.close();
		stmt.close();
		
		return series;
	}
	
	public static void updateSerieImdbId(int id, String imdbId) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(String.format("UPDATE Series SET ImdbId = '%s' WHERE Id = %s", imdbId, id));
		stmt.close();
	}
	
	public static void insertEpisode(int seriesId, int season, int episode, double rating) throws SQLException {
		Statement stmt = conn.createStatement();
		String query = String.format("INSERT INTO Episode (SeriesId, Season, Episode, Rating) VALUES (%s, %s, %s, %s)", seriesId, season, episode, rating);
		stmt.executeUpdate(query);
		stmt.close();
	}
}