package ch.fhnw.cose.tvseries;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ImdbFetcher {
	private static final String apiKey = "1d9af0f5";
	private static final String seriesByNameUrl = "http://www.omdbapi.com/?t=%s&r=xml&apikey=%s";
	private static final String seriesByIdUrl = "http://www.omdbapi.com/?i=%s&r=xml&apikey=%s";
	private static final String episodesByIdUrl = "http://www.omdbapi.com/?i=%s&r=xml&apikey=%s&Season=%s";
		
	private static SAXParser getSAXParser() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    
	    return saxParser;
	}

	public static String getImdbIdByName(String name) throws ParserConfigurationException, SAXException, IOException  {
		
		String nameFixed = name.replace(' ', '+');
		
		URL url = new URL(String.format(seriesByNameUrl, nameFixed, apiKey));
        InputStream is = url.openStream();
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    
	    java.util.List<String> imdbIds = new java.util.ArrayList<String>();
	    saxParser.parse(is, new MyContentHandler(imdbIds));
	    
		return imdbIds.get(0);
	}
	
	public static double getSeriesRatingByImdbId(String imdbId) throws ParserConfigurationException, SAXException, IOException  {
		String url = String.format(seriesByIdUrl, imdbId, apiKey);
		InputStream is = new URL(url).openStream();
		SAXParser saxParser = getSAXParser();

		//Double imdbRating = 0.0;
		Double[] imdbRatings = new Double[1];
		imdbRatings[0] = new Double(0);
		
	    saxParser.parse(is, new DefaultHandler() {
	    	@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) {
	    		if(localName != "movie") { 
	    			return; 
	    		}
	    		
	    		try {
	    			imdbRatings[0] = Double.parseDouble(attributes.getValue("imdbRating"));
	    			//imdbRating = Double.parseDouble(attributes.getValue("Rating"));
	    		}
	    		catch(Exception e) {
	    			System.out.println("Ungültiges Format für Rating " + attributes.getValue("imdbRating"));
	    		}
	    		
			}
		});
	    
	    return imdbRatings[0];
	}
	
	public static List<Episode> getEpisodesForSeason(String imdbID, int seasonNr) 
			throws ParserConfigurationException, SAXException, IOException, Exception  {
		
		if(imdbID == null || imdbID == "") {
			return new ArrayList<Episode>();
		}
		
		String url = String.format(episodesByIdUrl, imdbID, apiKey, seasonNr);
		InputStream is = new URL(url).openStream();
		SAXParser saxParser = getSAXParser();
	    List<Episode> episodes = new ArrayList<Episode>();
	    
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	    
	    saxParser.parse(is, new DefaultHandler() {
	    	@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) {
	    		if(localName == "error") { 
					throw new IllegalArgumentException("Keine Staffel " + seasonNr);
	    		}
	    		
	    		if(localName != "result") { 
	    			return; 
	    		}
	    		
	    		String title = attributes.getValue("Title");
	    		String imdbID = attributes.getValue("imdbID");
	    		Double imdbRating = 0.0;
	    		try {
	    			imdbRating = Double.parseDouble(attributes.getValue("imdbRating"));
	    		}
	    		catch(Exception e) {
	    			System.out.println("Ungültiges Format für Rating " + attributes.getValue("imdbRating"));
	    		}
	    		
	    		int episode = Integer.parseInt(attributes.getValue("Episode"));
	    		Date released = null;
				
	    		try {
	    			String releaseDate = attributes.getValue("Released");
					released = df.parse(releaseDate);
				} 
	    		catch (Exception e) {
					System.out.println("Released " + attributes.getValue("Released") + " konnte nicht geparsed werden.");
				}
	    		
	    		episodes.add(new Episode(title, imdbID, imdbRating, 0, seasonNr, episode, released));
			}
		});

		return episodes;
	}

	public static List<String> getActorsForSeries(String imdbID) throws ParserConfigurationException, SAXException, IOException  {
		String url = String.format(seriesByIdUrl, imdbID, apiKey);
		InputStream is = new URL(url).openStream();
		SAXParser saxParser = getSAXParser();
	    List<String> actors = new ArrayList<String>();

	    saxParser.parse(is, new DefaultHandler() {
	    	@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	    		if(localName != "movie") { 
	    			return; 
	    		}
	    		
	    		String value = attributes.getValue("actors");
	    		
	    		if(value == null)
	    			return;
	    		
	    		actors.addAll(Arrays.asList(value.split(", ")));
			}
		});
	    
		return actors;
	}
	
	static class MyContentHandler extends org.xml.sax.helpers.DefaultHandler
	{
		private java.util.List<String> imdbIds;
		public MyContentHandler(java.util.List<String> imdbIds) {
			this.imdbIds = imdbIds;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {

			if(localName != "movie")
			{ return; }
			
			String imdbId = attributes.getValue("imdbID");
			imdbIds.add(imdbId);
		}
	}
}