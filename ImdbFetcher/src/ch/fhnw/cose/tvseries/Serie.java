package ch.fhnw.cose.tvseries;

public class Serie {
	public Serie(int id, String name, boolean canceled, String imdbId) {
		this.id = id;
		this.name = name;
		this.canceled = canceled;
		this.imdbId = imdbId;
	}
	public int id;
	public String name;
	public boolean canceled;
	public String imdbId;
}