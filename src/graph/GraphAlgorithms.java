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
	
	//Works for my simple test case,haven't got around to trying it out on the graph, and haven't cleaned the code up yet.
	public static int[] Dijkstras(int source, Graph<Integer> graph) { //movie is the class Movie, and is the node in the graph
        PriorityQueue minprio = new PriorityQueue();
        int[] dist = new int[graph.numVertices()]; //2D array of integers, where the left column is the node, and the right column is the distance to the source node
        int[] prev = new int[graph.numVertices()];

        for (int i = 0; i < prev.length; i++) {
            prev[i] = -1;
        }

        for (int i = 0; i < dist.length; i++) {
            dist[i] = graph.numVertices() + 1;
        }

        dist[source-1] = 0;
        prev[source-1] = 0;
        minprio.push(0, source); //Source node has priority 0 and, and is the 0th node
        List<Integer> neighbors = graph.getNeighbors(source-1);

        /*
        for (int i = 0; i < neighbors.size(); i++){
            int u = neighbors.get(i);
            minprio.push(1, u); //pushes the nodes with distance 1, because all nodes start with distance 1, and start labeling them from 1.
            dist[u] = 1;
        }
        */
        System.out.println("done with second loop: "+ Arrays.toString(dist));


        while (true){
            try {
                int u = minprio.topElement();
                minprio.pop();
                //System.out.println(u);
                List<Integer> newneighbors = graph.getNeighbors(u);
                for (int i = 0; i < newneighbors.size(); i++) {
                    System.out.println(u);
                    int v = newneighbors.get(i); // new movie
                    int alt = dist[u-1] + 1; //1 is the weight of u to v, which is always 1 because  all weights are 1
                    System.out.println("alt: "+alt);
                    System.out.println("dist[v]: "+dist[v-1]);
                    if (alt < dist[v-1]) {
                        //System.out.println("if statement succeeds for: "+alt+"<"+dist[v]);
                        dist[v-1] = alt;
                        prev[v-1] = u;
                        if (minprio.isPresent(v)) {
                            minprio.changePriority(v, alt);
                        } else {
                            System.out.println("Pushing "+v);
                            minprio.push(alt, v);
                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        System.out.println("done with while loop");
        return prev;
    }
}
