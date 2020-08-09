package testing;

import java.util.LinkedList;

public class TestList {
	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for(int i = 0; i < 1545; i++) {
			list.add(i);
		}
		
		int size = list.size();
		
		if(size / 100 >= 0) {
			// Pop 100 tracks from the queue
			for(int i = 0; i < 100; i++) {
				list.removeFirst();		// Add 100 tracks to JSON array
			}
		} else {
			// Pop everything off of the queue
			for(int i = 0; i < size; i++) {
				list.removeFirst();			// Add remaining tracks to JSON array
			}
		}
		
	}

}
