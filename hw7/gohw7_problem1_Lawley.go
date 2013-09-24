package main

import (
	"fmt"
	"math"
)

func main() {
	var n, m int
	fmt.Scanf("%d%d", &n, &m)
	
	s := make([][][]float64, n)
	for i := range s {
		s[i] = make([][]float64, n)
		for j := range s[i] {
			s[i][j] = make([]float64, n)
			for k := range s[i][j] {
				if i != j {
					s[i][j][k] = math.Inf(1)
				}
			}
		}
	}
	
	paths := make([][]int, n)
	for i := range paths {
		paths[i] = make([]int, n)
		paths[i][i] = 1
	}
	
	for i := 0; i < m; i++ {
		var a, b int
		fmt.Scanf("%d%d", &a, &b)
		a--
		b--
		
		fmt.Scanf("%f", &s[a][b][0])
		paths[a][b] = 1
	}
	
	for k := 1; k < n; k++ {
		for i := 0; i < n; i++ {
			for j := 0; j < n; j++ {
				last := s[i][j][k - 1]
				next := s[i][k][k - 1] + s[k][j][k - 1]
				
				if last == next && last != math.Inf(1) && k != i && k != j {
					paths[i][j] += paths[i][k] * paths[k][j]
				} else if next < last {
					paths[i][j] = paths[i][k] * paths[k][j]
				}
				
				s[i][j][k] = math.Min(last, next)
			}
		}
	}
	
	for i := 0; i < n; i++ {
		for j := 0; j < n; j++ {
			shortestPath := s[i][j][n - 1]
			
			if shortestPath == math.Inf(1) {
				fmt.Print("inf")
			} else {
				fmt.Printf("%.0f", shortestPath)
			}
			
			fmt.Printf("/%d ", paths[i][j])
		}
		fmt.Print("\n")
	}
}
