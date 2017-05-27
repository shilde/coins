package com.coins.services.toneanalyzer;

import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class ToneAnalyzerHelper {

	public static final String TA_URL_PROP_NAME = "ta.url";
	public static final String TA_USERNAME_PROP_NAME = "ta.username";
	public static final String TA_PASSWORD_PROP_NAME = "ta.password";

	URI uri;
	Client client;
	
	ToneAnalyzer service;

	public ToneAnalyzerHelper(Properties props) {
		if (StringUtils.isEmpty(props.getProperty(TA_URL_PROP_NAME))
				|| StringUtils.isEmpty(props.getProperty(TA_USERNAME_PROP_NAME))
				|| StringUtils.isEmpty(props.getProperty(TA_PASSWORD_PROP_NAME))) {
			System.err.println("Some properties are not specified. Check Config!");
		}

		service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
		service.setEndPoint(props.getProperty(TA_URL_PROP_NAME));
		service.setUsernameAndPassword(props.getProperty(TA_USERNAME_PROP_NAME), props.getProperty(TA_PASSWORD_PROP_NAME));
	}

	public String getToneJSON(String contentItemJson, boolean includeRaw) {
		ToneAnalysis tone = service.getTone(contentItemJson, null).execute();
		return tone.toString();
	}

}
