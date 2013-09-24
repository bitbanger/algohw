import java.util.Scanner;

public class hw5_problem1_Lawley {
	public static double euclideanDistance(P1Vertex a, P1Vertex b) {
		double deltaX = a.a - b.a;
		double deltaY = a.b - b.b;
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}
	
	public static double minTriangulation(P1Vertex[] vertices) {
		int n = vertices.length;
		
		// S[i][j] = the minimum-cost triangulation between vertices i and j
		// A linear order of vertices around the polygon is provided by the sorted guarantee
		double[][] S = new double[n][n];
		
		// Initialize the array with NaN
		// I don't know why
		for(int i = 0; i < n; ++i)
			for(int j = 0; j < n; ++j)
				S[i][j] = Double.NaN;
		
		// All adjacent vertices cannot be triangulated, so they are 0.0
		for(int i = 0; i < n - 1; ++i)
			S[i][i + 1] = 0.0;
		
		// Offset loop; start at the third diagonal
		for(int d = 2; d < n; ++d) {
			// Row loop
			for(int i = 0; i < n - d; ++i) {
				// Here's our column index, which differs from the row index by the offset
				int j = i + d;
				
				double minVal = Double.POSITIVE_INFINITY;
				
				for(int k = i; k < j; ++k) {
					double candidate = 0.0;
					
					// Special case: don't count the distance between the first and last vertices
					if(i == 0 && j == n - 1) candidate = S[i][k] + S[k][j];
					
					// Our best triangulation is the triangulated polygons between i and k and k and j, plus the triangle we form between i, k, and j
					// However, this problem only wants the length of the ADDED edges, so we only add the line between i and j when we draw the triangle
					// Even though this doesn't vary with the k, the k determines *which prior configuration* of lines we're adding to (I think)
					else candidate = S[i][k] + S[k][j] + euclideanDistance(vertices[i], vertices[j]);
					
					if(candidate < minVal) minVal = candidate;
				}
				
				S[i][j] = minVal;
			}
		}
		
		// S[0][n - 1] = the minimum-cost triangulation between vertices 0 and n-1 = our answer
		return S[0][n - 1];
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		P1Vertex[] vertices = new P1Vertex[scan.nextInt()];
		
		for(int i = 0; i < vertices.length; ++i)
			vertices[i] = new P1Vertex(scan.nextDouble(), scan.nextDouble());
		
		System.out.printf("%.4f\n", minTriangulation(vertices));
	}
}

class P1Vertex {
	double a, b;
	
	public P1Vertex(double a, double b) {
		this.a = a;
		this.b = b;
	}
}
