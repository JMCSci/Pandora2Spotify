package testing;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestParse {
	public static void main(String[] args) {
//		 jsonString = jsonString.replaceAll("\\s", "");		// Remove whitespace
//		 String [] tokens1 = jsonString.split("\"uri\":\"spotify:track:");
		
		
		String x = "{  \"tracks\" : {    \"href\" : \"https://api.spotify.com/v1/search?query=track%3AJah+Jah+Give+Us+Dub+artist%3AScientist&type=track&offset=0&limit=20\",    \"items\" : [ ],    \"limit\" : 20,    \"next\" : null,    \"offset\" : 0,    \"previous\" : null,    \"total\" : 0  }}\n" + 
				"";

//		x = x.replaceAll("\\s", "");
//		String [] tokens1 = x.split("\"uri\":\"spotify:track:");
//		 String [] tokens2 = tokens1[1].split("\"");
//		 System.out.println(tokens2[0]);
		JSONObject obj = new JSONObject(x);
		System.out.println(obj.toString(2));
		
		
		
		
		for(int i = 0; i < 3; i++) {
			System.out.println();
		}
		
		JSONObject a = new JSONObject(obj.get("tracks").toString());
		JSONArray arr = new JSONArray(a.getJSONArray("items").toString());
		
		if(!arr.isEmpty()) {
			System.out.println(arr.get(0));
			JSONObject b = new JSONObject(arr.get(0).toString());
			System.out.println(b.names());
			System.out.println(b.get("id"));
		}
		
		String z = "https://api.spotify.com/v1/search?q=track:Step%20To%20It%20artist:D>Tour&type=track\n" + 
				"";
		
//		z = "Don’t Le#$ave (feat. Ellie Goulding)";
//		z = "Innocent / Alex 9000 / \\Innocent II";
		
		System.out.println(z);
		z = z.replaceAll("([^()]*\\])", "");
		z = z.replaceAll("[<>#;$\\\\/\"]", "");
//		z = z.replaceAll("\\\\", "");
		z = z.replaceAll(" ", "%20");
		z = z.replaceAll(Character.toString(8217), "");
		System.out.println(z);
	
		

	}
	
	/* Parse JSON response for track id to be used when adding to playlist 
	 * Paging object contains id
	 * The offset-based paging object is a container for a set of objects. 
	 * It contains a key called items (whose value is an array of the requested objects) 
	 * Along with other keys like previous, next and limit that can be useful in future calls 
	 */
	public static void parseID(String jsonString) {
		JSONObject obj = new JSONObject(jsonString);						// Put paging object into JSONObject
		JSONObject a = new JSONObject(obj.get("tracks").toString());		// Get track object
		JSONArray arr = new JSONArray(a.getJSONArray("items").toString());	// Get JSON array object -	
		System.out.println(arr.get(0));
		JSONObject b = new JSONObject(arr.get(0).toString());
		System.out.println(b.names());
		System.out.println(b.get("id"));
	}

}


