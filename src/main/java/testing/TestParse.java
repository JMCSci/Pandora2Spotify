package testing;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestParse {
	public static void main(String[] args) {
//		 jsonString = jsonString.replaceAll("\\s", "");		// Remove whitespace
//		 String [] tokens1 = jsonString.split("\"uri\":\"spotify:track:");
		
		
		String x = "{  \"tracks\" : {    \"href\" : \"https://api.spotify.com/v1/search?query=track%3AHoney-Dipped+artist%3ADave+Koz&type=track&market=US&offset=0&limit=1\",    \"items\" : [ {      \"album\" : {        \"album_type\" : \"compilation\",        \"artists\" : [ {          \"external_urls\" : {            \"spotify\" : \"https://open.spotify.com/artist/0ZcJXldoq09BRIMl0Qh1Vm\"          },          \"href\" : \"https://api.spotify.com/v1/artists/0ZcJXldoq09BRIMl0Qh1Vm\",          \"id\" : \"0ZcJXldoq09BRIMl0Qh1Vm\",          \"name\" : \"Dave Koz\",          \"type\" : \"artist\",          \"uri\" : \"spotify:artist:0ZcJXldoq09BRIMl0Qh1Vm\"        } ],        \"external_urls\" : {          \"spotify\" : \"https://open.spotify.com/album/4zVaKhVCYp4DaQZDIKO3TV\"        },        \"href\" : \"https://api.spotify.com/v1/albums/4zVaKhVCYp4DaQZDIKO3TV\",        \"id\" : \"4zVaKhVCYp4DaQZDIKO3TV\",        \"images\" : [ {          \"height\" : 640,          \"url\" : \"https://i.scdn.co/image/ab67616d0000b273df4ba1fc6673be8afcb0b1aa\",          \"width\" : 640        }, {          \"height\" : 300,          \"url\" : \"https://i.scdn.co/image/ab67616d00001e02df4ba1fc6673be8afcb0b1aa\",          \"width\" : 300        }, {          \"height\" : 64,          \"url\" : \"https://i.scdn.co/image/ab67616d00004851df4ba1fc6673be8afcb0b1aa\",          \"width\" : 64        } ],        \"name\" : \"Greatest Hits\",        \"release_date\" : \"2008-01-01\",        \"release_date_precision\" : \"day\",        \"total_tracks\" : 15,        \"type\" : \"album\",        \"uri\" : \"spotify:album:4zVaKhVCYp4DaQZDIKO3TV\"      },      \"artists\" : [ {        \"external_urls\" : {          \"spotify\" : \"https://open.spotify.com/artist/0ZcJXldoq09BRIMl0Qh1Vm\"        },        \"href\" : \"https://api.spotify.com/v1/artists/0ZcJXldoq09BRIMl0Qh1Vm\",        \"id\" : \"0ZcJXldoq09BRIMl0Qh1Vm\",        \"name\" : \"Dave Koz\",        \"type\" : \"artist\",        \"uri\" : \"spotify:artist:0ZcJXldoq09BRIMl0Qh1Vm\"      } ],      \"disc_number\" : 1,      \"duration_ms\" : 264293,      \"explicit\" : false,      \"external_ids\" : {        \"isrc\" : \"USCA20300384\"      },      \"external_urls\" : {        \"spotify\" : \"https://open.spotify.com/track/18JSBFM8i0I2H8vnPWK1Bf\"      },      \"href\" : \"https://api.spotify.com/v1/tracks/18JSBFM8i0I2H8vnPWK1Bf\",      \"id\" : \"18JSBFM8i0I2H8vnPWK1Bf\",      \"is_local\" : false,      \"is_playable\" : true,      \"name\" : \"Honey-Dipped\",      \"popularity\" : 27,      \"preview_url\" : null,      \"track_number\" : 6,      \"type\" : \"track\",      \"uri\" : \"spotify:track:18JSBFM8i0I2H8vnPWK1Bf\"    } ],    \"limit\" : 1,    \"next\" : \"https://api.spotify.com/v1/search?query=track%3AHoney-Dipped+artist%3ADave+Koz&type=track&market=US&offset=1&limit=1\",    \"offset\" : 0,    \"previous\" : null,    \"total\" : 3  }}";
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
		System.out.println(arr.get(0));
		JSONObject b = new JSONObject(arr.get(0).toString());
		System.out.println(b.names());
		System.out.println(b.get("id"));
		

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


