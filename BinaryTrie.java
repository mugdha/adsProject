 
public class BinaryTrie {

	private class Node {
		private int value; // equal to vertex number
		public Node leftNode, rightNode;

		public Node() {
			this.value = -1;
			this.leftNode = null;
			this.rightNode = null;
		}
	}

	public BinaryTrie() {

	}

	private Node root = null;

	public void postOrderConcatenate() {

		postOrderConcatenate(this.root);
	}

	private void postOrderConcatenate(Node head) {

		if (head == null) {
			return;
		}
		if (head.leftNode != null)
			postOrderConcatenate(head.leftNode);
		if (head.rightNode != null)
			postOrderConcatenate(head.rightNode);
		if (head.rightNode != null && head.leftNode != null) {

			if (head.rightNode.value == head.leftNode.value
					&& head.rightNode.value != -1) {
				int temp = head.rightNode.value;
				head.value = temp;
				head.rightNode = null;
				head.leftNode = null;
			}
		}
		if ((head.leftNode != null) && head.rightNode == null) {
			if (head.leftNode.value != -1) {
				head.value = head.leftNode.value;
				head.leftNode = null;
			}
		}

		if ((head.rightNode != null) && head.leftNode == null) {
			if (head.rightNode.value != -1) {
				head.value = head.rightNode.value;
				head.rightNode = null;
			}
		}
	}

	public void addNode(String ip, int vertex) {
		root = addNode(root, ip, vertex, 0);
	}

	private Node addNode(Node head, String ip, int vertex, int level) {

		if (head == null)
			head = new Node();

		if (level == ip.length()) {
			head.value = vertex;
			return head;
		}

		int val = ip.charAt(level)-48;
	 
		if (val== 0) {

			head.leftNode = addNode(head.leftNode, ip, vertex, level + 1);
		}

		else {
			head.rightNode = addNode(head.rightNode, ip, vertex, level + 1);
		}
		return head;
	}

	public String longestPrefixMatch(String binaryIP) {

		int length = longestPrefix(root, binaryIP, 0, 0);
		return binaryIP.substring(0, length);
	}

	private int longestPrefix(Node head, String ip, int pos, int len) {

		if (head == null)
			return len;

		if (head.value != -1)
			len = pos;

		if (pos == ip.length())
			return len;

		int val = ip.charAt(pos)-48;
		 
		if (val == 0)
			return longestPrefix(head.leftNode, ip, pos + 1, len);
		else
			return longestPrefix(head.rightNode, ip, pos + 1, len);
	}
 
	public int get(String key) {

		Node head = get(root, key, 0);
		if (head == null)
			return 0;
		return head.value;
	}

	private Node get(Node head, String key, int level) {

		if (head == null)
			return null;
		if (level == key.length())
			return head;
		int val = key.charAt(level) - 48;
		if (val == 0)
			return get(head.leftNode, key, level + 1);
		else
			return get(head.rightNode, key, level + 1);
	}

}
