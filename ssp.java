import java.util.Iterator;
import java.util.LinkedList;

public class ssp {

	public static void main(String[] args) {

		if (args.length == 3) {
			String fileName = args[0];
			int sVertx = Integer.parseInt(args[1]);
			int dVertex = Integer.parseInt(args[2]);
			Graph g = new Graph(fileName);
			int totalVertex=g.totalVertex;
			if((sVertx>=0 && sVertx<totalVertex) && (dVertex>=0 && dVertex<totalVertex))
			{
				sspCal(g, sVertx, dVertex);
			}
			else
			{
				System.out.println("Provided source vertex or destination vertex is incorrect");
			}
			 
		} else {
			System.out
					.println("provide input in format:ssp file_name source_node destination_node");
		}

	}

	private static String sspCal(Graph g, int sVertx, int dVertex) {
		// sVertx is the source vertex, dVertex is destination vertex
		
		String finalOutput="";
		
		int totalVertex = g.getTotalVertex();
		LinkedList<AdjListElmnt>[] list = g.getLists();
		FabonacciHeap fh = new FabonacciHeap();
		FabonacciHeap.DataEntry[] dist = new FabonacciHeap.DataEntry[totalVertex];// distance
																					// is
																					// stored
																					// in
																					// the
																					// array
																					// of
																					// entry
		int[] prev = new int[totalVertex];		
		 
		boolean[] discovered = new boolean[totalVertex];

		LinkedList<Integer> adjacentNode = new LinkedList<>();
		// initialize it for the first time and make all the distance to
		// max_value
		for (int i = 0; i < totalVertex; i++) {
			dist[i] = fh.insertElem(i, Integer.MAX_VALUE);
			// notice that enqueue will return a reference to the entry we
			// inserted
		}
		fh.decreaseKey(dist[sVertx], 0); // decrease the distance to source node
											// to 0
		discovered[sVertx] = true;
		while (!fh.isEmpty()) {
			FabonacciHeap.DataEntry curr = fh.deleteMin();
			int min = curr.getElem();
			int cost = curr.getPriority();
			discovered[min] = true;
			if(min==dVertex)
			{
				 
				int i=min;
				StringBuffer sb=new StringBuffer();
				sb=sb.insert(0, i);	
				
				while(i!=sVertx)
				{
					i=prev[i];
					sb=sb.insert(0, " ");
					sb=sb.insert(0, i);					
					
					
				}			
				 
				finalOutput=cost+"\n"+sb.toString();
				System.out.println(finalOutput);
				return finalOutput;
				 				 
			}
			adjacentNode = g.getAdjListOfNode(min);
			for (Iterator<Integer> iterator = adjacentNode.iterator(); iterator
					.hasNext();) {
				Integer iNode = (Integer) iterator.next();
				int weight = g.getDistance(min, iNode);
				int distance = dist[iNode].getPriority(); // dist is the current
															// shortest path to
															// the node adjacent
															// to [min]

				if (!discovered[iNode] && distance > cost + weight) {
					prev[iNode]=min;
					fh.decreaseKey(dist[iNode], cost + weight);
					
				}

			}

		}
		return finalOutput;
		
	}

}
