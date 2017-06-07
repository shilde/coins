package ch.fhnw.cose.tvseries;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class ImdbFetcherTest {

	@Test
	public void getImdbIdByName() throws NamingException, SQLException, ParserConfigurationException, SAXException, IOException {
		String imdbId = ImdbFetcher.getImdbIdByName("The Simpsons");
	}
	
	@Test
	public void updateSeriesWithImdb() throws NamingException, SQLException, ParserConfigurationException, SAXException, IOException {
		
		java.util.List<Serie> series = DatabaseHandler.getSeriesWithoutImdbId();
		for(Serie serie : series) {
			try
			{
				String imdbId = ImdbFetcher.getImdbIdByName(serie.name);
				DatabaseHandler.updateSerieImdbId(serie.id, imdbId);
				
			}
			catch(Exception ex)
			{
				
			}
		}
	}
	
	@Test
	public void updateSeriesRatings()
	{
		
		try {
			List<Serie> series = DatabaseHandler.getSeries();
			
			for(Serie serie : series) {
				try {
					Double rating = ImdbFetcher.getSeriesRatingByImdbId(serie.imdbId);
					DatabaseHandler.updateSerieRating(rating, serie.imdbId);
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} 
		catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateActorsForSeries() throws NamingException, SQLException {
		List<Serie> series = DatabaseHandler.getSeries();
		for(Serie serie : series) {
			try
			{
				List<String> actorsNames = ImdbFetcher.getActorsForSeries(serie.imdbId);	
				
				if(actorsNames == null)
					break;
				
				for(String actorName : actorsNames) {
					Actor actor = DatabaseHandler.getActorByName(actorName);
					
					if(actor == null) {
						actor = new Actor(0, actorName);
						actor.id = DatabaseHandler.insertActor(actor.name);
					}
					
					try {
						System.out.println("Add Series Actor relation " + serie.id + " " + actor.id);
						DatabaseHandler.insertSeriesActor(serie.id, actor.id);
					}
					catch(Exception ex) {
						// Vermutlich von unique-constraint, deshalb ignorieren
						System.out.println(ex.getMessage());
					}
				}			
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}

	@Test
	public void getActorsForSeries() throws NamingException, SQLException, ParserConfigurationException, SAXException, IOException {
		List<String> actors = ImdbFetcher.getActorsForSeries("tt0096697");	
	}
	
	@Test
	public void insertEpisodes() throws Exception, SQLException {
		java.util.List<Serie> series = DatabaseHandler.getSeries();
		for(Serie serie : series) {
			try
			{
				System.out.println("Erstelle Episoden für Serie " + serie.imdbId + "...");
				int seasonNr = 0;
				
				while(seasonNr++ <= 100) {
					try {
						List<Episode> episodes = ImdbFetcher.getEpisodesForSeason(serie.imdbId, seasonNr);
						
						for(Episode episode : episodes) {
							episode.seriesId = serie.id;
							DatabaseHandler.insertEpisode(episode);
						}
					}
					catch(IllegalArgumentException e)
					{
						// Vermutlich keine Episode für diese Nr.
						System.out.println("Keine Staffel " + seasonNr + " für Serie " + serie.name);
						break;
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
}