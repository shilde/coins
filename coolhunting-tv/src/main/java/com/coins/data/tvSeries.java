package com.coins.data;

public enum tvSeries {

	breakinBad("Breaking Bad"),
	walkingDead("The Walking Dead"),
	prisonBreak("Prison Break");
	
	private final String name;
	
	private tvSeries(String s) {
		name = s;
	}
	
	public boolean equalsname(String otherName) {
		return name.equals(otherName);
	}
	
	public String toString() {
		return this.name;
	}
}
