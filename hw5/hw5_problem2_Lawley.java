import java.util.Scanner;
import java.util.Iterator;

public class hw5_problem2_Lawley {
	public static int cycleCulprit = -1;
	
	public static void dfs(Vertex v, int parent) {
		if(v.seen) {
			cycleCulprit = v.a;
			System.out.print("YES\n" + v.a + " ");
			return;
		}
		else {
			v.seen = true;
			for(Vertex neighbor : v.neighbors)
				if(neighbor.a != parent && cycleCulprit == -1)
					dfs(neighbor, v.a);
		}
		
		if(cycleCulprit != -1) {
			if(cycleCulprit == v.a) {
				System.out.print("\n");
				System.exit(0);
			} else System.out.print(v.a + " ");
		}
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		Vertex[] graph = new Vertex[scan.nextInt()];
		
		for(int i = 0; i < graph.length; ++i)
			graph[i] = new Vertex(i + 1);
		
		int edges = scan.nextInt();
		for(int i = 0; i < edges; ++i) {
			int a = scan.nextInt() - 1;
			int b = scan.nextInt() - 1;
			graph[a].neighbors.add(graph[b]);
			graph[b].neighbors.add(graph[a]);
		}
		
		for(Vertex v : graph)
			if(!v.seen)
				dfs(v, -1);
		
		System.out.println("NO");
	}
}

class Vertex {
	public int a;
	public boolean seen;
	public VList<Vertex> neighbors;
	
	public Vertex(int a) {
		neighbors = new VList<Vertex>();
		this.a = a;
	}
	
	public boolean equals(Object other) {
		return (other instanceof Vertex) && ((Vertex)other).a == this.a;
	}
}

class VList<T> implements Iterable<T> {
	VNode<T> head;
	int size;
	
	public void add(T item) {
		if(head == null) head = new VNode<T>(item);
		
		else {
			VNode<T> curr = head;
			while(curr.next != null) {
				curr = curr.next;
			}
			curr.next = new VNode<T>(item);
			++size;
		}
	}
	
	public Iterator<T> iterator() {
		return new VIterator();
	}
	
	private class VIterator implements Iterator<T> {
		VNode<T> curr = head;
		
		public boolean hasNext() {
			return curr != null;
		}
		
		public T next() {
			T val = curr.value;
			curr = curr.next;
			return val;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}

class VNode<T> {
	public T value;
	public VNode<T> next;
	
	public VNode(T value) {
		this.value = value;
	}
}
