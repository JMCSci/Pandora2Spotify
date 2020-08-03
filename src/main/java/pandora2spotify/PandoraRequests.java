/* Uses Maven dependencies 
 * Selenium and BrowserMob to get POST request during scroll down
 * When complete send to TRObjects class to parse JSON and get tracks and artist names
 * Then Spotify Web API request searches on each of the tracks and add them to your new playlist
 */

package pandora2spotify;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.util.ArrayDeque;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
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
	ArrayDeque<String> ids = new ArrayDeque<String>();
	ArrayDeque<String> formattedSongs = new ArrayDeque<String>();
	Secrets pandoraSecrets;
	
	PandoraRequests(Secrets secrets) throws Exception {
		pandoraSecrets = secrets;
		retrieveThumbsUp();		// Uses browser to get thumbs up tracks from Pandora by refreshing page and retrieving POST responses
		getTR();				// Parse POST responses for TR objects containing tracks
		jsonToList();			// Uses TR id to get tracks located in JSON responses
		formatForQuery();		// Format the strings from songs list for use in Spotify track search		
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
		 
		System.setProperty("webdriver.gecko.driver", "/Users/jasonmoreau/Desktop/WebDriver/bin/geckodriver");
		FirefoxOptions options = new FirefoxOptions();
		options.merge(capabilities);
		WebDriver driver = new FirefoxDriver(options);
		
		/* Go to the login in page */
		driver.get("https://www.pandora.com/account/sign-in");
		Thread.sleep(5000);
		// DONT FORGET TO REMOVE USERNAME AND PASSWORD -- pass in with a File and Scanner 
		driver.findElement(By.name("username")).sendKeys(pandoraSecrets.getPandoraUsername());	// Enters username
		driver.findElement(By.name("password")).sendKeys(pandoraSecrets.getPandoraPassword());	// Enters password
		driver.findElement(By.className("FormButtonSubmit")).click();							// Clicks log-in
		Thread.sleep(5000);																		// Waits for page to load
		driver.get("https://www.pandora.com/profile/thumbs/tarius12");							// Goes to Thumbs Up page
		/* Scrolls down */
		/*
		 * THIS IS IN A LOOP 
		 * YOU ALSO MUST KNOW WHEN YOU REACH THE BOTTOM OF THE PAGE 
		 */
		Thread.sleep(2500);
		JavascriptExecutor js =(JavascriptExecutor) driver;
		driver.manage().window().maximize();
		proxy.setHarCaptureTypes(CaptureType.RESPONSE_CONTENT);
		proxy.newHar("Pandora Thumbs Up");
		int size = 1;
		for(int i = 0; i < size; i++) {	// a loop of 5 had 37 TR's (on average each TR has 10 tracks)
			System.out.print("Page refresh: " + (i + 1) + " of " + size + "\r");
			js.executeScript("window.scrollBy(0,1324)");					// scroll down
			Thread.sleep(1500);
			proxy.newPage(); 						// next page - HAR
		}
			
		List<HarEntry> entries = proxy.getHar().getLog().getEntries();
		System.out.println("Number of tracks: " + entries.size());
		
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
	
	/* Gets the TR objects
	 * Each TR object represents a track 
	 * TR's are used when looking in JSON response to retrieve track info 
	 * Track info includes: track and artist names
	 */
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
		System.out.println("Total: " + tr.size());
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
		 System.out.println(songs.size());
			Iterator<String> iterator = songs.iterator();
			while(iterator.hasNext()) {
				String x = iterator.next().replaceAll("\\([^()]*\\)", "");
				x = x.replaceAll("'", "");
				x = x.replaceAll(" ", "%20");
				formattedSongs.add(x);
			}
			songs.clear();		// Clear the songs list after formatting
			System.out.println(formattedSongs.size());
	}
	 
	 int songListSize() {
		 return formattedSongs.size();
	 }
	 
	 int idListSize() {
		 return ids.size();
	 }
	 
	 String getSongs() {
		 return formattedSongs.removeFirst();
		 
	 }
	 
	 String getIds() {
		 return ids.removeFirst();
	 }
	 
	 /* Parse JSON response for track id to be used when adding to playlist */
	 void parseID(String jsonString) {
			jsonString = jsonString.replaceAll("\\s", "");		// Remove whitespace
			String [] tokens1 = jsonString.split("\"uri\":\"spotify:track:");
			String [] tokens2 = tokens1[1].split("\"");
			ids.add(tokens2[0]);
	}
	 	 
}
