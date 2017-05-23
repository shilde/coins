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
	public void getActorsForSeries() throws NamingException, SQLException, ParserConfigurationException, SAXException, IOException {
		List<String> actors = ImdbFetcher.getActorsForSeries("tt0096697");	
	}
		
	@Test
	public void updateActorsForSeries() throws NamingException, SQLException, ParserConfigurationException, SAXException, IOException {
		
		java.util.List<Serie> series = DatabaseHandler.getSeries();
		
		for(Serie serie : series) {
			try
			{
				
				
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
	
	@Test
	public void insertEpisodes() throws Exception, SQLException {
		java.util.List<Serie> series = DatabaseHandler.getSeriesWithoutImdbId();
		for(Serie serie : series) {
			try
			{
				
			}
			catch(Exception ex)
			{
				
			}
		}
	}
}