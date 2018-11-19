package graph;

import java.util.*;

/**
 * A directed graph class implemented using an adjacency list
 * The list is constructed from a HashMap of ArrayLists
 * Nodes can be of any generic type
 *
 *  @author Adam Braude and Eli Corpron
 *  @version 9.28.18
 *
 */

public class Graph<V> implements GraphIfc<V> {
  
  //Abstract Map, List as suggested in the assigment, 
  //but I suspect that some operations could be improved by replacing the Lists with HashSets.
  Map<V, List<V>> adjacencyList;
  
  /**
   *Constructor that creates a new empty graph of Vs
   */
  public Graph(){
    adjacencyList = new HashMap<V, List<V>>();
  }
  
  /**
	 * Returns the number of vertices in the graph
	 * @return The number of vertices in the graph
	 */
	public int numVertices(){
    return adjacencyList.size();
  }  
		
	/**
	 * Returns the number of edges in the graph
	 * @return The number of edges in the graph
	 */
	public int numEdges(){
    int out = 0;
    for(List<V> list : adjacencyList.values()){
        out+=list.size();
    }
    return out;
  }
	
	/**
	 * Removes all vertices from the graph
	 */
	public void clear(){
    adjacencyList.clear();
  }
		
	/** 
	 * Adds a vertex to the graph. This method has no effect if the vertex already exists in the graph. 
	 * @param v The vertex to be added
	 */
	public void addVertex(V v){
    if (containsNode(v)){
      return;
    }
    adjacencyList.put(v, new ArrayList<V>());
  }
	
	/**
	 * Adds an edge between vertices u and v in the graph. 
	 * @param u A vertex in the graph
	 * @param v A vertex in the graph
	 * @throws IllegalArgumentException if either vertex does not occur in the graph.
	 */
	public void addEdge(V u, V v){
    if (!containsNode(u) || !containsNode(v)){
      throw new IllegalArgumentException("At least one vertex did not appear in the graph!");
    } else {
      List<V> list = adjacencyList.get(u);
      if (!list.contains(v)){
        list.add(v);
      }
    }
  }

	/**
	 * Returns the set of all vertices in the graph.
	 * @return A set containing all vertices in the graph
	 */
	public Set<V> getVertices(){
    return adjacencyList.keySet();
  }
	
	/**
	 * Returns the neighbors of v in the graph. A neighbor is a vertex that is connected to
	 * v by an edge. If the graph is directed, this returns the vertices u for which an 
	 * edge (v, u) exists.
	 *  
	 * @param v An existing node in the graph
	 * @return All neighbors of v in the graph.			
	 */
	public List<V> getNeighbors(V v){
    return adjacencyList.get(v);
  }

	/**
	 * Determines whether the given vertex is already contained in the graph. The comparison
	 * is based on the <code>equals()</code> method in the class V. 
	 * 
	 * @param v The vertex to be tested.
	 * @return True if v exists in the graph, false otherwise.
	 */
	public boolean containsNode(V v){
    //We can use this because containsKey checks for .equals() equality
    if (adjacencyList.containsKey(v)){
      return true;
    }
    return false;
  }
	
	/**
	 * Determines whether an edge exists between two vertices. In a directed graph,
	 * this returns true only if the edge starts at v and ends at u. 
	 * @param v A node in the graph
	 * @param u A node in the graph
	 * @return True if an edge exists between the two vertices
	 * @throws IllegalArgumentException if either vertex does not occur in the graph
	 */
	public boolean edgeExists(V v, V u){
    if (!containsNode(u) || !containsNode(v)){
      throw new IllegalArgumentException("At least one vertex did not appear in the graph!");
    }
    return adjacencyList.get(v).contains(u);
  }

	/**
	 * Returns the degree of the vertex. In a directed graph, this returns the outdegree of the
	 * vertex. 
	 * @param v A vertex in the graph
	 * @return The degree of the vertex
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	public int degree(V v){
    if (!containsNode(v)){
      throw new IllegalArgumentException("Cannot find degree of vertex " + v + " not in graph.");
    }
    return adjacencyList.get(v).size();
  }
	
	/**
	 * Returns a string representation of the graph. The string representation shows all
	 * vertices and edges in the graph. 
	 * @return A string representation of the graph
	 */
	public String toString(){
    return adjacencyList.toString();
  }
  
  /**
   *Main method for unit testing
   */
  public static void main(String[] args) {
    Graph<Integer> graph = new Graph<Integer>();
    graph.addVertex(1);
    graph.addVertex(5);
    graph.addVertex(13);
    graph.addVertex(15);
    graph.addEdge(1,15);
    graph.addEdge(5,13);
    graph.addEdge(1,13);
    graph.addEdge(1,13);
    System.out.println(graph);
    System.out.println(graph.getVertices());
    System.out.println("Does graph contain 5? " + graph.containsNode(5));
    System.out.println("Does graph contain 7? " + graph.containsNode(7));
    System.out.println("Num vertices: " + graph.numVertices() + " Num edges: " + graph.numEdges());
    System.out.println("Neighbors of 1 are: " + graph.getNeighbors(1));
    System.out.println("This is " + graph.degree(1) + " neighbors");
    System.out.println("Neighbors of 15 are: " + graph.getNeighbors(15));
    System.out.println("This is " + graph.degree(15) + " neighbors");
    System.out.println("Is there an edge between 1 and 13?: " + graph.edgeExists(1, 13));
    System.out.println("Is there an edge between 1 and 5?: " + graph.edgeExists(1, 5));
    try {
      System.out.println("Is there an edge between 1 and 13?: " + graph.edgeExists(1, 19)); 
    }
    catch (IllegalArgumentException e){
      System.out.println("Caught " + e);
    }
    System.out.println("Neighbors of 99 are: " + graph.getNeighbors(99));
    try {
      System.out.println("This is " + graph.degree(99) + " neighbors");
    }
    catch (IllegalArgumentException e){
      System.out.println("Caught " + e);
    }
    graph.clear();
    System.out.println(graph);
    System.out.println("Num vertices: " + graph.numVertices() + " Num edges: " + graph.numEdges());
  }
}
