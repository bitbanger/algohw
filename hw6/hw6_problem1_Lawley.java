import java.util.Scanner;

public class hw6_problem1_Lawley {
	// Size-four array of size-two int arrays used as two-tuples
	// Time complexity: O(m + n)
	public static int[][] adjacentSpots(char[][] board, State state, boolean robot) {
		int[][] spots = new int[4][2];
		
		int x, y;
		int ox, oy;
		
		if(!robot) {
			x = state.x1;
			y = state.y1;
			ox = state.x2;
			oy = state.y2;
		} else {
			x = state.x2;
			y = state.y2;
			ox = state.x1;
			oy = state.y1;
		}
		
		// Turn the other robot into a "wall" for a moment
		char nonRobot = board[ox][oy];
		board[ox][oy] = 'x';
		
		// Initialize the spots to -1
		for(int i = 0; i < 4; ++i) for(int j = 0; j < 2; ++j) spots[i][j] = -1;
		
		for(int i = 0; i < board.length; ++i) {
			if(board[i][y] == 'x') {
				if(i < x) {
					spots[0][0] = i + 1;
					spots[0][1] = y;
				} else if(i > x) {
					spots[1][0] = i - 1;
					spots[1][1] = y;
					break;
				}
			}
		}
		
		// Do a pass
		for(int i = 0; i < board[0].length; ++i) {
			if(board[x][i] == 'x') {
				if(i < y) {
					spots[2][0] = x;
					spots[2][1] = i + 1;
				} else if(i > y) {
					spots[3][0] = x;
					spots[3][1] = i - 1;
					break;
				}
			}
		}
		
		// Remove the "robot wall" from the board, since it's not our board
		board[ox][oy] = nonRobot;
		
		return spots;
	}
	
	// Did the robot stay on the board?
	// Complexity: O(1)
	public static boolean validState(State state) {
		return (state != null) && (state.x1 != -1) && (state.y1 != -1) && (state.x2 != -1) && (state.y2 != -1);
	}
	
	// Generate all possible next states (at most 8) given a current state
	// Complexity: O(m + n)
	public static State[] nextStates(char[][] board, State state) {
		State[] states = new State[8];
		
		int[][] spots1 = adjacentSpots(board, state, false);
		int[][] spots2 = adjacentSpots(board, state, true);
		
		int numValidStates = 0;
		
		for(int i = 0; i < 4; ++i) {
			states[i] = new State(spots1[i][0], spots1[i][1], state.x2, state.y2);
			if(validState(states[i])) ++numValidStates;
			states[i + 4] = new State(state.x1, state.y1, spots2[i][0], spots2[i][1]);
			if(validState(states[i + 4])) ++numValidStates;
		}
		
		State[] validStates = new State[numValidStates];
		int j = -1;
		
		for(int i = 0; i < 8; ++i)
			if(validState(states[i])) validStates[++j] = states[i];
		
		return validStates;
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int m = scan.nextInt(), n = scan.nextInt();
		
		char[][] board = new char[m][n];
		
		scan.nextLine(); // Move the line up....
		
		State initialState = new State(0, 0, 0, 0);
		
		boolean robot = false;
		boolean winSpot = false;
		
		int winX1 = 0, winY1 = 0, winX2 = 0, winY2 = 0;
		
		// Huge loop to initialize everything
		// Complexity: O(m * n)
		for(int i = 0; i < m; ++i) {
			String line = scan.nextLine();
			for(int j = 0; j < n; ++j) {
				char x = line.charAt(j);
				// Find where the robots are for our initial state and disregard them for the board
				if(x == 'r') {
					if(!robot) {
						initialState.x1 = i;
						initialState.y1 = j;
						robot = true;
					} else {
						initialState.x2 = i;
						initialState.y2 = j;
					}
					
					board[i][j] = '.';
				} else {
					board[i][j] = x;
					
					// Mark our final positions and leave them on the board, I guess
					if(board[i][j] == 't') {
						if(!winSpot) {
							winX1 = i;
							winY1 = j;
							winSpot = true;
						} else {
							winX2 = i;
							winY2 = j;
						}
					}
				}
			}
		}
		
		// Now we do our BFS until completion, maintaining a visited list for every possible configuration
		
		int[][][][] seen = new int[m][n][m][n];
		for(int a = 0; a < m; ++a) for(int b = 0; b < n; ++b) for(int c = 0; c < m; ++c) for(int d = 0; d < n; ++d) seen[a][b][c][d] = -1;
		
		// TODO: Implement a linked queue
		VList<State> queue = new VList<State>();
		queue.add(initialState);
		seen[initialState.x1][initialState.y1][initialState.x2][initialState.y2] = 0;
		
		// Go through every configuration branch from our initial state and search for a winning state
		// Complexity: O((m + n) * (m * n)^2)
		while(!queue.isEmpty()) {
			State state = queue.remove();
			
			// Have we won yet?
			if((state.x1 == winX1 && state.y1 == winY1 && state.x2 == winX2 && state.y2 == winY2) || (state.x2 == winX1 && state.y2 == winY1 && state.x1 == winX2 && state.y1 == winY2)) {
				System.out.println(seen[state.x1][state.y1][state.x2][state.y2]);
				System.exit(0);
			}
			
			for(State nextState : nextStates(board, state)) {
				if(seen[nextState.x1][nextState.y1][nextState.x2][nextState.y2] == -1) {
					queue.add(nextState);
					seen[nextState.x1][nextState.y1][nextState.x2][nextState.y2] = seen[state.x1][state.y1][state.x2][state.y2] + 1;
					nextState.parent = state;
				}
			}
		}
		
		// No way to win :(
		System.out.println("-1");
		System.exit(0);
		
	}
}

class State {
	public int x1, y1, x2, y2;
	public State parent;
	
	public State(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public String toString() {
		return String.format("(%d, %d, %d, %d)", x1, y1, x2, y2);
	}
}

class VList<T> {
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
		}
		
		++size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public T remove() {
		if(size == 0) return null;
		
		T val = head.value;
		head = head.next;
		--size;
		
		return val;
	}
}

class VNode<T> {
	public T value;
	public VNode<T> next;
	
	public VNode(T value) {
		this.value = value;
	}
}
