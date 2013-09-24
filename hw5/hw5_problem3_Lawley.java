import java.util.Scanner;
import java.util.Iterator;

public class hw5_problem3_Lawley {
	public static void markDepths(Vertex v, int accum, int parent) {
		v.depth = Math.max(v.depth, accum);
		
		for(Vertex n : v.neighbors) {
			// This as well as the case below are probably overkill, but I don't have time to refine this
			if(!n.seen)
				markDepths(n, v.depth + 1, v.a);
			// Cover the case where we encounter someone on the same chain and need to update
			else
				v.depth = Math.max(n.depth + 1, v.depth);
		}
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		Vertex[] graph = new Vertex[scan.nextInt()];
		
		for(int i = 0; i < graph.length; ++i)
			graph[i] = new Vertex(i + 1);
		
		for(int i = 0; i < graph.length; ++i) {
			int r;
			while((r = scan.nextInt()) != 0)
				graph[i].addNeighbor(graph[r - 1]);
		}
		
		for(int i = 0; i < graph.length; ++i) markDepths(graph[i], 1, -1);
		
		int max = 0;
		for(int i = 0; i < graph.length; ++i)
			if(graph[i].depth > max) max = graph[i].depth;
		
		System.out.println(max);
	}
}

class Vertex {
	public int a;
	public boolean seen;
	public int depth;
	
	public VList<Vertex> neighbors;
	
	public Vertex(int a) {
		neighbors = new VList<Vertex>();
		this.a = a;
		this.seen = false;
		this.depth = 1;
	}
	
	public boolean equals(Object other) {
		return (other instanceof Vertex) && ((Vertex)other).a == this.a;
	}
	
	public void addNeighbor(Vertex v) {
		neighbors.add(v);
	}
	
	public String toString() {
		String r = a + ": ";
		for(Vertex v : neighbors) r = r + v.a + " ";
		return r;
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
