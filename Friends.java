package friends;

import java.util.*;

/**
 * Friends derives friendship characteristics using graph algorithms.
 * 
 * @author Data Structures Fall 2020
 * @author Cynthia Zhu
 */
public class Friends {
	
	/**
	 * Finds the shortest chain of people from p1 to p2 using breadth-first search.
	 * 
	 * @param g Friendship graph
	 * @param p1 First person in chain
	 * @param p2 Last person in chain
	 * @return Shortest chain from p1 to p2, null if no path exists from p1 to p2
	 */
	public static ArrayList<String> findShortestChain(Graph g, String p1, String p2) {
		
		Person source = g.members[g.map.get(p1)];
		Person destination = g.members[g.map.get(p2)];
		
		ArrayList<String> shortestPath = new ArrayList<String>();
		
		Queue<Person> queue = new LinkedList<Person>();
		boolean[] visited = new boolean[g.members.length];
		int[] previous = new int[g.members.length];
		for (int i = 0; i < previous.length; i++) {
			previous[i] = -1;
		}
		boolean hasPath = false;
		
		queue.add(source);
		visited[g.map.get(source.name)] = true;
		
		while (!queue.isEmpty()) {
			Person currentPerson = queue.remove();
			if (currentPerson == destination) {
				hasPath = true;
				break;
			}
			for (Friend friend = currentPerson.first; friend != null; friend = friend.next) {
				int i = friend.fnum;
				if (!visited[i]) {
					queue.add(g.members[i]);
					visited[i] = true;
					previous[i] = g.map.get(currentPerson.name);
				}
			}
		}
		
		if (hasPath) {
			Stack<Person> stack = new Stack<Person>();
			stack.push(destination);
			int i = previous[g.map.get(destination.name)];
			while (i != -1) {
				stack.push(g.members[i]);
				i = previous[i];
			}
			while (!stack.isEmpty()) {
				String name = stack.pop().name;
				shortestPath.add(name);
			}
		}
		
		return shortestPath;
		
	}
	
	/**
	 * Finds all cliques of students at a given school.
	 * 
	 * @param g Friendship graph
	 * @param school School
	 * @return All cliques at school, null if no student attends school
	 */
	public static ArrayList<ArrayList<String>> findAllCliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> allCliques = new ArrayList<ArrayList<String>>();
		
		boolean[] visited = new boolean[g.members.length];
		
		for (int i = 0; i < visited.length; i++) {
			if (!visited[i]) {
				visited[i] = true;
				if (g.members[i].school != null) {
					if (g.members[i].school.equals(school)) {
						ArrayList<String> clique = new ArrayList<String>();
						findOneClique(g, i, visited, school, clique);
						allCliques.add(clique);
					}
				}
			}
		}
		
		return allCliques;
		
	}
	
	/**
	 * Finds one clique of students at a given school using breadth-first search.
	 * 
	 * @param g Friendship graph
	 * @param i Index of person in graph array
	 * @param visited Boolean array that indicates whether vertex in graph has been visited
	 * @param school School
	 * @param clique One clique at school
	 */
	private static void findOneClique(Graph g, int i, boolean[] visited, String school, ArrayList<String> clique) {
		
		Queue<Person> queue = new LinkedList<Person>();
		
		queue.add(g.members[i]);
		
		while (!queue.isEmpty()) {
			Person currentPerson = queue.remove();
			if (!clique.contains(currentPerson.name)) {
				clique.add(currentPerson.name);
			}
			for (Friend friend = currentPerson.first; friend != null; friend = friend.next) {
				int j = friend.fnum;
				if (!visited[j]) {
					Person currentFriend = g.members[j];
					visited[j] = true;
					if (currentFriend.school != null) {
						if (currentFriend.school.equals(school)) {
							queue.add(currentFriend);
							if (!clique.contains(currentFriend.name)) {
								clique.add(currentFriend.name);
							}
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Finds all connectors.
	 * 
	 * @param g Friendship graph
	 * @return All connectors, null if no connectors exist
	 */
	public static ArrayList<String> findAllConnectors(Graph g) {
		
		ArrayList<String> connectors = new ArrayList<String>();
		
		int length = g.members.length;
		
		boolean[] visited = new boolean[length];
		int[] numDFS = new int[length];
		int[] numBackEdge = new int[length];
		
		for (int i = 0; i < visited.length; i++) {
			if (!visited[i]) {
				findAllConnectors(g, i, i, i, visited, numDFS, numBackEdge, connectors);
			}
		}
		
		return connectors;
		
	}
	
	/**
	 * Finds all connectors using depth-first search.
	 * 
	 * @param g Friendship graph
	 * @param previous Index of previous person in graph array
	 * @param current Index of current person in graph array
	 * @param source Source of depth-first search
	 * @param visited Boolean array that indicates whether vertex in graph has been visited
	 * @param numDFS Integer array that stores order in which vertex in graph was visited
	 * @param numBackEdge Integer array that stores back edge of vertex
	 * @param connectors All connectors
	 */
	private static void findAllConnectors(Graph g, int source, int previous, int current, boolean[] visited, int[] numDFS, int[] numBackEdge, ArrayList<String> connectors) {
		
		Person currentPerson = g.members[current];
		
		visited[current] = true;
		numDFS[current] = numDFS[previous] + 1;
		numBackEdge[current] = numDFS[current];
		
		for (Friend friend = currentPerson.first; friend != null; friend = friend.next) {
			int next = friend.fnum;
			if (!visited[next]) {
				findAllConnectors(g, source, current, next, visited, numDFS, numBackEdge, connectors);
				if (numDFS[current] > numBackEdge[next]) {
					numBackEdge[current] = Integer.min(numBackEdge[current], numBackEdge[next]);
				} else {
					if (current != source || friend != currentPerson.first) {
						if (!connectors.contains(currentPerson.name)) {
							connectors.add(currentPerson.name);
						}
					}
				}
			} else {
				numBackEdge[current] = Integer.min(numBackEdge[current], numDFS[next]);
			}
		}
		
	}
	
}