package friends;

import java.util.*;

/**
 * Graph implements a friendship graph.
 * 
 * @author Data Structures Fall 2020
 * @author Cynthia Zhu
 */

class Person {
	String name;
	boolean student;
	String school;
	Friend first;
}

class Friend {
	int fnum;
	Friend next;
	Friend(int fnum, Friend next) {
		this.fnum = fnum;
		this.next = next;
	}
}

public class Graph {
	
	Person[] members;
	HashMap<String,Integer> map;
	
	public Graph(Scanner sc) {
		
		int n = Integer.parseInt(sc.nextLine());
		members = new Person[n];
		map = new HashMap<String,Integer>(n * 2);
		
		for (int i = 0; i < n; i++) {
			
			String line = sc.nextLine();
			StringTokenizer st = new StringTokenizer(line, "|");
			
			Person person = new Person();
			person.name = st.nextToken();
			String yn = st.nextToken();
			if (yn.equals("y")) {
				person.student = true;
				person.school = st.nextToken();
			}
			
			members[i] = person;
			map.put(person.name, i);
			
		}
		
		while (sc.hasNextLine()) {
			
			String line = sc.nextLine();
			StringTokenizer st = new StringTokenizer(line, "|");
			
			String p1 = st.nextToken();
			String p2 = st.nextToken();
			int i = map.get(p1);
			int j = map.get(p2);
			
			members[i].first = new Friend(j, members[i].first);
			members[j].first = new Friend(i, members[j].first);
			
		}
		
	}
	
}