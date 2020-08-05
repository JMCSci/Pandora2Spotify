/* Spotify API Wrapper
 * Request w/o user profile access
 * Uses Firefox WebDriver and Selenium API
 * This flow is suitable for long-running applications in which the user grants permission only once. 
 * It provides an access token that can be refreshed. 
 * Since the token exchange involves sending your secret key, perform this on a secure location, like a backend service
 * Do not use from a client such as a browser or from a mobile app.
 */

// Authorization Grant Flow - Refreshable User Authorization

package pandora2spotify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpotifyAPI {
	private LinkedHashMap<String, String> currentUser = new LinkedHashMap<String, String>();
	private Authorization authorization;
	private PandoraRequests pandora;
	private int requests = 0;
	private JSONObject json;
	
	SpotifyAPI(Authorization authorization, PandoraRequests pandora, Secrets secrets) throws Exception {
		this.authorization = authorization;			// Request Spotify Web API authorization
	}
	
	// addCurrentUser: Adds current user data to HashMap
	void addCurrentUser() throws Exception {
		if(requests >= 1) {			// Automatically ask for for refresh tokens after first API request
			authorization.requestRefresh();
		}
		if(currentUser.isEmpty()) {
			URL url = new URL("https://api.spotify.com/v1/me/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", authorization.getToken("token_type") + 
					" " + authorization.getToken("access_token"));
			
			// Read the response - JSON
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String readLine = "";
			StringBuilder line = new StringBuilder();
			while((readLine = reader.readLine()) != null){
				line.append(readLine);
			}
			
			clearJSON();
			json = new JSONObject(line.toString());
			JSONObject nestedFollow = new JSONObject(json.optString("followers"));	// access nested data under this category
			
			currentUser.put("id", json.get("id").toString());
			currentUser.put("display_name", json.get("display_name").toString());
			currentUser.put("product", json.get("product").toString());
			currentUser.put("type", json.get("type").toString());
			currentUser.put("followers", nestedFollow.get("total").toString());
			System.out.println(currentUser);
			reader.close();
		} else {
			System.out.println("Current user data has already been retrieved");
		}
		requests++;
	}
	
	/* Create a playlist for tracks to be add
	 * This should be named at the beginning of the program with user input
	 * Add called AFTER */
	void createPlaylist() throws Exception {
		if(requests >= 1) {			// Automatically ask for for refresh tokens after first API request
			authorization.requestRefresh();
		}
		System.out.println(authorization.getToken("token_type") + "\n" + authorization.getToken("access_token"));
		URL url = new URL("https://api.spotify.com/v1/users/" + currentUser.get("id") + "/playlists");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", authorization.getToken("token_type") + " " +
				authorization.getToken("access_token"));
		
		String postRequest = "{\n\"name\":\"Pandora Liked Songs\",\n" + 
				"\"public\":false\n" + "}";
		
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(postRequest.getBytes());
		out.close();
		
		InputStream in = conn.getInputStream();
		
		String readLine = "";
		StringBuilder line = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		while((readLine = reader.readLine()) != null) {
			line.append(readLine);
		}
		
		json = new JSONObject(line.toString());
		
		reader.close();
	}
	
	void userPlaylists() throws Exception {
		URL url = new URL("https://api.spotify.com/v1/me/playlists");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(false);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", authorization.getToken("token_type") + 
				" " + authorization.getToken("access_token"));
		
		String readline = "";
		StringBuilder line = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		while((readline = reader.readLine())!= null) {
			line.append(readline);
		}
		
		json = new JSONObject(line.toString());
		
		boolean playlistFound = parseJsonArray(json);
		reader.close();
		
		if(playlistFound == false) {
			System.exit(-1);
		}
		
	}
	
	// Parse json from userPlaylists to retrieve playlist id
	boolean parseJsonArray(JSONObject json) {
		String playlistID = "";
		JSONArray arr = new JSONArray(json.get("items").toString());				// Create JSON array from JSON object
		int size = arr.length();													// Get the length of the array
		for(int i = 0; i < size; i++) {
			JSONObject obj = new JSONObject(arr.get(i).toString());	   			    // Add array object to JSON object
			if(obj.get("name").toString().matches("Pandora Liked Songs")) {  	// Get the name in this JSON
				playlistID = obj.get("id").toString();								// Get the playlist id
				currentUser.put("playlist_id", playlistID);							// add playlist id to hash map
				return true;
			} 
		}
		System.out.println("Playlist not found");
		return false;
	}
	

	String searchForItem(String query) throws Exception {
//		String query = "q=track:Honey-Dipped%20artist:Dave%20Koz&type=track&limit=1&offset=0&market=US";
//		String query = pandora.getSongs();
		
		URL url = new URL("https://api.spotify.com/v1/search?" + query);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(false);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", authorization.getToken("token_type") + " " + authorization.getToken("access_token"));

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String readLine = "";
		StringBuilder line = new StringBuilder("");
		
		while((readLine = reader.readLine()) != null) {
			line.append(readLine);
		}
		
		String jsonString = line.toString();
		
		reader.close();
		return jsonString;
	}
	
	/* Add tracks to playlist */
	void addToPlaylist(String jsonArray) throws Exception {
		if(requests >= 1) {		// Automatically ask for for refresh tokens after first API request
			authorization.requestRefresh();
		}
		URL url = new URL("https://api.spotify.com/v1/playlists/" + currentUser.get("playlist_id") + "/tracks");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", authorization.getToken("token_type") + " "
				+ authorization.getToken("access_token"));
		
