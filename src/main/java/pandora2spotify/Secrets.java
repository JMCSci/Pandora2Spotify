package pandora2spotify;

import java.io.Console;
import java.io.File;
import java.util.Scanner;

public class Secrets {
	private String clientID;
	private String clientSecret;
	private String pandoraUsername = "";
	private String pandoraPassword = "";
	
	Secrets() throws Exception {
		Scanner sc = new Scanner(System.in);
		getClientInfo(sc);
		getPandoraInfo(sc);
		sc.close();
	}
	
	void getClientInfo(Scanner sc) throws Exception {
		String fileLocation = "";
		System.out.print("Enter location of your client id and client secret: ");
		fileLocation = sc.nextLine();
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
	}
	
	void getPandoraInfo(Scanner sc) throws Exception {
		System.out.print("Enter your Pandora username: ");
		pandoraUsername = sc.nextLine();
		System.out.print("Enter your Pandora password: ");
		Console console = System.console();				
		char [] pwd = console.readPassword();				// Hides user input on the command line
		pandoraPassword = new String(pwd);
	}
	
	String getClientID() {
		return clientID;
	}
	
	String getClientSecret() {
		return clientSecret;
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
