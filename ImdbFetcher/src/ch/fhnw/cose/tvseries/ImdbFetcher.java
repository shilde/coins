package ch.fhnw.cose.tvseries;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ImdbFetcher {
	
	public static String getImdbIdByName(String name) throws ParserConfigurationException, SAXException, IOException  {
		
		String nameFixed = name.replace(' ', '+');
		
		URL url = new URL(String.format("http://www.omdbapi.com/?t=%s&r=xml&apikey=1d9af0f5", nameFixed));
        InputStream is = url.openStream();
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    
	    java.util.List<String> imdbIds = new java.util.ArrayList<String>();
	    saxParser.parse(is, new MyContentHandler(imdbIds));
	    
		return imdbIds.get(0);
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