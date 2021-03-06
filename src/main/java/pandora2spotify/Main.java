package pandora2spotify;

public class Main {
	public static void main(String[] args) throws Exception {
		/* GET USER SECRETS (USER NAMES AND PASSWORDS) */
		Secrets secrets = new Secrets();
		secrets.userSecrets();
		/* START PANDORA USER REQUESTS */
		PandoraRequests pandora = new PandoraRequests(secrets);
		System.out.println("\n*** Retrieving Pandora songs ***\n");
		pandora.retrieveThumbsUp();				// Uses browser to get thumbs up tracks from Pandora by refreshing page and retrieving POST responses
		pandora.getTR();						// Parse POST responses for TR objects containing tracks
		pandora.jsonToList();					// Uses TR id to get tracks located in JSON responses
		pandora.formatForQuery();				// Format the strings from songs list for use in Spotify track search
		/* REQUEST SPOTIFY AUTHORIZATION */
		System.out.println("\n*** Requesting Spotify access ***\n");
		Authorization authorization = new Authorization(secrets);
		authorization.createState();			// Create state - optional protection against attacks such as cross-site request forgery
		authorization.getRequest();				// Request authorization to access data
		authorization.authorization();			// Save authorization code and state to hash map - used to request access token
		authorization.requestAccessTokens();	// Obtains access and refresh tokens
		/* START SPOTIFY USER PROFILE REQUESTS */
		SpotifyAPI api = new SpotifyAPI(authorization, pandora, secrets);
		System.out.println("\n*** Setting up Spotify playlist ***\n");
		api.addCurrentUser();					// Retrieve current Spotify user data
		api.createPlaylist();					// Create a new Spotify playlist
		api.userPlaylists();					// Retrieve Spotify playlist ID
		/* SEARCH SPOTIFY FOR SONGS */
		System.out.println("\n*** Searching for tracks on Spotify ***\n");
		searchSpotify(pandora, api);
		/* ADD SONGS TO SPOTIFY PLAYLIST */
		System.out.println("\n*** Adding songs to Spotify playlist ***\n");
		addTracks(pandora, api);
		/* END OF PROGRAM */
		System.out.println("Your Pandora songs have been successfully migrated to Spotify");
	}
	
	// searchSpotify: Uses "Thumbs Up" songs in list to search Spotify for tracks 
	// Parses the JSON responses and saves the track id to a queue
	public static void searchSpotify(PandoraRequests pandora, SpotifyAPI api) throws Exception {
		int size = pandora.formattedSongs.size();						// Gets the length of the songs array
		int count = 0;
		ProgressBar progressBar = new ProgressBar();
		for(int i = 0; i < size; i++) {
			String jsonString = api.searchForItem(pandora.getSongs());	// Get JSON response
			pandora.parseID(jsonString);								// Parse response for track id and adds to id queue
			Thread.sleep(900);
			progressBar.displaySmall(count, size);
			count++;
		}
	}
	
	// addTrack: Creates JSON array and adds songs to Spotify playlist
	// Creates a JSONArray using at most 100 songs per request and adds tracks to playlist
	public static void addTracks(PandoraRequests pandora, SpotifyAPI api) throws Exception {
		/* While loop that checks if id queue is empty
		 * If statement that checks size of remaining queue
		 * If it is, then pop 100 items off the queue with a for loop
		 * Use it to create JSON array
		 * Array will be used in POST request to add songs */
		ProgressBar progressBar = new ProgressBar();
		int count = 0;
		int totalSongs = pandora.idListSize();
		System.out.println("\nTotal songs to transfer: " + totalSongs + "\n");
		while(!pandora.idListIsEmpty()) {
			if(pandora.idListSize() > 100) {
				count += 100;
			} else {
				count += pandora.idListSize();
			}
			String songs = api.createJSONArray();
			api.addToPlaylist(songs);
			Thread.sleep(2500);
			progressBar.displaySmall(count, totalSongs);
		}
	}

}
