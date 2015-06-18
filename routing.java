import java.io.File;
import java.io.FileNotFoundException; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class routing {

	public static HashMap<Integer, String> vertexBInaryMap = new HashMap<Integer, String>();
	public static List<BinaryTrie> binaryTriesNodeList = new LinkedList<BinaryTrie>();

	public static void main(String[] args) {
		if (args.length == 4) {

			int sVertex = Integer.parseInt(args[2]);
			int dVertex = Integer.parseInt(args[3]);
			Graph g = new Graph(args[0]);
			createHashMap(args[1]);
			int totalVertex = g.getTotalVertex();
			if((sVertex>=0 && sVertex<totalVertex) && (dVertex>=0 && dVertex<totalVertex))
			{

			BinaryTrie b1;
			binaryTriesNodeList.clear();
			for (int i = 0; i < g.totalVertex; i++) {
			 
				b1 = new BinaryTrie();

				for (int j = 0; j < g.totalVertex; j++) {
					if (i != j) {

						String tempResult = ssp(g, i, j);
						String res[] = tempResult.split("\n");
						String[] path = res[1].split(" ");
						String binaryIP = vertexBInaryMap.get(j);
						int nextNode = Integer.parseInt(path[1]);
						b1.addNode(binaryIP, nextNode);
					}
				}
				b1.postOrderConcatenate();
				binaryTriesNodeList.add(b1);
			}

			String res = ssp(g, sVertex, dVertex);
			String result[] = res.split("\n");
		 
			System.out.println(result[0]);
			 
			String path = "";
			String destIp = vertexBInaryMap.get(dVertex);
			BinaryTrie temp = binaryTriesNodeList.get(sVertex);
			String longPrefix = temp.longestPrefixMatch(destIp);
			int nodeValue = temp.get(longPrefix);
			path += longPrefix;
			while (nodeValue != dVertex) {

				temp = binaryTriesNodeList.get(nodeValue);
				longPrefix = temp.longestPrefixMatch(destIp);
				nodeValue = temp.get(longPrefix);
				path += " " + longPrefix;
			}
			System.out.println(path);
			}
			else
			{
				System.out.println("Provided source vertex or destination vertex is incorrect");
			}

		} else {
			System.out
					.println("provide input in format:routing file_name_1 file_name_2 source_node destination_node");
		}
	}

	private static void createHashMap(String ipFile) {

		File inputFile = new File(ipFile);
		Scanner sc; // use scanner to scan the input file
		try {
			sc = new Scanner(inputFile);
			int vertextNo = 0;
			while (sc.hasNext()) {
				String nextVal = sc.next();
				String[] partStrings = nextVal.split("\\.");
				String binaryString = "";
				for (int i = 0; i < partStrings.length; i++) {
					String number = Integer.toBinaryString(Integer
							.parseInt(partStrings[i]));
					if (number.length() < 8) {
						number = new String((new char[8 - number.length()]))
								.replace('\0', '0') + number;
					}
					binaryString += number;
				}
				vertexBInaryMap.put(vertextNo, binaryString);
				vertextNo++;

			}

		} catch (FileNotFoundException e) {// catch exception
			System.err.println("Error: " + ipFile + " not found");// print out
																	// error
																	// info
		}
	}

	private static String ssp(Graph g, int sVertx, int dVertex) {
		// sVertx is the source vertex, dVertex is destination vertex

		String finalOutput = "";

		int totalVertex = g.getTotalVertex();
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
			if (min == dVertex) {

				int i = min;
				StringBuffer sb = new StringBuffer();
				sb = sb.insert(0, i);

				while (i != sVertx) {
					i = prev[i];
					sb = sb.insert(0, " ");
					sb = sb.insert(0, i);

				}

				finalOutput = cost + "\n" + sb.toString();
				// System.out.println(finalOutput);
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
					prev[iNode] = min;
					fh.decreaseKey(dist[iNode], cost + weight);

				}

			}

		}
		return finalOutput;

	}
}
