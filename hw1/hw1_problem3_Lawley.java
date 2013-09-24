import java.util.Scanner;

public class hw1_problem3_Lawley {
	// There will never be more than two digits, so I'm considering this constant
	// Complexity: O(1)
	public static int nthDigit(int num, int n, int base) {
		return (num / (int)Math.pow(base, n)) % base;
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		// Pull ints in from stdin
		int[] toSort = new int[scan.nextInt()];
		
		int base = toSort.length;
		
		for(int i = 0; i < toSort.length; ++i) {
			toSort[i] = scan.nextInt();
		}
		
		// We need B buckets, where B is the base of the numbers to sort
		// However, we interpret each of these numbers from 0 to n^2 - 1 as being in base n
		// The set of numbers to sort is also of size n
		// Therefore, I use the size of that set as the number of buckets
		LaneList[] laneBuckets = new LaneList[base];
		
		for(int i = 0; i < laneBuckets.length; ++i) {
			laneBuckets[i] = new LaneList();
		}
		
		// Loop twice; in base n, with max value n^2 - 1, we only need two digits
		// Complexity: O(1)
		for(int k = 0; k < 2; ++k) {
			// Complexity: O(n)
			for(int i = 0; i < toSort.length; ++i) {
				int key = toSort[i];
				int bucket = nthDigit(key, k, base);
				
				laneBuckets[bucket].addToEnd(key);
			}
			
			// The complexity of this looks suspect, however, it will only run as many times as there are items!
			// This is because it only performs an operation per item in each bucket, and there can't be more items than n.
			// Complexity: O(n)
			int j = 0;
			for(int i = 0; i < laneBuckets.length; ++i) {
				while(!laneBuckets[i].isEmpty()) {
					toSort[j++] = laneBuckets[i].popOffBeginning();
				}
			}
		}
		
		// Complexity: O(n)
		for(int i = 0; i < toSort.length; ++i) {
			System.out.print(toSort[i] + (i < toSort.length - 1 ? " " : ""));
		}
		
		System.out.print("\n");
	}
}

class LaneList {
	public LaneNode head = new LaneNode();
	public LaneNode tail = head;
	
	int size = 0;
	
	// Time complexity: O(1)
	public void addToEnd(int val) {
		++size;
		
		if(tail.isSentinel) {
			tail.isSentinel = false;
			tail.val = val;
		} else {
			tail.next = new LaneNode(val);
			tail = tail.next;
		}
	}
	
	// Time complexity: O(1)
	public int popOffBeginning() {
		--size;
		
		int node = head.val;
		
		head = head.next;
		
		// If we emptied the list, restore the original sentinel state
		if(size == 0) {
			head = new LaneNode();
			tail = head;
		}
		
		return node;
	}
	
	// Time complexity: O(1)
	public boolean isEmpty() {
		return size == 0;
	}
}

class LaneNode {
	public int val;
	public boolean isSentinel;
	public LaneNode next;
	
	public LaneNode() {
		isSentinel = true;
	}
	
	public LaneNode(int val) {
		isSentinel = false;
		this.val = val;
	}
}
