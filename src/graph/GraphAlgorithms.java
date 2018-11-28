package graph;
import java.util.Arrays;

import data.*;

/*
 * This Class has methods for finding shortest paths in Graphs
 *
 *  @author Adam Braude and Eli Corpron
 *  @version 11.27.18
 */
public class GraphAlgorithms {
	
	/**
     	* floydWarshall that takes a graph of Movies and returns a 2d array of shortest paths between pairs of nodes
     	* @param graph The graph of Movies to be parsed
     	* @return Returns a 2d array of ints that correpsonds to the shortest path between nodes.
     	*/
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
	
	/**
     	* Dijjkstras that works on a graph of Movie objects and a Movie object for a starting node, then finds the shortest
     	* path to every node in the graph. Returns this in an array of ints, where the index corresponds to the node, and
     	* the int at the index is the preceding node.
     	* @param source The source node
     	* @param graph The graph to be parsed
     	* @return Returns an array of ints, where the index is the node, and the int at the index is the previous node
    	*/
	public static int[] Dijkstras(Movie source, GraphIfc<Movie> graph) { //works with any graph with edges of weight 1
        PriorityQueue minprio = new PriorityQueue();
        int[] dist = new int[graph.numVertices()];
        int[] prev = new int[graph.numVertices()];

        for (int i = 0; i < prev.length; i++) { //initializes the prev with a list of unatainable nodes
            prev[i] = -1;
        }

        for (int i = 0; i < dist.length; i++) { //initiliazes the length with an unattainable length
            dist[i] = graph.numVertices() + 1;
        }

        dist[source.getMovieId()-1] = 0; //source is null, and Movie index is 1-based
        prev[source.getMovieId()-1] = 0; //source is null
        minprio.push(0, source.getMovieId()); //Source node has priority 0 and, and is the 0th node

        Movie[] graphlist = new Movie[graph.numVertices()]; //stores the movies that we will need to access in an array
        graphlist[source.getMovieId()-1] = source;

        while (true){ //since the min priority queue isn't initialized with all nodes, we know it runs out of nodes when it breaks
            try {
                int u = minprio.topElement(); //store the top node int
                minprio.pop(); //remove it from the queue
                Movie currentmovie = graphlist[u-1]; //grabs the movie from the list
                List<Movie> newneighbors = graph.getNeighbors(currentmovie); //get a list of the neighbors for u
                for (int i = 0; i < newneighbors.size(); i++) {
                    Movie nextmovie = newneighbors.get(i); // next movie from the list of neighbors
                    graphlist[nextmovie.getMovieId()-1] = nextmovie; //adds it to the movie list
                    int alt = dist[u-1] + 1; //1 is the weight of u to v, which is always 1 because  all weights are 1
                    int v = nextmovie.getMovieId();
                    if (alt < dist[v-1]) { //if the new path is better than the old one
                        dist[v-1] = alt; //distance is now alt
                        prev[v-1] = u; //previous for this node is now the previous node
                        if (minprio.isPresent(v)) {
                            minprio.changePriority(v, alt); //if the node is in the graph, change it to the new prio
                        } else {
                            minprio.push(alt, v); //else it needs to be added to the queue
                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return prev;
    }
}
