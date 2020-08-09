package testing;

import java.util.LinkedList;

public class TestList {
	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for(int i = 0; i < 1926; i++) {
			list.add(i);
		}
		
		int size = list.size();
		
		System.out.println(size);
		
		
		while(!list.isEmpty()) {
			size = list.size();
			System.out.println("SIZE: " + size / 100);
			if(size / 100 > 0) {
				// Pop 100 tracks from the queue
				for(int i = 0; i < 100; i++) {
					System.out.println(i);
					list.removeFirst();	// Add 100 tracks to JSON array
				}
			} else {
				// Pop everything off of the queue
				for(int i = 0; i < size; i++) {
					System.out.println(i);
					list.removeFirst();			// Add remaining tracks to JSON array
				}
			}
		}
		System.out.println("LIST SIZE:" + list.size());
		
		
	}

}