//		String postRequest = "{\"uris\":[\"spotify:track:4YohGkPPpo7YoZBLu5Yfux\",\"spotify:track:6dRCHL8tSG8yuZ53ZKGwdI\",\"spotify:track:2bKsGQ1zCGuUK2bjvovZ92\"]}";
		String postRequest = jsonArray;
		
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(postRequest.getBytes());
		out.close();
		
//		System.out.println(conn.getResponseCode());
	}
	
	
	/* Creates a JSON array to send as POST request when adding songs to playlist */
//	String createJSONArray() {
//		JSONArray arr = new JSONArray();				// Create an empty JSONArray
//		String prefix = "spotify:track:";
//		/* This is will be in a loop with a maxiumum of 100 tracks */
//		arr.put(0, prefix + "PnzycXmUIhzQbSfQlw4uJD");		// Add tracks
//		arr.put(1, prefix + "VD8Fzurt4xHCPh8uETFxjz");		// Add tracks
//		arr.put(2, prefix + "nM7r70zyXRfqinWUkVLdy2");		// Add tracks
//		JSONObject o = new JSONObject();					// Create JSONObject
//		o.put("uris", arr);									// Add the JSONArray to the JSONObject
//		String jsonArray = o.toString();					// Convert it to a string
//		jsonArray = jsonArray.replaceAll("//s","");			// Remove whitespace
//		System.out.println(jsonArray);						// Return the string to addToPlaylist method
//		return jsonArray;
//	}
	
	/* Creates a JSON array to send as POST request when adding songs to playlist */
	String createJSONArray() {
		JSONArray arr = new JSONArray();				// Create an empty JSONArray
		String prefix = "spotify:track:";
		/* This is will be in a loop with a maxiumum of 100 tracks */
		// Get the size of ids array
		int size = pandora.idListSize();
		
		if(size / 100 > 0) {
			System.out.println("at least 100");
			// Pop 100 tracks from the queue
			for(int i = 0; i < 100; i++) {
				arr.put(i, prefix +  pandora.getIds());		// Add 100 tracks to JSON array
			}
		} else {
			// Pop everything off of the queue
			for(int i = 0; i < size; i++) {
				arr.put(i, prefix +  pandora.getIds());		// Add remaining tracks to JSON array
			}
		}
		JSONObject o = new JSONObject();					// Create JSONObject
		o.put("uris", arr);									// Add the JSONArray to the JSONObject
		String jsonArray = o.toString();					// Convert it to a string
		jsonArray = jsonArray.replaceAll("//s","");			// Remove whitespace						
		return jsonArray;									// Return the string to addToPlaylist method
	}

	void clearJSON() {
		json = null;
	}
	
	void clearMap(HashMap<String, String> hashmap) {
		hashmap.clear();
	}

}
