import java.util.Scanner;

public class hw7_problem2_Lawley {
	public static void main(String[] args) {
		boolean speedup = args.length > 0 && args[0].equals("--good-speedup");
		
		Scanner scan = new Scanner(System.in);
		
		final int n = scan.nextInt(), m = scan.nextInt(), s = scan.nextInt() - 1;
		
		final double[] dist = new double[n];
		for(int i = 0; i < n; ++i) dist[i] = Double.POSITIVE_INFINITY;
		dist[s] = 0;
		
		final int[] edges = new int[m * 3];
		for(int i = 0; i < m * 3; i += 3) {
			edges[i] = scan.nextInt() - 1;
			edges[i + 1] = scan.nextInt() - 1;
			edges[i + 2] = scan.nextInt();
		}
		
		long seqTime = -System.currentTimeMillis();
		
		// Do our initial n-1 iterations
		for(int i = 0; i < n - 1; ++i) {
			for(int j = 0; j < m * 3; j += 3) {
				int u = edges[j];
				int v = edges[j + 1];
				int w = edges[j + 2];
				
				if(dist[v] > dist[u] + w)
					dist[v] = dist[u] + w;
			}
		}
		
		seqTime += System.currentTimeMillis();
		
		long parTime = 0L;
		
		// What's this?
		if(speedup) {
			parTime = -System.currentTimeMillis();
			
			// Create two threads to relax the edges in parallel
			for(int i = 0; i < n - 1; ++i) {
				Thread t1 = new Thread() {
					public void run() {
						for(int j = 0; j < (m / 2) * 3; j += 3) {
							int u = edges[j];
							int v = edges[j + 1];
							int w = edges[j + 2];
							
							if(dist[v] > dist[u] + w)
								dist[v] = dist[u] + w;
						}
					}
				};
				
				Thread t2 = new Thread() {
					public void run() {
						for(int j = ((m / 2) + 1) * 3; j < m * 3; j += 3) {
							int u = edges[j];
							int v = edges[j + 1];
							int w = edges[j + 2];
							
							if(dist[v] > dist[u] + w)
								dist[v] = dist[u] + w;
						}
					}
				};
				
				// Start both threads
				t1.start();
				t2.start();
				// Wait for the threads to finish (barrier action)
				try {
					t1.join();
					t2.join();
				} catch(Exception e) {
					
				}
			}
			
			parTime += System.currentTimeMillis();
		}
		
		// Do n-1 more to allow all negative weight cycle consequences to fully propagate
		// THE CONSEQUENCES WILL NEVER BE THE SAME
		for(int i = 0; i < n - 1; ++i) {
			for(int j = 0; j < m * 3; j += 3) {
				int u = edges[j];
				int v = edges[j + 1];
				int w = edges[j + 2];
				
				if(dist[v] > dist[u] + w)
					dist[v] = Double.NEGATIVE_INFINITY;
			}
		}
		
		// Finally, print out all vertices which weren't affected
		for(int i = 0; i < n; ++i)
			if(dist[i] != Double.NEGATIVE_INFINITY && dist[i] != Double.POSITIVE_INFINITY)
				System.out.println(i + 1);
		
		if(speedup)
			System.out.println("Speedup was " + ((seqTime * 1.0) / parTime));
	}
}
