package ch.fhnw.cose.tvseries;
import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.Test;

public class DatabaseHandlerTest {

	@Test
	public void getSeries() throws NamingException, SQLException {
		java.util.List<Serie> series = DatabaseHandler.getSeries();
	}
	
	@Test
	public void insertEpisode() throws NamingException, SQLException {
		DatabaseHandler.insertEpisode(1, 1, 1, 8.5);
	}
}