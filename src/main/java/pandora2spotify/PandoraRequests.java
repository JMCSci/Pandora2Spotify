/* Uses Maven dependencies 
 * Selenium and BrowserMob to get POST request during scroll down
 * When complete send to TRObjects class to parse JSON and get tracks and artist names
 * Then Spotify Web API request searches on each of the tracks and add them to your new playlist
 */

package pandora2spotify;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.util.ArrayDeque;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.JavascriptExecutor;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;


public class PandoraRequests {
	List<String> postResponse = new LinkedList<String>();
	List<String> tr = new LinkedList<String>();
	List<String> songs = new LinkedList<String>();
	List<String> notAdded = new LinkedList<String>();
	LinkedList<String> ids = new LinkedList<String>();
	LinkedList<String> formattedSongs = new LinkedList<String>();
	Secrets pandoraSecrets;
	int songTotal = 0;
	
	PandoraRequests(Secrets secrets) throws Exception {
		pandoraSecrets = secrets;
	}
	
	void retrieveThumbsUp() throws Exception {
		/* Get HAR files from browser since Pandora API doesn't 
		 * provide a function for retrieving a user's "Thumbs" list
		 */
		BrowserMobProxy proxy = new BrowserMobProxyServer();		   // Proxy server used to get HAR files using Selenium 
		proxy.setTrustAllServers(true);
		proxy.start();
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		
		String hostIp = Inet4Address.getLocalHost().getHostAddress();
		seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());		// Set proxy port for Selenium
		seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());		// Set proxy port for Selenium 
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		 
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF); // suppress Selenium logs
		System.setProperty("webdriver.gecko.driver", "/Users/jasonmoreau/Desktop/WebDriver/bin/geckodriver");
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/Users/jasonmoreau/Desktop/MARIONETTE");
		FirefoxBinary firefoxbinary = new FirefoxBinary();
		firefoxbinary.addCommandLineOptions("--headless");	// Browser headless mode
		FirefoxOptions options = new FirefoxOptions();
		options.merge(capabilities);
		options.setBinary(firefoxbinary);
		WebDriver driver = new FirefoxDriver(options);
		
		/* Go to the login in page */
		driver.get("https://www.pandora.com/account/sign-in");
		Thread.sleep(5000);
		// DONT FORGET TO REMOVE USERNAME AND PASSWORD -- pass in with a File and Scanner 
		driver.findElement(By.name("username")).sendKeys(pandoraSecrets.getPandoraUsername());	// Enters username
		driver.findElement(By.name("password")).sendKeys(pandoraSecrets.getPandoraPassword());	// Enters password
		driver.findElement(By.className("FormButtonSubmit")).click();							// Clicks log-in
		Thread.sleep(5000);																		// Waits for page to load
		driver.get("https://www.pandora.com/profile/tarius12");	// Goes to profile page	
		/* GET HAR FILES FROM PROFILE PAGE */
		proxy.setHarCaptureTypes(CaptureType.RESPONSE_CONTENT);
		proxy.newHar("Pandora Get Profile");
		Thread.sleep(2500);
		List<HarEntry> getProfile = proxy.getHar().getLog().getEntries();

		/* GET RETRIEVE JSON RESPONSE CONTAINING THUMBS UP INFO */
		String profileResponse = "";
		int refreshTotal = 0;
		
		for(HarEntry entry: getProfile) {
			 if(entry.getRequest().getUrl().matches("https://www.pandora.com/api/v1/listener/getProfile")){
				   profileResponse = entry.getResponse().getContent().getText().toString();
				   // Send pandoraUser to a method to extract the positiveFeedBack count
				   // Send to another method to round up
				   // Round up and insert it into the loop counter
				   // REMEMBER THE TR objects contain 10 songs
				   // So 1926 songs would round up to 1930
				   // Divide it by 10 and thats how many times you have to refresh - 193 times
				   break;
			  }
		}
		
		refreshTotal = getThumbsCount(profileResponse);		// Retrieves and calculates refresh/loop total
		System.out.println("\nTotal Pandora \"Thumbs Up\" tracks: " + songTotal);
		
		
		/*
		 * 
		 * FOR TESTING ONLY!!!
		 * 
		 * 
		 */
		
		
		
		/* Scroll down Pandora page and get HAR files */
		driver.get("https://www.pandora.com/profile/thumbs/tarius12");							// Goes to Thumbs Up page
		Thread.sleep(2500);
		JavascriptExecutor js =(JavascriptExecutor) driver;
		driver.manage().window().maximize();
		proxy.setHarCaptureTypes(CaptureType.RESPONSE_CONTENT);
		proxy.newHar("Pandora Thumbs Up");
		
		if(refreshTotal > 0) {
			for(int i = 0; i < refreshTotal; i++) {	
				// a loop of 5 had 37 TR's (on average each TR has 10 tracks)
				System.out.print("Page refresh: " + (i + 1) + " of " + refreshTotal + "\r");
				js.executeScript("window.scrollBy(0,1324)");					// scroll down
				Thread.sleep(700);	// got 1000 song @ 800
//				proxy.newPage(); 						// next page - HAR
			}
		} else {
			System.out.println("Your Pandora profile has no thumbed up songs.");
			System.out.println("The program will now exit");
			proxy.stop();
			driver.close();
			System.exit(-1);
		}
		
		System.out.println("Finishing up Pandora song retrieval");
		Thread.sleep(30000);
	
		List<HarEntry> entries = proxy.getHar().getLog().getEntries();
		/* GET RETRIEVE JSON RESPONSE CONTAINING TR INFO */
		for (HarEntry entry : entries) {
			/* Only add the POST requests */
			   if(entry.getRequest().getUrl().matches("https://www.pandora.com/api/v4/catalog/annotateObjectsSimple")){
				   // Response will have all of the TR's
				   // Insert each POST response into a JSONObject
				   // TR represents each JSON array object
				   // A JSON array object is a track and its metadata
				   postResponse.add(entry.getResponse().getContent().getText().toString());
			   } 
			}
		proxy.stop();
		driver.close();
	}
	
	
	/* getThumbsCount: Parses JSON response for "Thumbs Up" count and returns integer
	 * Located in JSON as positiveFeedbackCount */
	int getThumbsCount(String profileResponse) {
		JSONObject obj = new JSONObject(profileResponse);
		int totalThumbs = 0;
		int refreshTotal = 0;
		songTotal = totalThumbs = Integer.parseInt(obj.get("positiveFeedbackCount").toString());
		refreshTotal = roundUp(totalThumbs, refreshTotal);
		return refreshTotal;
	}
	
	// roundUp: Rounds totalThumbs up to the nearest tens place
	int roundUp(int totalThumbs, int refreshTotal) {
		if(totalThumbs > 0) {
			int roundUp = 0;
			roundUp = (totalThumbs % 10) - 10;
			totalThumbs = Math.abs(roundUp) + totalThumbs;
			refreshTotal = totalThumbs / 10;
		}
		return refreshTotal;
	}
	
	/* Gets the TR objects
	 * Each TR object represents a track 
	 * TR's are used when looking in JSON response to retrieve track info 
	 * Track info includes: track and artist names */
	void getTR() {
		String temp;
		int postResponseSize = postResponse.size();
		int i = 0;

		while(i < postResponseSize) {
			String json = postResponse.get(i);
			temp = json.substring(json.indexOf("TR:"), json.indexOf("\":"));
			tr.add(temp);
			// Slice up to next TR in JSON
			json = postResponse.get(i);
			json = json.substring(json.indexOf("\"artistName\":"));
			String [] tokens1 = json.split("},\"TR:");
			int size = tokens1.length;				// length of tokens1
					
			String temp1 = "";
			for(int j = 1; j < size; j++) {
				String [] tokens2 = tokens1[j].split("\"");
				temp1 = "TR:" + tokens2[0];
				tr.add(temp1);
			}
			i++;
		}
	}
	
	/* Convert responses in list to JSON object 
	 * Parse JSON responses for song and artist names, URLify them, and add them to List 
	 * For use in GET request when searching for tracks and retrieving their ids 
	 */
	 void jsonToList() throws Exception {
		File file = new File("PandoraSongs");
		FileOutputStream out = new FileOutputStream(file);	// Output track and artist names to a file
		int size = postResponse.size();
		int JSONlength = 0;
		int trCount = 0;
		// GET POST REQUEST
		for(int i = 0; i < size; i++) {
			StringBuilder har = new StringBuilder(postResponse.get(i));
			// CREATE JSON object
			JSONObject obj = new JSONObject(har.toString());
			// GET THE LENGTH OF THE JSON RESPONSE
			JSONlength = obj.length();
			for(int j = 0; j < JSONlength; j++) {
				// PARSE JSON USING TR's
				JSONObject tracks = new JSONObject(obj.get(tr.get(trCount)).toString());	// Insert nested JSON into another object
				System.out.println(tracks.get("name") + " -- " + tracks.get("artistName"));
				String temp = tracks.get("name") + " - " + tracks.get("artistName") + "\n";
				out.write(temp.getBytes());
				songs.add("q=track:" + tracks.get("name") + "%20artist:" + tracks.get("artistName") + "&type=track");
				trCount += 1;
			}
			
		}
		out.close();
	}
	 
	 /* formatForQuery: Format the URLifed strings 
	 *  Removes and replaces whitespace, parenthesis, and apostrophes from string
	 *  For use in Spotify GET request track search
	 */
	 void formatForQuery() {
			Iterator<String> iterator = songs.iterator();
			while(iterator.hasNext()) {
				String x = iterator.next().replaceAll("\\([^()]*\\)", "");
				x = x.replaceAll("[<>#;$\\\\/\"]", "");
				x = x.replaceAll(Character.toString(8217), "");
				x = x.replaceAll(" ", "%20");
				formattedSongs.add(x);
			}
			songs.clear();		// Clear the songs list after formatting
			System.out.println("Total songs: " + formattedSongs.size());
	}
	 
	 int songListSize() {
		 return formattedSongs.size();
	 }
	 
	 int idListSize() {
		 return ids.size();
	 }
	 
	 boolean idListIsEmpty() {
		 return ids.isEmpty();
	 }
	 
	 String getSongs() {
		 return formattedSongs.removeFirst();
		 
	 }
	 
	 String getIds() {
		 return ids.removeFirst();
	 }
	 
	
    /* Parse JSON response for track id to be used when adding to playlist 
     * Paging object contains id
	 * The offset-based paging object is a container for a set of objects. 
	 * It contains a key called items (whose value is an array of the requested objects) 
	 * Along with other keys like previous, next and limit that can be useful in future calls 
	 */
	void parseID(String jsonString) {
		JSONObject obj = new JSONObject(jsonString);						// Put paging object into JSONObject
		JSONObject a = new JSONObject(obj.get("tracks").toString());		// Get track object
		JSONArray arr = new JSONArray(a.getJSONArray("items").toString());	// Get JSON array objects from items
		if(!arr.isEmpty()) {
			JSONObject b = new JSONObject(arr.get(0).toString());				// Convert objects into JSONObject
			if(b.has("id")) {
				ids.add(b.get("id").toString());								// Get the id value	
			} else {
				System.out.println("Track not added");
				System.out.println(jsonString);
				System.out.println("Track: " + b.get("name") + " Artists: " + b.get("artist"));
			}
		}
		
	}
	 	 
}
