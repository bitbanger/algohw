package main

import (
	"fmt"
	"math"
)

type Vertex struct {
	a, b float64
}

func euclideanDistance(a, b Vertex) float64 {
	dx := a.a - b.a
	dy := a.b - b.b
	
	return math.Sqrt(dx*dx + dy*dy)
}

func main() {
	var n int
	fmt.Scanf("%d", &n)
	
	vertices := make([]Vertex, n)
	for i := range vertices {
		var a, b float64
		fmt.Scanf("%f%f", &a, &b)
		
		vertices[i] = Vertex{a, b}
	}
	
	// S[i][j] = the minimum-cost triangulation between vertices i and j
	// A linear order of vertices around the polygon is provided by the sorted guarantee
	minCost := make([][]float64, n)
	
	// Initialize the array with NaN
	// I don't know why
	for i := range minCost {
		minCost[i] = make([]float64, n)
		for j := range minCost[i] {
			minCost[i][j] = math.NaN()
		}
	}
	
	// All adjacent vertices cannot be triangulated, so they are 0.0
	for i := 0; i < n - 1; i++ {
		minCost[i][i + 1] = 0.0
	}
	
	for d := 2; d < n; d++ {
		// Row loop
		for i := 0; i < n - d; i++ {
			// Here's our column index, which differs from the row index by the offset
			j := i + d
			
			minVal := math.Inf(1)
			
			for k := i; k < j; k++ {
				candidate := 0.0
				
				if i == 0 && j == n - 1 {
					// Special case: don't count the distance between the first and last vertices
					candidate = minCost[i][k] + minCost[k][j]
				} else {
					// Our best triangulation is the triangulated polygons between i and k and k and j, plus the triangle we form between i, k, and j
					// However, this problem only wants the length of the ADDED edges, so we only add the line between i and j when we draw the triangle
					// Even though this doesn't vary with the k, the k determines *which prior configuration* of lines we're adding to (I think)
					candidate = minCost[i][k] + minCost[k][j] + euclideanDistance(vertices[i], vertices[j])
				}
				
				if candidate < minVal {
					minVal = candidate
				}
			}
			
			minCost[i][j] = minVal
		}
	}
	
	// minCost[0][n - 1] = the minimum-cost triangulation between vertices 0 and n-1 = our answer
	fmt.Printf("%.4f\n", minCost[0][n - 1])
	
	
}
