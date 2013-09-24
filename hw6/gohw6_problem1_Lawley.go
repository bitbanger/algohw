package main

import (
	"fmt"
	"bufio"
	"os"
	"container/list"
)

type State struct {
	x1, y1, x2, y2 int
}

type StateQueue struct {
	list *list.List // >dat list
}

func (q StateQueue) Push(state State) {
	q.list.PushBack(state)
}

func (q StateQueue) Pop() State {
	v, _ := q.list.Remove(q.list.Front()).(State)
	
	return v
}

func (q StateQueue) IsEmpty() bool {
	return q.list.Len() == 0
}

func NewStateQueue() *StateQueue {
	v := &StateQueue{}
	v.list = list.New()
	
	return v
}

func AdjacentSpots(board [][]bool, state State, robot bool) [][]int {
	spots := make([][]int, 4)
	for i := range spots {
		spots[i] = make([]int, 2)
		for j := range spots[i] {
			spots[i][j] = -1
		}
	}
	
	var x, y, ox, oy int
	if(!robot) {
		x, y, ox, oy = state.x1, state.y1, state.x2, state.y2
	} else {
		x, y, ox, oy = state.x2, state.y2, state.x1, state.y1
	}
	
	// Turn the other robot into a wall for a second
	board[ox][oy] = false
	
	// Do a pass on the X axis to check for either side's collisions
	for i := 0; i < len(board); i++ {
		if !board[i][y] {
			if i < x {
				spots[0][0] = i + 1
				spots[0][1] = y
			} else if i > x {
				spots[1][0] = i - 1
				spots[1][1] = y
				break
			}
		}
	}
	
	// Now do a similar pass on the Y axis
	for i := 0; i < len(board[0]); i++ {
		if !board[x][i] {
			if i < y {
				spots[2][0] = x
				spots[2][1] = i + 1
			} else if i > y {
				spots[3][0] = x
				spots[3][1] = i - 1
				break
			}
		}
	}
	
	// The other robot was able to get there in the first place, so set him back to true
	// (It's not our board; this is just a reference!)
	board[ox][oy] = true
	
	return spots
}

func IsValidState(state State) bool {
	return state != State{} && state.x1 != -1 && state.y1 != -1 && state.x2 != -1 && state.y2 != -1
}

func NextStates(board [][]bool, state State) []State {
	possibleStates := make([]State, 8)
	
	numValidStates := 0
	
	spots1 := AdjacentSpots(board, state, false)
	spots2 := AdjacentSpots(board, state, true)
	
	for i := 0; i < 4; i++ {
		possibleStates[i] = State{spots1[i][0], spots1[i][1], state.x2, state.y2}
		if IsValidState(possibleStates[i]) {
			numValidStates++
		}
		
		possibleStates[i + 4] = State{state.x1, state.y1, spots2[i][0], spots2[i][1]}
		if IsValidState(possibleStates[i + 4]) {
			numValidStates++
		}
	}
	
	validStates := make([]State, numValidStates)
	
	j := 0
	for _, s := range possibleStates {
		if IsValidState(s) {
			validStates[j] = s
			j++
		}
	}
	
	return validStates
}

func main() {
	var m, n int
	fmt.Scanf("%d%d", &m, &n)
	
	win1 := State{}
	win2 := State{}
	
	initialState := State{}
	
	board := make([][]bool, m)
	
	in := bufio.NewReader(os.Stdin)
	
	robot := false
	winPos := false
	
	for i := 0; i < m; i++ {
		board[i] = make([]bool, n)
		
		line, _ := in.ReadString('\n')
		
		for j, c := range line {
			switch string(c) {
				case "r":
					if !robot {
						initialState.x1, initialState.y1 = i, j
						robot = true
					} else {
						initialState.x2, initialState.y2 = i, j
					}
					board[i][j] = true
				case "t":
					if !winPos {
						win1.x1, win1.y1, win2.x2, win2.y2 = i, j, i, j
						winPos = true
					} else {
						win1.x2, win1.y2, win2.x1, win2.y1 = i, j, i, j
					}
					board[i][j] = true
				case ".":
					board[i][j] = true
				case "x":
					board[i][j] = false
					
			}
		}
	}

	seen := make([][][][]int, m)
	for i := range seen {
		seen[i] = make([][][]int, n)
		for j := range seen[i] {
			seen[i][j] = make([][]int, m)
			for k := range seen[i][j] {
				seen[i][j][k] = make([]int, n)
				for l := range seen[i][j][k] {
					seen[i][j][k][l] = -1
				}
			}
		}
	}
	
	queue := NewStateQueue()
	queue.Push(initialState)
	seen[initialState.x1][initialState.y1][initialState.x2][initialState.y2] = 0
	
	for !queue.IsEmpty() {
		state := queue.Pop()
		
		if state == win1 || state == win2 {
			fmt.Println(seen[state.x1][state.y1][state.x2][state.y2])
			os.Exit(0)
		}
		
		neighbors := NextStates(board, state)
		
		for _, neighbor := range neighbors {
			if seen[neighbor.x1][neighbor.y1][neighbor.x2][neighbor.y2] == -1 {
				queue.Push(neighbor)
				seen[neighbor.x1][neighbor.y1][neighbor.x2][neighbor.y2] = seen[state.x1][state.y1][state.x2][state.y2] + 1
			}
		}
	}
	
	fmt.Println("-1")
}
