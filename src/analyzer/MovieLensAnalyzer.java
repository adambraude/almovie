package analyzer;
import util.*;
import graph.*;
import data.*;
import java.util.Scanner;

public class MovieLensAnalyzer {
	
	//build graph where u and v are adjacent if at least 33.0% of the users who rated u gave the same rating to v.
	private static GraphIfc<Movie> build33Graph(DataLoader data) {
		GraphIfc<Movie> output = new Graph<Movie>();
		//nodes
		for (Movie movie : data.getMovies().values()){
			output.addVertex(movie);
		}
		//edges
		for (Movie movie : data.getMovies().values()){
			for (Movie movie2 : data.getMovies().values()) {
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
	
	//Explore the generated graph
	private static void exploreGraph(GraphIfc<Movie> graph) {
		Scanner scan = new Scanner(System.in);
		String input = "";
		while (!input.equals("4")) {
			System.out.println("[Option 1] Print out statistics about the graph");
			System.out.println("[Option 2] Print node information");
			System.out.println("[Option 3] Display shortest path between two nodes");
			System.out.println("[Option 4] Quit");
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
				System.out.println("\t Diameter: unknown because no Floyd-Warshall");
				System.out.println("\t Avg. path length: unknown because no Floyd-Warshall");
			} else if (input.equals("2")) {
				
			} else if (input.equals("3")) {
				
			}
			else if (input.equals("4")) {
					System.out.println("Exiting...bye");
			} else {
				System.out.println("Not understood. Input (1-4)");
			}
		}
	}
	
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres		
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}		
		// FILL IN THE REST OF YOUR PROGRAM
		DataLoader dataLoad = new DataLoader();
		dataLoad.loadData(args[1], args[0]);
		System.out.println("======== Welcome to MovieLens Analyzer ========");
		System.out.println("The files being analyzed are");
		System.out.println("./src/ml-latest-small/ratings.csv");
		System.out.println("./src/ml-latest-small/movies_top_1000.csv");
		System.out.println("");
		System.out.println("There is 1 choice for defining adjacency:");
		System.out.println("[Option 1] u and v are adjacent if at least 33.0% of the users who rated u gave the same rating to v.");
		System.out.println("Choose an option to build the graph (1)");
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		while (!input.equals("1")) {
			System.out.println("Not understood. Input (1)");
			input = scan.nextLine();
		}
		if (input.equals("1")) {
			System.out.println("Building graph...");
			long time = System.currentTimeMillis();
			GraphIfc<Movie> graph = build33Graph(dataLoad);
			System.out.println("Built graph in " + (System.currentTimeMillis() - time) + "ms");
			exploreGraph(graph);
		}
	}
}
