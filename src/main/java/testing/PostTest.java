/* Uses Maven dependencies 
 * Selenium and BrowserMob to get POST request during scroll down
 * When complete send to TRObjects class to parse JSON and get tracks and artist names
 * Then Spotify Web API request searches on each of the tracks and add them to your new playlist
 */

package testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.net.Inet4Address;

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


public class PostTest {
	static String profileDetails = ""; 
	public static void main(String[] args) throws Exception {
		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.setTrustAllServers(true);
		proxy.start();
		
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		
		String hostIp = Inet4Address.getLocalHost().getHostAddress();
		seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
		seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
		
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
		driver.findElement(By.name("username")).sendKeys("tarius12@optonline.net");			// enter username
		driver.findElement(By.name("password")).sendKeys("7GyasJPmUxmX)P9AxxBgDmaeVyY");	// enter password
		driver.findElement(By.className("FormButtonSubmit")).click();						// click log-in
		Thread.sleep(5000);																	// wait for page to load
		driver.get("https://www.pandora.com/profile/thumbs/tarius12");						// go to thumbs up
		/* Scroll down */
		/*
		 * THIS WILL HAVE TO BE IN A LOOP 
		 * YOU ALSO MUST KNOW WHEN YOU REACH THE BOTTOM OF THE PAGE 
		 */
		
		Thread.sleep(2500);
		JavascriptExecutor js =(JavascriptExecutor) driver;
		driver.manage().window().maximize();
		proxy.setHarCaptureTypes(CaptureType.RESPONSE_CONTENT);
		proxy.newHar("Pandora Thumbs Up");
		for(int i = 0; i < 2; i++) {	// a loop of 5 had 37 TR's (on average each TR has 10 tracks)
			js.executeScript("window.scrollBy(0,1324)");					// scroll down
			Thread.sleep(1500);
			proxy.newPage(); 						// next page  Har -- should be in loop
		}
		
		List<String> postRequest = new LinkedList<String>();
		List<String> tr = new LinkedList<String>();
		Map<String,String> songs = new HashMap<String, String>();
		
		List<HarEntry> entries = proxy.getHar().getLog().getEntries();
		System.out.println(entries.size());
		
		for (HarEntry entry : entries) {
			/* Get only the POST requests */
			if(entry.getRequest().getUrl().matches("https://www.pandora.com/api/v4/catalog/annotateObjectsSimple")){
				// Response will have all of the TR's
				// Insert each POST response into a JSONObject
				// TR represents each JSON array object
				// A JSON array object is a track and its metadata
				postRequest.add(entry.getResponse().getContent().getText().toString());
			 }
		}
		
		System.out.println(profileDetails);
		proxy.stop();
		driver.close();
		
		getTR(postRequest, tr);
		getSongs(postRequest, tr, songs);
		
	}
	
	public static void getTR(List<String> postRequest, List<String> tr) {
		String temp;
		int postRequestSize = postRequest.size();
		int i = 0;

		while(i < postRequestSize) {
			String json = postRequest.get(i);
			temp = json.substring(json.indexOf("TR:"), json.indexOf("\":"));
			tr.add(temp);
			// Slice up to next TR in JSON
			json = postRequest.get(i);
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
	
	public static void getSongs(List<String> postRequest, List<String> tr, Map<String, String> songs) {
		int size = postRequest.size();
		int JSONlength = 0;
		int trCount = 0;
		// GET POST REQUEST
		for(int i = 0; i < size; i++) {
			StringBuilder har = new StringBuilder(postRequest.get(i));
			// CREATE JSON object
			JSONObject obj = new JSONObject(har.toString());
			// GET THE LENGTH OF THE JSON RESPONSE
			JSONlength = obj.length();
			for(int j = 0; j < JSONlength; j++) {
				// PARSE JSON USING TR's
				JSONObject tracks = new JSONObject(obj.get(tr.get(trCount)).toString());	// Insert nested JSON into another object
				System.out.println(tracks.get("name") + " -- " + tracks.get("artistName"));
				songs.put(tracks.get("name").toString(), tracks.get("artistName").toString());
				trCount += 1;
			}
			
		}

	}

}
