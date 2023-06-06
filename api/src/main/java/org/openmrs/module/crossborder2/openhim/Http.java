/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.crossborder2.openhim;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Http {
	
	private static final String URL = "http://hiedhs.intellisoftkenya.com:5001/";
	
	private static final String USERNAME = "kemr";
	
	private static final String PASSWORD = "password";
	
	public String get(String endpoint, String query) {
		HttpClient httpClient = getHttpClient();
		String encodedQuery = "";
		try {
			encodedQuery = URLEncoder.encode(query, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		HttpGet request = new HttpGet(URL + endpoint + "?" + encodedQuery);
		request.setHeader("Accept", "application/fhir+json");
		HttpResponse response;
		String responseBody;
		try {
			response = httpClient.execute(request);
			responseBody = EntityUtils.toString(response.getEntity());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return responseBody;
	}
	
	public String post(String endpoint, String payload) {
		return post(endpoint, payload, null);
	}
	
	public String post(String endpoint, String payload, String query) {
		HttpClient client = getHttpClient();
		HttpPost request = new HttpPost(URL + endpoint + (query != null ? "?" + query : ""));
		request.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(payload).build());
		HttpResponse response;
		String jsonResponse = null;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() >= 200) {
				HttpEntity entity = response.getEntity();
				jsonResponse = EntityUtils.toString(entity);
			} else {
				System.out.println("Request failed: " + response.getStatusLine().getStatusCode() + " "
				        + response.getStatusLine().getReasonPhrase());
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return jsonResponse;
	}
	
	public String put(String endpoint, String payload, String query) {
		HttpClient client = getHttpClient();
		HttpPut request = new HttpPut(URL + endpoint + "?" + query);
		request.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(payload).build());
		HttpResponse response;
		String jsonResponse = null;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() >= 200) {
				HttpEntity entity = response.getEntity();
				jsonResponse = EntityUtils.toString(entity);
			} else {
				System.out.println("Request failed: " + response.getStatusLine().getStatusCode() + " "
				        + response.getStatusLine().getReasonPhrase());
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return jsonResponse;
	}
	
	private HttpClient getHttpClient() {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));
		HttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
		return httpClient;
	}
}
