import java.util.Scanner;

public class hw4_problem1_Lawley {
	// Done in linear space and quadratic time.
	public static int alternatingSubseqDynamic(int[] a) {
		int[] s = new int[a.length];
		
		for(int j = 0; j < a.length; ++j) {
			s[j] = 1;
			
			for(int k = 0; k < j; ++k) {
				if(a[k]%2 != a[j]%2 && s[j] < s[k] + 1) {
					s[j] = s[k] + 1;
				}
			}
		}
		
		int best = s[0];
		
		for(int i = 0; i < s.length; ++i) {
			if(s[i] > best) best = s[i];
		}
		
		System.out.print("\n");
		
		return best;
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int a[] = new int[scan.nextInt()];
		
		for(int i = 0; i < a.length; ++i) a[i] = scan.nextInt();
		
		System.out.println(alternatingSubseqDynamic(a));
	}
}