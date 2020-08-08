package testing;

import org.json.JSONObject;

public class JSONTest {
	public static void main(String[] args) {
		String x = "{\"listenerId\":\"82194757\",\"webname\":\"tarius12\",\"private\":true,\"followState\":\"cannotFollow\",\"fullName\":\"TG\",\"positiveFeedbackCount\":1926,\"stationCount\":13,\"bookmarkCount\":119,\"followingCount\":0,\"followersCount\":0,\"bio\":\"\"}";
		JSONObject obj = new JSONObject(x);
		int totalThumbs = Integer.parseInt(obj.get("positiveFeedbackCount").toString());
		// Round the number up 
		System.out.println((1923 % 10));
		
		// 10 songs
		int roundUp = (97 % 10) - 10;
		System.out.println(roundUp);
		totalThumbs = Math.abs(roundUp) + 97;
		int refreshTotal = totalThumbs / 10;
		System.out.println("Refresh total: " + refreshTotal);
				
		// 100 songs
		roundUp = (999 % 10) - 10;
		System.out.println(roundUp);
		totalThumbs = Math.abs(roundUp) + 999;
		refreshTotal = totalThumbs / 10;
		System.out.println("Refresh total: " + refreshTotal);
				
		// 1000 songs
		roundUp = (1923 % 10) - 10;
		System.out.println(roundUp);
		totalThumbs = Math.abs(roundUp) + 1923;
		refreshTotal = totalThumbs / 10;
		System.out.println("Refresh total: " + refreshTotal);
		
		// 10000 songs
		roundUp = (19454 % 10) - 10;
		System.out.println(roundUp);
		totalThumbs = Math.abs(roundUp) + 19454;
		refreshTotal = totalThumbs / 10;
		System.out.println("Refresh total: " + refreshTotal);
		
		// 100000 songs
		roundUp = (194784 % 10) - 10;
		System.out.println(roundUp);
		totalThumbs = Math.abs(roundUp) + 194784;
		refreshTotal = totalThumbs / 10;
		System.out.println("Refresh total: " + refreshTotal);
		
		roundUp(9999);
		getThumbsCount();
		
	}
	
	// getThumbsCount: Parses JSON response for "Thumbs Up" count and returns integer
	// Located in JSON as positiveFeedbackCount
	public static int getThumbsCount() {
		int songTotal = 0;
		int totalThumbs = 0;
		String profileResponse = "";
		JSONObject obj = new JSONObject(profileResponse);
		songTotal = totalThumbs = Integer.parseInt(obj.get("positiveFeedbackCount").toString());
		System.out.println("Song total: " + songTotal);
		System.out.println("Thumbs: " + totalThumbs);
		return totalThumbs;
	}
	
	
	// roundUp: Rounds totalThumbs up to the nearest tens place
	public static int roundUp(int totalThumbs) {
		int refreshTotal = 0;
		if(totalThumbs > 0) {
			int roundUp = 0;
			roundUp = (totalThumbs % 10) - 10;
			totalThumbs = Math.abs(roundUp) + totalThumbs;
			refreshTotal = totalThumbs / 10;
		}
		System.out.println(refreshTotal);
		return refreshTotal;
	}
	
//	String x = "{\"listenerId\":\"82194757\",\"webname\":\"tarius12\",\"private\":true,\"followState\":\"cannotFollow\",\"fullName\":\"TG\",\"positiveFeedbackCount\":1926,\"stationCount\":13,\"bookmarkCount\":119,\"followingCount\":0,\"followersCount\":0,\"bio\":\"\"}";
//	JSONObject obj = new JSONObject(x);
//	int totalThumbs = Integer.parseInt(obj.get("positiveFeedbackCount").toString());
//	// Round the number up 
//	System.out.println((19236 % 10));
//	int roundUp = (19236 % 10) - 10;
//	System.out.println(roundUp);
//	totalThumbs = Math.abs(roundUp) + totalThumbs;
//	System.out.println(totalThumbs);
	
	

}
