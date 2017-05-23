package ch.fhnw.cose.tvseries;

import java.sql.Connection;
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
		ResultSet rs = stmt.executeQuery("SELECT Id, Name, Canceled FROM Series");

		java.util.ArrayList<Serie> series = new java.util.ArrayList<>();
		while(rs.next())
		{
			int id = rs.getInt("Id");
			String name = rs.getString("Name");
			boolean canceled = rs.getBoolean("Canceled");
			String imdbId = rs.getString("ImdbId");
			
			Serie serie = new Serie(id, name, canceled, imdbId);
			series.add(serie);
		}
		
		rs.close();
		stmt.close();
		
		return series;
	}
	
	public static java.util.List<Serie> getSeriesWithoutImdbId() throws NamingException, SQLException {

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Id, Name, Canceled, ImdbId FROM Series WHERE ImdbId IS NULL");

		java.util.ArrayList<Serie> series = new java.util.ArrayList<>();
		while(rs.next())
		{
			int id = rs.getInt("Id");
			String name = rs.getString("Name");
			boolean canceled = rs.getBoolean("Canceled");
			String imdbId = rs.getString("ImdbId");
			
			Serie serie = new Serie(id, name, canceled, imdbId);
			series.add(serie);
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