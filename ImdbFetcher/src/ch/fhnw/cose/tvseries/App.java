package ch.fhnw.cose.tvseries;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class App {
	public static void main(String[] args) {
		boolean execute = true; // Wegen Request Limit der API
		
		if(execute) {
			/*List<String> names = new ArrayList<String>();
			names.add("2 Broke Girls");
			names.add("Criminal Minds: Beyond Borders");
			names.add("Acting Out");
			names.add("Aftermath");
			names.add("Almost Impossible Game Show");
			names.add("The Amber Rose Show");
			names.add("American Crime");
			names.add("APB");
			names.add("Baby Daddy");
			names.add("Best Friends Whenever");
			names.add("The Blacklist: Redemption");
			names.add("Bloodline");
			names.add("The Catch");
			names.add("Chicago Justice");
			names.add("Conviction");
			names.add("Dr. Ken");
			names.add("Doubt");
			names.add("Emerald City");
			names.add("Escaping the KKK");
			names.add("Eyewitness");
			names.add("Frequency");
			names.add("Gamer’s Guide To Pretty Much Everything");
			names.add("The Get Down");
			names.add("Girl Meets World");
			names.add("Good Girls Revolt");
			names.add("Harvey Beaks");
			names.add("Imaginary Mary");
			names.add("Impastor");
			names.add("Incorporated");
			names.add("Last Man Standing");
			names.add("The Living and the Dead");
			names.add("Loosely Exactly Nicole");
			names.add("Making History");
			names.add("Man Seeking Woman");
			names.add("Mary + Jane");
			names.add("Mercy Street");
			names.add("No Tomorrow");
			names.add("Notorious");
			names.add("The Odd Couple");
			names.add("Outsiders");
			names.add("Pitch");
			names.add("Powerless");
			names.add("Pretty Little Liars");
			names.add("Prison High");
			names.add("Pure Genius");
			names.add("Quarry");
			names.add("The Real O’Neals");
			names.add("Rosewood");
			names.add("Salem");
			names.add("Scream Queens");
			names.add("Secrets and Lies");
			names.add("Sense8");
			names.add("Shots Fired");
			names.add("Sleepy Hollow");
			names.add("Son of Zorn");
			names.add("Stupid Man, Smart Phone");
			names.add("Sun Records");
			names.add("Sweet/Vicious");
			names.add("Training Day");
			names.add("Underground");
			names.add("Vice Principals");
			names.add("The Young Pope");
			names.add("Blunt Talk");
			names.add("BrainDead");
			names.add("Castle");
			names.add("The Knick ");
			names.add("Marco Polo");
			names.add("Please Like Me");
			names.add("Ransom");
			names.add("Roadies");
			names.add("Time After Time");
			getImdbIdByNamesList(names);*/
			
			/*try {
				List<Serie> series = DatabaseHandler.getSeries(true);
				updateEpisodesForSeries(series);
			} 
			catch (NamingException | SQLException e) {
				e.printStackTrace();
			}*/
			/*try {
				List<Serie> series = DatabaseHandler.getSeries(true);
				updateActorsForSeries(series);
			} 
			catch (NamingException | SQLException e) {
				e.printStackTrace();
			}*/
			
			updateSeriesRatings();
		}
	}
	
	private static void updateSeriesRatings()
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
	
	private static void getImdbIdByNamesList(List<String> names) {	
		for(String name : names) {
			try
			{
				//System.out.println("Hole ImdbId für Serie " + name + "...");
				
				String imdbId = ImdbFetcher.getImdbIdByName(name);
				
				System.out.println(name + ", " + imdbId);
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
	
	private static void updateEpisodesForSeries(List<Serie> series) {	
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
