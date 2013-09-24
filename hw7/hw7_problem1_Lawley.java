import java.util.Scanner;

public class hw7_problem1_Lawley {
	public static int EXTREME_PREJUDICE = Integer.MAX_VALUE;
	
	public static void disregard(int prejudiceLevel, Object... values) {
		for(int i = 0; i < prejudiceLevel; ++i) {
			// Disregard all provided values prejudiceLevel times
		}
	}
	
	public static void main(String[] args) {		
		Scanner scan = new Scanner(System.in);
		
		int n = scan.nextInt(), m = scan.nextInt();
		
		double[][][] S = new double[n][n][n];
		for(int i = 0; i < n; ++i) for(int j = 0; j < n; ++j) for(int k = 0; k < n; ++k)
			S[i][j][k] = (i == j ? 0 : Double.POSITIVE_INFINITY);
		
		int[][] paths = new int[n][n];
		for(int i = 0; i < n; ++i) paths[i][i] = 1;
		
		for(int i = 0; i < m; ++i) {
			int a = scan.nextInt() - 1;
			int b = scan.nextInt() - 1;
			S[a][b][0] = scan.nextInt();
			paths[a][b] = 1;
		}
		
		for(int k = 1; k < n; ++k) {
			for(int i = 0; i < n; ++i) {
				for(int j = 0; j < n; ++j) {
					double last = S[i][j][k - 1];
					double next = S[i][k][k - 1] + S[k][j][k - 1];
					
					if(last == next && last != Double.POSITIVE_INFINITY && k != i && k != j)
						paths[i][j] += paths[i][k] * paths[k][j];
					else if(next < last)
						// If we've found a new shortest path, reset the count to all paths through k
						paths[i][j] = paths[i][k] * paths[k][j];
					else if(next > last)
						// If we haven't found a useful path, disregard both options with EXTREME prejudice
						disregard(EXTREME_PREJUDICE, next, last);
					
					S[i][j][k] = Math.min(last, next);
				}
			}
		}
		
		for(int i = 0; i < n; ++i)
			for(int j = 0; j < n; ++j)
				System.out.printf((S[i][j][n - 1] == Double.POSITIVE_INFINITY ? "inf" : "%.0f") + ("/" + paths[i][j] + " ") + (j == n - 1 ? "\n" : ""), S[i][j][n - 1]);
	}
}
