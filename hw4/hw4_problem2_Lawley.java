import java.util.Scanner;

public class hw4_problem2_Lawley {
	// VERSION 1: Done in linear space and quadratic time.
	// I'm leaving this here just in case my mega-optimized version below doesn't handle every case.
	public static int countAlternatingSubsequences(int[] a) {
		// s[i] = number of alternating subsequences ending with a[i]
		// s[i + 1] = s[i] if a[i] and a[i + 1] are different parity
		int[] s = new int[a.length];
		
		int total = 0;
		
		// For each element E: 
		// If the parity of a previous element F is different, append E.
		// Now all of F's subsequences can end with E to become new sequences; we should re-count them for E, too.
		for(int i = 0; i < a.length; ++i) {
			// Count the element itself, initially.
			s[i] = 1;
			// Then count all the elements before us, and re-count its values if we can be appended to create another set of subsequences.
			for(int j = 0; j < i; ++j)
				if(a[i]%2 != a[j]%2) s[i] += s[j];
			total += s[i];
		}
		
		return total;
	}
	
	// VERSION 2: Done in constant space and linear time. (And five lines of nice-looking code!)
	// This is the same solution as the one above, except for the running total for each parity I keep.
	// Since looping through the dynamic programming array involved only two cases (re-count if different parity, else nothing), I decided not to waste time.
	// All counts kept for the same parity should be re-counted when a number of a different parity is located.
	// Keeping information on where each individual number of that parity is wastes time and space.
	public static int countAlternatingSubsequencesBetter(int[] a) {
		int totalNumberOfAlternatingSubsequencesWhoseLastElementIsEven = 0, totalNumberOfAlternatingSubsequencesWhoseLastElementIsOdd = 0;
		
		for(int i = 0; i < a.length; ++i)
			if(a[i]%2 == 0) totalNumberOfAlternatingSubsequencesWhoseLastElementIsEven += (1 + totalNumberOfAlternatingSubsequencesWhoseLastElementIsOdd);
			else            totalNumberOfAlternatingSubsequencesWhoseLastElementIsOdd  += (1 + totalNumberOfAlternatingSubsequencesWhoseLastElementIsEven);
		
		return totalNumberOfAlternatingSubsequencesWhoseLastElementIsEven + totalNumberOfAlternatingSubsequencesWhoseLastElementIsOdd;
	}
	
	public static void main(String[] args) {
		// Scanner? I hardly know 'er!
		Scanner scan = new Scanner(System.in);
		
		int a[] = new int[scan.nextInt()];
		
		for(int i = 0; i < a.length; ++i) a[i] = scan.nextInt();
		
		System.out.println(countAlternatingSubsequencesBetter(a));
	}
}