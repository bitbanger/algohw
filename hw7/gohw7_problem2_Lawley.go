package main

import (
	"fmt"
	"math"
)

type Edge struct {
	u, v, w int
}

func main() {
	var n, m, s int
	fmt.Scanf("%d%d\n%d", &n, &m, &s)
	s--
	
	edges := make([]Edge, m)
	for i := range edges {
		var u, v, w int
		fmt.Scanf("%d%d%d", &u, &v, &w)
		
		edges[i] = Edge{u - 1, v - 1, w}
	}
	
	dist := make([]float64, n)
	for i := range dist {
		dist[i] = math.Inf(1)
	}
	dist[s] = 0
	
	for i := 0; i < n - 1; i++ {
		for _, edge := range edges {
			if dist[edge.v] > dist[edge.u] + float64(edge.w) {
				dist[edge.v] = dist[edge.u] + float64(edge.w)
			}
		}
	}
	
	for i := 0; i < n - 1; i++ {
		for _, edge := range edges {
			if dist[edge.v] > dist[edge.u] + float64(edge.w) {
				dist[edge.v] = math.Inf(-1)
			}
		}
	}
	
	for i, v := range dist {
		if !math.IsInf(v, -1) && !math.IsInf(v, 1) {
			fmt.Printf("%d\n", i + 1)
		}
	}
}
