package com.coins.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.coins.db.DatabaseHelper;
import com.coins.model.TvSerie;


public class TvSeriesService {

	private static TvSeriesService instance;
	private static final Logger LOGGER = Logger.getLogger(TvSeriesService.class.getName());

	private final HashMap<String, TvSerie> series = new HashMap<>();

	private long nextId = 0;

	private TvSeriesService() {

	}

	public static TvSeriesService getInstance() {
		if (instance == null) {
			instance = new TvSeriesService();
			instance.getAllseries();
		}
		return instance;
	}
	
	public synchronized List<TvSerie> findAll() {
		return findAll(null);
	}

	public synchronized List<TvSerie> findAll(String stringFilter) {
		ArrayList<TvSerie> arrayList = new ArrayList<>();
		for (TvSerie series : series.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| series.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(series.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(TvSeriesService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<TvSerie>() {

			@Override
			public int compare(TvSerie o1, TvSerie o2) {
				return (int) (o2.getId().compareTo(o1.getId()));
			}
		});
		return arrayList;
	}
	
	public synchronized void save(TvSerie entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE,
					"Serie is null!");
			return;
		}
		if (entry.getId() == null) {
			//entry.setId(nextId++);
		}
		try {
			entry = (TvSerie) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		series.put(entry.getId(), entry);
	}
	
	/*
	 * Some Test Data
	 */
	public void getAllseries() {
		
		List<TvSerie> _series = new ArrayList<TvSerie>();
		
		if (findAll().isEmpty()) {
			
			DatabaseHelper db = new DatabaseHelper();
			db.getConnection();
			_series = db.getAllSeries();
			
			Random r = new Random(0);
			for (TvSerie serie : _series) {
				TvSerie c = new TvSerie();
				c.setName(serie.getName());
				c.setId(serie.getId());
				save(c);
			}
		}
	}
	
	public HashMap<String, TvSerie> getSeries() {
		return series;
	}
}
