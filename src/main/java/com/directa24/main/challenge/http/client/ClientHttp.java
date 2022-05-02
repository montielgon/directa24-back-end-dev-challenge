package com.directa24.main.challenge.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.directa24.main.challenge.exception.HttpConnectionFailedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHttp {
	public static final String ENDPOINT = "https://directa24-movies.mocklab.io/api";

	public static HttpURLConnection createConnection(String method, String url) {
		HttpURLConnection connection = null;
		try {
			URL urlObject = new URL(String.format(url));
			connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod(method);
		} catch (ProtocolException e) {
			log.error("The request method provided is invalid. Method: {}", method);
		} catch (MalformedURLException e) {
			log.error("The url provided is invalid. URL: {} ", url);
		} catch (IOException e) {
			log.error("Could not establish connection. {} - {}", method, url);
		}
		if (connection == null) {
			throw new HttpConnectionFailedException();
		}
		return connection;
	}

	public static String getResponse(HttpURLConnection con) {
		StringBuilder content = new StringBuilder();
		Reader streamReader = null;

		try {
			streamReader = con.getResponseCode() > 299 ? new InputStreamReader(con.getErrorStream()) : new InputStreamReader(con.getInputStream());
			BufferedReader in = new BufferedReader(streamReader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}

			in.close();
		} catch (IOException e) {
			log.error("An unexpected error occurred while trying to get the http connection response");
		}
		con.disconnect();
		return content != null ? content.toString() : null;
	}
}
