package ch.fhnw.cose.tvseries;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class App {
	public static void main(String[] args) {
		boolean execute = true; // Wegen Request Limit der API
		
		if(execute) {
			try {
				List<Serie> series = DatabaseHandler.getSeries();
				updateEpisodesForSeries(series);
			} 
			catch (NamingException | SQLException e) {
				e.printStackTrace();
			}
			/*try {
				List<Serie> series = DatabaseHandler.getSeries();
				updateActorsForSeries(series);
			} 
			catch (NamingException | SQLException e) {
				e.printStackTrace();
			}*/
		}
	}
	
	private static void updateEpisodesForSeries(List<Serie> series) {	
		for(Serie serie : series) {
			try
			{
				System.out.println("Erstelle Episoden für Serie " + serie.imdbId + "...");
				int seasonNr = 1;
				
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
	
	private static void updateActorsForSeries(List<Serie> series) {	
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
}
