package com.coins.services.pesonalityinsights;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.coins.fetcher.twitter.Twitter4JHelper;

import twitter4j.Status;

public class PersonalityInsightsAnalyzer {

	public void main() throws Exception {
		Properties props = new Properties();
		props.load(FileUtils.openInputStream(new File("config.properties")));
		
		Twitter4JHelper twitterHelper = new Twitter4JHelper(props);
		//PersonalityInsightsHelper piHelper = new PersonalityInsightsHelper(props);
		//ToneAnalyzerHelper taHelper = new ToneAnalyzerHelper(props);
		
		HashSet<String> langs = new HashSet<String>();
		langs.add("en");
		
		List<Status> tweets = twitterHelper.getTweetsFromQuery("@GameOfThrones", langs, 200);
		
		for (int i = 0; i < tweets.size(); i++) {
			System.out.println(tweets.get(i).getText());
		}
		
		String contentItemsJson = twitterHelper.convertTweetsToPIContentItems(tweets);
		//String profile = piHelper.getProfileJSON(contentItemsJson, false);
		//String profile = piHelper.getProfileCSV(contentItemsJson, false);
		//String tone = taHelper.getToneJSON(contentItemsJson, false);
		//System.out.println(tone);
	}

}
