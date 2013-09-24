import java.util.Scanner;
import java.util.Iterator;
import java.util.Arrays;

public class hw6_problem2_Lawley {
	// This function is a modified version of one I wrote for a Parallel Computing assignment I wrote last quarter.
	private static Edge[] merge(Edge[] left, Edge[] right) {
		Edge[] retVal = new Edge[left.length + right.length];
		
		int i = 0;
		int j = 0;
		int k = 0;
		
		while(i < left.length && j < right.length) {
			if(left[i].compareTo(right[j]) > 0) {
				retVal[k] = right[j];
				j++;
			} else {
				retVal[k] = left[i];
				i++;
			}
			
			k++;
		}
		
		while(i < left.length) {
			retVal[k] = left[i];
			i++;
			k++;
		}
		
		while(j < right.length) {
			retVal[k] = right[j];
			j++;
			k++;
		}
		
		return retVal;
	}
	
	// This function is a modified version of one I wrote for a Parallel Computing assignment I wrote last quarter.
	private static Edge[] mergeSort(Edge[] unsorted) {
		if(unsorted.length <= 1) return unsorted;
		
		int mid = unsorted.length / 2;
		
		Edge[] left = new Edge[mid];
		Edge[] right = new Edge[unsorted.length - left.length];
		
		// This is O(n) time, and I could do it easily in O(n) time myself
		System.arraycopy(unsorted, 0, left, 0, mid);
		System.arraycopy(unsorted, mid, right, 0, unsorted.length - mid);
		
		left = mergeSort(left);
		right = mergeSort(right);
		
		return merge(left, right);
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int n = scan.nextInt(), m = scan.nextInt();
		
		// If m is less than n, the graph is disconnected, and there exists no spanning tree.
		if(m < n) {
			System.out.println("-1");
			System.exit(0);
		}
		
		UnionFind uf = new UnionFind(n);
		
		Edge[] edges = new Edge[m];
		
		for(int i = 0; i < m; ++i) {
			edges[i] = new Edge(scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt() == 1);
		}
		
		int totalCost = 0;
		
		
		for(Edge e : mergeSort(edges)) {
			if(uf.find(e.u - 1) != uf.find(e.v - 1)) {
				totalCost += e.w;
				uf.union(e.u - 1, e.v - 1);
			} else if(e.f) {
				System.out.println("-1");
				System.exit(0);
			}
		}
		
		System.out.println(totalCost);
	}
}

class Edge implements Comparable<Edge> {
	public int u;
	public int v;
	public int w;
	public boolean f;
	
	public Edge(int u, int v, int w, boolean f) {
		this.u = u;
		this.v = v;
		this.w = w;
		this.f = f;
	}
	
	public int compareTo(Edge other) {
		if(f && !other.f) return -1;
		else if(!f && other.f) return 1;
		else return w - other.w;
	}
	
	public String toString() {
		return String.format("(%d, %d): %d (%b)", u, v, w, f);
	}
}

class UnionFind {
	int[] size;
	int[] boss;
	VList[] set;
	
	public UnionFind(int numVertices) {
		size = new int[numVertices];
		boss = new int[numVertices];
		set = new VList[numVertices];
		for(int i = 0; i < size.length; ++i) {
			size[i] = 1;
			boss[i] = i;
			set[i] = new VList();
			set[i].add(i);
		}
	}
	
	public void union(int a, int b) {
		if(boss[a] == boss[b]) return;
		
		int u = a, v = b;
		if(size[boss[u]] <= size[boss[v]]) {
			u = b;
			v = a;
		}
		
		set[boss[u]].appendList(set[boss[v]]);
		size[boss[u]] += size[boss[v]];
		for(int z : set[boss[v]]) {
			boss[z] = boss[u];
		}
	}
	
	public int find(int i) {
		return boss[i];
	}
}

class VList implements Iterable<Integer> {
	VNode head;
	int size;
	
	public void add(int item) {
		if(head == null) head = new VNode(item);
		
		else {
			VNode curr = head;
			while(curr.next != null) {
				curr = curr.next;
			}
			curr.next = new VNode(item);
			++size;
		}
	}
	
	public void appendList(VList other) {
		VNode tail = head;
		while(tail.next != null) tail = tail.next;
		tail.next = other.head;
		size += other.size;
	}
	
	public Iterator<Integer> iterator() {
		return new VIterator();
	}
	
	private class VIterator implements Iterator<Integer> {
		VNode curr = head;
		
		public boolean hasNext() {
			return curr != null;
		}
		
		public Integer next() {
			int val = curr.value;
			curr = curr.next;
			return val;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
}

class VNode {
	public int value;
	public VNode next;
	
	public VNode(int value) {
		this.value = value;
	}
}

