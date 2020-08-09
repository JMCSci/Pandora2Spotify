package pandora2spotify;

public class ProgressBar {
	
	ProgressBar() {
		
	}
	
	// displaySmall: Displays a progress bar on the command line in smaller increments
	// Increments 10% based on size of count:data structure size ratio in while loop
	public void displaySmall(int count, int len) throws Exception {	
		StringBuilder s = new StringBuilder("[                                                  ]");
		if(count >= len * 0.00 && count <= len * 0.10) {
			for(int i = 1; i < 5; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 0% \r");
		} else if(count >= len * 0.10 && count <= len * 0.20) {
			for(int i = 1; i < 10; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 10% \r"); 
		} else if(count >= len * 0.20 && count <= len * 0.30) {
			for(int i = 1; i < 15; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 20% \r");
		} else if(count >= len * 0.30 && count <= len * 0.40) {
			for(int i = 1; i < 20; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 30% \r");
		} else if(count >= len * 0.40 && count <= len * 0.50) {
			for(int i = 1; i < 25; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 40% \r");
		} else if(count >= len * 0.50 && count <= len * 0.60) {
			for(int i = 1; i < 30; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 50% \r");
		} else if(count >= len * 0.60 && count <= len * 0.70) {
			for(int i = 1; i < 35; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 60% \r");
		} else if(count >= len * 0.70 && count <= len * 0.80) {
			for(int i = 1; i < 40; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 70% \r");
		} else if(count >= len * 0.80 && count <= len * 0.90) {
			for(int i = 1; i < 45; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 80% \r");
		} else if(count >= len * 0.90 && count < len * 1.00) {
			for(int i = 1; i < 50; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 90% \r");
		} else if(count == len){
			for(int i = 1; i < s.length() - 1; i++) {
				s = s.replace(i, i + 1, "\u2588");
			}
			System.out.print("Progress: " + s + " 100% \r"); 
		}
	}
	
	// displayLarge: Displays a progress bar on the command line in larger increments
	// Increments 25% based on size of count:data structure size ratio in while loop
	public void displayLarge(int count, int len) throws Exception {	
		StringBuilder s = new StringBuilder("[                                                  ]");
			if(count <= len * 0.25) {
				for(int i = 1; i < 12; i++) {
					s = s.replace(i, i + 1, "\u2588");
				}
				System.out.print("Progress: " + s + " 25% \r");
			} else if(count >= len * 0.25 && count <= len * 0.50) {
				for(int i = 1; i < 25; i++) {
					s = s.replace(i, i + 1, "\u2588");
				}
				System.out.print("Progress: " + s + " 50% \r");
			} else if(count >= len * 0.50 && count <= len * 0.75) {
				for(int i = 1; i < 37; i++) {
					s = s.replace(i, i + 1, "\u2588");
				}
				System.out.print("Progress: " + s + " 75% \r");
			} else {
				for(int i = 1; i < s.length() - 1; i++) {
					s = s.replace(i, i + 1, "\u2588");
				}
				System.out.print("Progress: " + s + " 100% \r");
		}
	}

}
