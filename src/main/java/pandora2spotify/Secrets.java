package pandora2spotify;

import java.io.Console;
import java.io.File;
import java.util.Scanner;

public class Secrets {
	private String clientID;
	private String clientSecret;
	private String spotifyUsername = "";
	private String spotifyPassword = "";
	private String pandoraUsername = "";
	private String pandoraPassword = "";
	
	Secrets() throws Exception {

	}
	
	void userSecrets() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter location of your client id and client secret: ");
		String fileLocation = "";
		fileLocation = sc.nextLine();
		getClientSecrets(sc, fileLocation);
	}
	
	void getClientSecrets(Scanner sc, String fileLocation) throws Exception {
		File file = new File(fileLocation);
		if(file.exists()) {
			Scanner fileInput = new Scanner(file);
			clientID = fileInput.nextLine();
			clientSecret = fileInput.nextLine();
			fileInput.close();
		} else {
			System.out.println("File not found");
			sc.close();
			System.exit(-1);
		}
		getSpotifyInfo(sc);
	}
	
	void getSpotifyInfo(Scanner sc) throws Exception {
		System.out.print("Enter your Spotify username: ");
		spotifyUsername = sc.nextLine();
		System.out.print("Enter your Spotify password: ");
		Console console = System.console();				
		char [] pwd = console.readPassword();				// Hides user input on the command line
		spotifyPassword = new String(pwd);
		getPandoraInfo(sc);
	}
	
	void getPandoraInfo(Scanner sc) throws Exception {
		System.out.print("Enter your Pandora username: ");
		pandoraUsername = sc.nextLine();
		System.out.print("Enter your Pandora password: ");
		Console console = System.console();				
		char [] pwd = console.readPassword();				// Hides user input on the command line
		pandoraPassword = new String(pwd);
		sc.close();
	}
	
	String getClientID() {
		return clientID;
	}
	
	String getClientSecret() {
		return clientSecret;
	}
	
	String getSpotifyUsername() {
		return spotifyUsername;
	}
	
	String getSpotifyPassword() {
		return spotifyPassword;
	}
	
	String getPandoraUsername() {
		return pandoraUsername;
	}
	
	String getPandoraPassword() {
		return pandoraPassword;
	}
	
	Secrets getSecrets() {
		return this;
	}

}
