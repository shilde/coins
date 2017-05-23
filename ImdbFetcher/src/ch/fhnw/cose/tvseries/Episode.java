package ch.fhnw.cose.tvseries;

import java.util.Date;

public class Episode {
	public Episode(String title, String imdbID, double imdbRating, int seriesId, int season, int episode, Date released) {
		this.title = title;
		this.imdbID = imdbID;
		this.imdbRating = imdbRating;
		this.seriesId = seriesId;
		this.season = season;
		this.episode = episode;
		this.released = released;
	}
	
	public String title;
	public String imdbID;
	public double imdbRating;
	public int seriesId;
	public int season;
	public int episode;
	public Date released;
}
