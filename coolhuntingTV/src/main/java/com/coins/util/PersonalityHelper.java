package com.coins.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.server.VaadinRequest;
import com.vaadin.util.CurrentInstance;

public class PersonalityHelper {

	public ListSeries getPersonality(String name) {

		ListSeries line1;
		Double[] l = new Double[5];

		JsonObject json = loadJson("personalityJson/" + name);

		JsonObject watson;
		JsonArray tree;

		watson = json.getAsJsonObject("tree");
		tree = watson.getAsJsonArray("children");

		for (int i = 0; i < 1; i++) {
			JsonObject children = tree.get(i).getAsJsonObject();
			JsonArray a = children.getAsJsonArray("children");
			for (int j = 0; j < a.size(); j++) {
				JsonObject o = a.get(j).getAsJsonObject();
				JsonArray b = o.getAsJsonArray("children");
				for (int k = 0; k < 5; k++) {
					JsonObject u = b.get(k).getAsJsonObject();
					String t = u.get("percentage").getAsString();
					l[k] = Double.parseDouble(t.substring(0, 4)) * 100;
				}
			}
		}

		line1 = new ListSeries(l[0], l[1], l[2], l[3], l[4]);

		return line1;
	}

	public ListSeries getConsumerNeeds(String name) {
		ListSeries line1 = null;
		JsonObject json = loadJson("personalityJson/" + name);

		JsonObject watson;
		JsonArray tree;

		watson = json.getAsJsonObject("tree");
		tree = watson.getAsJsonArray("children");

		for (int i = 1; i < 2; i++) {
			JsonObject children = tree.get(i).getAsJsonObject();
			JsonArray a = children.getAsJsonArray("children");
			for (int j = 0; j < a.size(); j++) {
				JsonObject o = a.get(j).getAsJsonObject();
				JsonArray b = o.getAsJsonArray("children");

				JsonObject u = b.get(4).getAsJsonObject();
				String s1 = u.get("percentage").getAsString();
				Double d1 = Double.parseDouble(s1.substring(0, 4)) * 100;
				u = b.get(2).getAsJsonObject();
				String s2 = u.get("percentage").getAsString();
				Double d2 = Double.parseDouble(s2.substring(0, 4)) * 100;
				u = b.get(10).getAsJsonObject();
				String s3 = u.get("percentage").getAsString();
				Double d3 = Double.parseDouble(s3.substring(0, 4)) * 100;
				u = b.get(9).getAsJsonObject();
				String s4 = u.get("percentage").getAsString();
				Double d4 = Double.parseDouble(s4.substring(0, 4)) * 100;
				u = b.get(3).getAsJsonObject();
				String s5 = u.get("percentage").getAsString();
				Double d5 = Double.parseDouble(s5.substring(0, 4)) * 100;
				line1 = new ListSeries(d1, d2, d3, d4, d5);
			}
		}

		return line1;
	}
	
	public Double[] getEmotion(String name) {
		
		Double[] list = new Double[5];
		JsonObject json = loadJson("toneJson/" + name);
		
		JsonObject watson;
		JsonArray categories;
		
		watson = json.getAsJsonObject("document_tone");
		categories = watson.getAsJsonArray("tone_categories");
		
		for (int i = 0; i < 1; i++) {
			JsonObject o = categories.get(i).getAsJsonObject();
			JsonArray a = o.getAsJsonArray("tones");
			for (int j = 0; j < a.size(); j++) {
				JsonObject h = a.get(j).getAsJsonObject();
				String s = h.get("score").getAsString();
				list[j] = Double.parseDouble(s.substring(0, 4)) * 100;
			}
		}
		
		return list;
	}
	
	public Double[] getSocialTendencies(String name) {
		Double[] list = new Double[5];
		JsonObject json = loadJson("toneJson/" + name);
		
		JsonObject watson;
		JsonArray categories;
		
		watson = json.getAsJsonObject("document_tone");
		categories = watson.getAsJsonArray("tone_categories");
		
		for (int i = 2; i < 3; i++) {
			JsonObject o = categories.get(i).getAsJsonObject();
			JsonArray a = o.getAsJsonArray("tones");
			for (int j = 0; j < a.size(); j++) {
				JsonObject h = a.get(j).getAsJsonObject();
				String s = h.get("score").getAsString();
				list[j] = Double.parseDouble(s.substring(0, 4)) * 100;
			}
		}
		
		return list;
	}

	public JsonObject loadJson(String name) {

		VaadinRequest vaadinRequest = CurrentInstance.get(VaadinRequest.class);
		JsonObject json = null;

		File baseDirectory = vaadinRequest.getService().getBaseDirectory();
		try {
			json = readJson(new File(baseDirectory + "/WEB-INF/classes/" + name + ".txt"));
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

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	
}
