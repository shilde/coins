package com.coins.model;

import java.io.Serializable;

public class TvSerie implements Serializable, Cloneable {

	private String id;
	private String name = "";
	
	public TvSerie(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public TvSerie() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public TvSerie clone() throws CloneNotSupportedException {
		return (TvSerie) super.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.id == null) {
			return false;
		}

		if (obj instanceof TvSerie && obj.getClass().equals(getClass())) {
			return this.id.equals(((TvSerie) obj).id);
		}

		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
