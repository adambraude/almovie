package analyzer;
import util.*;
import graph.*;
import data.*;
import java.util.Scanner;

/*
 * A program that takes movie and reviewer data and allows users to build
 * and explore a graph of the data.
 *
 *  @author Adam Braude and Eli Corpron
 *  @version 11.18.18
 */
public class MovieLensAnalyzer {
	
	//build graph where u and v are adjacent if at least 33.0% of the users who rated u gave the same rating to v.
	private static GraphIfc<Movie> build33PercentAgreeGraph(DataLoader data) {
		GraphIfc<Movie> output = new Graph<Movie>();
		//nodes
		for (Movie movie : data.getMovies().values()){
			output.addVertex(movie);
		}
		//edges
		for (Movie movie : data.getMovies().values()){
			for (Movie movie2 : data.getMovies().values()) {
				if (movie2.equals(movie)) continue;
				double count = 0;
				double agree = 0;
				for (Integer reviewerID : movie.getRatings().keySet()) {
					count++;
					if (movie2.getRating(reviewerID) == movie.getRating(reviewerID)) {
						agree++;
					}
				}
				if (agree/count >= 0.33) {
					output.addEdge(movie, movie2);
				}
			}
		}
		return output;
	}
	
	//build graph where u and v are adjacent if 12 users who gave a rating to u gave the same rating to v
		private static GraphIfc<Movie> build12AgreeGraph(DataLoader data) {
			GraphIfc<Movie> output = new Graph<Movie>();
			//nodes
			for (Movie movie : data.getMovies().values()){
				output.addVertex(movie);
			}
			//edges
			for (Movie movie : data.getMovies().values()){
				for (Movie movie2 : data.getMovies().values()) {
					if (movie2.equals(movie)) continue;
					double agree = 0;
					for (Integer reviewerID : movie.getRatings().keySet()) {
						if (movie2.getRating(reviewerID) == movie.getRating(reviewerID)) {
							agree++;
						}
					}
					if (agree >= 12) {
						output.addEdge(movie, movie2);
					}
				}
			}
			return output;
		}
	
	//Give the user options for exploring the generated graph
	private static void exploreGraph(GraphIfc<Movie> graph, DataLoader data) {
		Scanner scan = new Scanner(System.in);
		String input = "";
		while (!input.equals("5")) {
			System.out.println("[Option 1] Print out statistics about the graph");
			System.out.println("[Option 2] Print node information");
			System.out.println("[Option 3] Display shortest path between two nodes");
			System.out.println("[Option 4] Search for movies by title");
			System.out.println("[Option 5] Quit");
			input = scan.nextLine();
			if (input.equals("1")) {
				System.out.println("Graph statistics:");
				int v = graph.numVertices();
				int e = graph.numEdges();
				System.out.println("\t |V| = " + v + " vertices");
				System.out.println("\t |E| = " + e + " edges");
				System.out.println("\t Density = " + (double)e/(((double)v)*((double)(v-1))));
				int maxDeg = 0;
				int maxDegNode = 0;
				for (Movie movie : graph.getVertices()) {
					int deg = graph.degree(movie);
					if (deg > maxDeg) {
						maxDeg = deg;
						maxDegNode = movie.getMovieId();
					}
				}
				System.out.println("\t Max. degree = " + maxDeg + " (node " + maxDegNode + ")");
				int[][] shortestPaths = GraphAlgorithms.floydWarshall(graph);
				maxDeg = 0;
				maxDegNode = 0;
				int maxDegNode2 = 0;
				double count = 0;
				double avgPathLength = 0;
				for (int i = 0; i < v; i++) {
					for (int j = 0; j < v; j++) {
						if (shortestPaths[i][j] <= v) {
							count ++;
							avgPathLength += shortestPaths[i][j];
							if (shortestPaths[i][j] > maxDeg) {
								maxDeg = shortestPaths[i][j];
								maxDegNode = i;
								maxDegNode2 = j;
							}
						}
					}
				}
				avgPathLength = (avgPathLength/count);
				System.out.println("\t Diameter: " + maxDeg + "(from " + (maxDegNode+1) + " to " + (maxDegNode2+1) + ")");
				System.out.println("\t Avg. path length: " + avgPathLength);
			} else if (input.equals("2")) {
				System.out.println("Enter movie id (1-" + graph.numVertices() + "):");
				int id = scan.nextInt();
				scan.nextLine();
				Movie movie = data.getMovies().get(id);
				System.out.println(movie);
				System.out.println("Neighbors:");
				for (Movie movie2 : graph.getNeighbors(movie)) {
					System.out.println("\t" + movie2.getTitle());
				}
			} else if (input.equals("3")) {
				System.out.println("Enter starting node (1-" + graph.numVertices() + "):");
				int start = scan.nextInt();
				scan.nextLine();
				System.out.println("Enter ending node (1-" + graph.numVertices() + "):");
				int end = scan.nextInt();
				scan.nextLine();

				Movie startingmovie = data.getMovies().get(start);
				int[] path = GraphAlgorithms.Dijkstras(startingmovie, graph);
				if (path[end-1] == -1) { //path is 0-index-based
					System.out.println("The start movie and the end movie are not connected!");
				}
				int counter = end; //counter is 1-index based
				while (counter != start){
					Movie movie1 = data.getMovies().get(counter); //checks current movie
					Movie movie2 = data.getMovies().get(path[counter-1]); //checks the next movie in line to the start
					counter = path[counter-1]; //sets the counter to the next movie in line
					System.out.println(movie1.getTitle()+" ===> "+movie2.getTitle());
				} //Starts with the end movie, ends with the start movie
			}
			else if (input.equals("4")) {
				System.out.println("Enter string to search movie titles for:");
				String key = scan.nextLine();
				System.out.println("The movies with titles containing " + key + " are:");
				for (Movie movie : data.getMovies().values()) {
					if (movie.getTitle().contains(key)) {
						System.out.println("\t" + movie.getTitle() + " (" + movie.getMovieId() + ")");
					}
				}
			}
			else if (input.equals("5")) {
					System.out.println("Exiting...bye");
			} else {
				System.out.println("Not understood. Input (1-5)");
			}
		}
	}
	
	/*
	 * Enters the MovieLens interface, taking a ratings file and movie file as input
	 */
	public static void main(String[] args){	
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}		
		DataLoader dataLoad = new DataLoader();
		dataLoad.loadData(args[1], args[0]);
		System.out.println("======== Welcome to MovieLens Analyzer ========");
		System.out.println("The files being analyzed are");
		System.out.println("./src/ml-latest-small/ratings.csv");
		System.out.println("./src/ml-latest-small/movies_top_1000.csv");
		System.out.println("");
		System.out.println("There is 1 choice for defining adjacency:");
		System.out.println("[Option 1] u and v are adjacent if at least 33.0% of the users who rated u gave the same rating to v.");
		System.out.println("[Option 2] u and v are adjacent if the same 12 users gave the same rating to both movies.");
		System.out.println("Choose an option to build the graph (1-2)");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		while (!input.equals("1") && !input.equals("2")) {
			System.out.println("Not understood. Input (1-2)");
			input = scan.nextLine();
		}
		System.out.println("Building graph...");
		long time = System.currentTimeMillis();
		GraphIfc<Movie> graph = null;
		if (input.equals("1")) {
			graph = build33PercentAgreeGraph(dataLoad);
		}
		if (input.equals("2")) {
			graph = build12AgreeGraph(dataLoad);
		}
		System.out.println("Built graph in " + (System.currentTimeMillis() - time) + "ms");
		exploreGraph(graph, dataLoad);
	}
}
