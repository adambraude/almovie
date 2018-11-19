package graph;
import java.util.Arrays;

import data.*;

public class GraphAlgorithms {
	
	public static int[][] floydWarshall(GraphIfc<Movie> graph){
		int v = graph.numVertices();
		int[][] paths = new int[v][v];
		int[][] pathsPrev = new int[v][v];
		for (int i = 0; i < v; i++) {
			Arrays.fill(pathsPrev[i], v+1);
		}
		for (Movie movie : graph.getVertices()) {
			for (Movie movie2 : graph.getNeighbors(movie)) {
				pathsPrev[movie.getMovieId()-1][movie2.getMovieId()-1] = 1;
			}
		}
		for (int k = 1; k < v; k++) {
			for (int i = 0; i < v; i++) {
				for (int j = 0; j < v; j++) {
					int option1 = pathsPrev[i][j];
					int option2 = pathsPrev[i][k] + pathsPrev[k][j];
					int b = Math.min(option1, option2);
					paths[i][j] = b;
				}
			}
			pathsPrev = paths;
			paths = new int[v][v];
		}
		return pathsPrev;
	}
}
