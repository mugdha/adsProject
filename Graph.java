import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

class AdjListElmnt {

	public int target;
	public int weight;

	// public boolean discovered;

	public AdjListElmnt(int target, int weight) {
		this.target = target;
		this.weight = weight;
	}

	public int getTarget() {
		return target;
	}

	public int getWeight() {
		return weight;
	}

}

@SuppressWarnings("unused")
public class Graph {

	private LinkedList<AdjListElmnt>[] lists;
	
 
	public LinkedList<AdjListElmnt>[] getLists() {
		return lists;
	}

	private int totalEdge;
	public int totalVertex;

	@SuppressWarnings("unchecked")
	public Graph(String fileName) {
		// this.lists=new ArrayList<ArrayList<Integer>>();
		//this.sourceV = source;
		//this.destinationV = desti;
		File inputFile = new File(fileName);
		Scanner sc; // use scanner to scan the input file
		try {
			sc = new Scanner(inputFile);
			if (sc.hasNextInt())
				this.totalVertex = Integer.parseInt(sc.next());
			if (sc.hasNextInt())
				this.totalEdge = Integer.parseInt(sc.next());
			lists = new LinkedList[totalVertex];
			for (int i = 0; i < totalVertex; i++)
				lists[i] = new LinkedList<AdjListElmnt>();

			while (sc.hasNextInt()) {
				Integer sr = Integer.parseInt(sc.next());// change format to
															// Integer
				Integer des = Integer.parseInt(sc.next());
				Integer weight = Integer.parseInt(sc.next());
				addEdge(sr, des, weight);

			}

		} catch (FileNotFoundException e) {// catch exception
			System.err.println("Error: " + fileName + " not found");// print out
																	// error
																	// info
		}
	}

	public int getTotalVertex() {
		return totalVertex;
	}

	public void addEdge(int source, int target, int weight) {

		AdjListElmnt newNode = new AdjListElmnt(target, weight);
		lists[source].add(newNode);
		// since its undirected do the reverser also
		AdjListElmnt newNode2 = new AdjListElmnt(source, weight);
		lists[target].add(newNode2);

	}

	public int getDistance(int src, int dest) {
		LinkedList<AdjListElmnt> ad = lists[src];
		for (Iterator<AdjListElmnt> iterator = ad.iterator(); iterator
				.hasNext();) {
			AdjListElmnt adjListElmnt = (AdjListElmnt) iterator.next();
			int target = adjListElmnt.getTarget();
			if (target == dest)
				return adjListElmnt.getWeight();
		}
		return 0;

	}
	
	
	  public LinkedList<Integer> getAdjListOfNode(int i) {
	    	
	    	// list the adjacency list of node which are still not discovered
	    	
	    	LinkedList<Integer> list=new LinkedList<>();
	    	for (Iterator<AdjListElmnt> iterator = lists[i].iterator(); iterator.hasNext();) {
	    		 Object ob=iterator.next();  
	    		  
	    			 list.add(((AdjListElmnt) ob).getTarget());	    		 
	    		 	//System.out.print( ((AdjListElmnt) ob).getTarget()+" ");	 
	    		 			
			}   
	    	return list;
	    }

}
