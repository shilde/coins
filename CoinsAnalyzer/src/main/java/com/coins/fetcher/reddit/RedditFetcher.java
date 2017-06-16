package com.coins.fetcher.reddit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RedditFetcher {
	
	
	
	
	public String getJsonContent(String handle) {
		
		StringBuffer sb = new StringBuffer();
		String s = "";
		
		JsonObject json = loadJson(handle);

		JsonObject data;
		JsonArray children;

		data = json.getAsJsonObject("data");
		children = data.getAsJsonArray("children");

		for (int i = 0; i < children.size(); i++) {
			JsonObject a = children.get(i).getAsJsonObject();
			JsonObject b = a.getAsJsonObject("data");
			System.out.println(b.get("body").getAsString());
			s.concat(b.get("body").getAsString());
			sb.append(s);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	public String getRedditComments(String subreddit) throws IOException {

		JsonObject json = null;

		//json = readJson(new File("C:/repository/CoinsAnalyzer/BetterCallSaul.txt"));

		return "";
	}

	public JsonObject loadJson(String name) {

		JsonObject json = null;

		try {
			json = readJson(new File("C:/repository/CoinsAnalyzer/TimeAfterABC.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	private JsonObject readJson(File file) throws IOException {
		BufferedReader rd = new BufferedReader(new FileReader(file));
		String json = readAll(rd);
		JsonElement jelement = new JsonParser().parse(json);
		JsonObject jobject = jelement.getAsJsonObject();
		return jobject;
	}

	private String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
