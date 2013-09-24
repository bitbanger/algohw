import java.util.Scanner;

public class hw4_problem3_Lawley {
	public static void path(int[] a, int W) {
		int n = a.length;
		
		boolean[][] dpArray = new boolean[n][W + 1];
		
		int trueInLastRow = -1;
		
		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < W + 1; ++j) {
				// Bootstrap the first row
				if(i == 0) {
					dpArray[i][j] = (j == a[0]);
				} else {
					int checkIdx1 = Math.abs(j + a[i]);
					int checkIdx2 = Math.abs(j - a[i]);
					
					if(checkIdx1 >= 0 && checkIdx1 < W + 1) {
						if(dpArray[i - 1][checkIdx1]) dpArray[i][j] = true;
					}
					
					if(checkIdx2 >= 0 && checkIdx2 < W + 1) {
						if(dpArray[i - 1][checkIdx2]) dpArray[i][j] = true;
					}
				}
				
				if(i == n - 1 && dpArray[i][j]) trueInLastRow = j;
			}
		}
		
		if(trueInLastRow != -1) {
			int curr = trueInLastRow;
			int[] path = new int[n];
			
			for(int i = n - 1; i >= 0; --i) {
				int add = curr + a[i];
				int sub = Math.abs(curr - a[i]);
				
				path[i] = curr;
				
				if(i > 0) {
					if((add >= 0 && add < W + 1) && dpArray[i - 1][add]) {
						curr = add;
					} else if((sub >= 0 && sub < W + 1) && dpArray[i - 1][sub]) {
						curr = sub;
					}
				}
			}
			
			System.out.println("YES");
			
			int currDist = 0;
			
			for(int i = 0; i < path.length; ++i) {
				int add = currDist + a[i];
				int sub = currDist - a[i];
				
				if(Math.abs(currDist + a[i]) == path[i]) {
					currDist += a[i];
					System.out.print("L");
				}
				else if(Math.abs(sub) == path[i]) {
					currDist -= a[i];
					System.out.print("R");
				}
			}
			
			System.out.print("\n");
		} else {
			System.out.println("NO");
		}
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int n = scan.nextInt();
		int W = scan.nextInt();
		int[] a = new int[n];
		for(int i = 0; i < a.length; ++i) a[i] = scan.nextInt();
		
		path(a, W);
	}
}