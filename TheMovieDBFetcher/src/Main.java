import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	private static DB db = new DB();
	private static List<JSONObject> actorsMovieDb = loadActorsFromFile();

	public static void main(String[] args) throws SQLException {
		updatePopularity();
	}

	@SuppressWarnings("resource")
	public static List<JSONObject> loadActorsFromFile() {
		JSONParser parser = new JSONParser();
		BufferedReader br;
		List<JSONObject> actorsMovieDb = new ArrayList<JSONObject>();
		try {
			br = new BufferedReader(new FileReader(
					"./person_ids_05_18_2017.json/person_ids_05_18_2017.json"));
			String line;
			// load all valid persons into a list
			while ((line = br.readLine()) != null) {
				JSONObject obj = (JSONObject) parser.parse(line);
				actorsMovieDb.add(obj);
			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return actorsMovieDb;
	}

	public static void updatePopularity() {
		try {
			ResultSet actors = db
					.runSql("SELECT idActor, name FROM Actor WHERE popularity IS NULL;");
			while (actors.next()) {
				int id = actors.getInt(1);
				String name = actors.getString(2);
				for (JSONObject actor : actorsMovieDb) {
					String n = (String) actor.get("name");
					if (n.equalsIgnoreCase(name)) {
						int movieDbId = (int) (long) actor.get("id");
						org.json.JSONObject obj = JsonReader
								.readJsonFromUrl("https://api.themoviedb.org/3/person/"
										+ movieDbId
										+ "?api_key=74f54b82e98587cc28e0131ddabf4e40");
						double p = (double) obj.getDouble("popularity");
						db.runSql2("UPDATE Actor SET popularity = " + p
								+ ", movieDB_id = " + movieDbId
								+ " WHERE idActor = +" + id + ";");
						break;
					}
				}

			}

		} catch (SQLException | IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void fullLoadTable() {
		// truncate table for full load
		try {
			db.runSql2("TRUNCATE Actor;");

			for (JSONObject obj : actorsMovieDb) {
				String name = (String) obj.get("name");
				double popularity = (double) obj.get("popularity");
				int id = (int) (long) obj.get("id");
				PreparedStatement stmt = db.conn
						.prepareStatement("INSERT INTO Actor (name, popularity, movieDB_id) VALUES (?,?,?);");
				stmt.setString(1, name);
				stmt.setDouble(2, popularity);
				stmt.setInt(3, id);
				stmt.executeUpdate();
			}
			System.out.println("Full table load completed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
