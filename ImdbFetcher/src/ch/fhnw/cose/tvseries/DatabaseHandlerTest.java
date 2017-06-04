package ch.fhnw.cose.tvseries;
import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.Test;

public class DatabaseHandlerTest {

	@Test
	public void getSeries() throws NamingException, SQLException {
		java.util.List<Serie> series = DatabaseHandler.getSeries(true);
		int numberOfSeries = series.size();
	}
	
	@Test
	public void getEpisodes() throws NamingException, SQLException {
		java.util.List<Episode> episodes = DatabaseHandler.getEpisodes();
		int numberOfEpisodes = episodes.size();
	}
	
	@Test
	public void getActors() throws NamingException, SQLException {
		java.util.List<Actor> actors = DatabaseHandler.getActors();
		int numberOfActors = actors.size();
	}
	
	@Test
	public void insertEpisode() throws NamingException, SQLException {
		DatabaseHandler.insertEpisode(1, 1, 1, 8.5);
	}
}