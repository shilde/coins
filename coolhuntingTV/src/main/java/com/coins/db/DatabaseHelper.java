package com.coins.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.coins.model.TvSerie;
import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseHelper {

	private static final String ENDPOINT = "db.endpoint";
	private static final String USERNAME = "db.username";
	private static final String PASSWORD = "db.password";
	
	private Properties props;
	private Connection conn;
	
	public DatabaseHelper() {
		props = new Properties();
		//props.load(FileUtils.openInputStream(new File("database.properties")));
	}
	
	public void getConnection () {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("");
		dataSource.setUser("");
		dataSource.setPassword("");
		dataSource.setDatabaseName("coins");
		
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<TvSerie> getAllSeries() throws SQLException {
		Statement statement = (Statement) conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT Name, ImdbId FROM Series");
		
		List<TvSerie> series = new ArrayList<TvSerie>();
		
		while(rs.next()) {
			series.add(new TvSerie(rs.getString("Name"), rs.getString("imdbId")));
		}
		
		rs.close();
		statement.close();
		
		return series;
	}
}
