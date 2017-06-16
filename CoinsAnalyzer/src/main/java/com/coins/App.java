package com.coins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.coins.fetcher.reddit.RedditFetcher;
import com.coins.fetcher.twitter.Twitter4JHelper;
import com.coins.services.pesonalityinsights.PersonalityInsightsHelper;

import twitter4j.Status;

public class App {

	public static void main(String[] args) throws Exception {
		String handle = "HIMYM";

		RedditFetcher fetcher = new RedditFetcher();
		String posts = fetcher.getJsonContent(handle);
		
		System.out.println(posts);
		/*Properties props = new Properties();
		props.load(FileUtils.openInputStream(new File("config.properties")));

		Twitter4JHelper twitterHelper = new Twitter4JHelper(props);
		PersonalityInsightsHelper piHelper = new PersonalityInsightsHelper(props);

		HashSet<String> langs = new HashSet<String>();
		langs.add("en");

		List<Status> tweets = twitterHelper.getTweetsFromUser("@" + handle, langs, 5000);

		System.out.println("Nummber of tweets: " + tweets.size());

		String contentItemsJson = twitterHelper.convertTweetsToPIContentItems(tweets);
		String profile = piHelper.getProfileJSON(contentItemsJson, false);

		PrintWriter pWriter = null;
		pWriter = new PrintWriter(new BufferedWriter(new FileWriter(handle + " Personality.txt")));
		pWriter.println(profile);
		pWriter.flush();
		pWriter.close();
		
		System.out.println(profile);*/

	}

}
