package ch.fhnw.cose.tvseries;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ImdbFetcher {
	private static final String apiKey = "1d9af0f5";
	private static final String seriesByNameUrl = "http://www.omdbapi.com/?t=%1s&r=xml&apikey=%2s";
	private static final String seriesByIdUrl = "http://www.omdbapi.com/?i=%1s&r=xml&apikey=%2s";
	
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