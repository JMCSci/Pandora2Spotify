package pandora2spotify;

import java.io.File;
import java.util.Scanner;

public class Secrets {
	private String clientID;
	private String clientSecret;
	private String pandoraUsername;
	private String pandoraPassword;
	
	Secrets() throws Exception {
		getClientInfo();
//		getPandoraInfo();
	}
	
	void getClientInfo() throws Exception {
		Scanner sc = new Scanner(System.in);
		String fileLocation = "";
		System.out.print("Enter location of your client id and client secret: ");
		fileLocation = sc.nextLine();
		File file = new File(fileLocation);
		sc.close();
		if(file.exists()) {
			Scanner fileInput = new Scanner(file);
			clientID = fileInput.nextLine();
			clientSecret = fileInput.nextLine();
			fileInput.close();
		} else {
			System.out.println("File not found");
			System.exit(-1);
		}
	}
	
	void getPandoraInfo() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your Pandora username: ");
		pandoraUsername = sc.nextLine();
		System.out.print("Enter your Pandora password: ");
		pandoraPassword = sc.nextLine();
		sc.close();
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

}
