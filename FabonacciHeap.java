import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public final class FabonacciHeap {

	/**
	 * @author mugdha
	 *
	 * @param
	 */

	public static final class DataEntry {
		private int degree = 0; // Number of children
		private boolean isMarked = false; // Whether this node is marked . used for cascading cut

		private DataEntry next; // Next and previous elements in the list pointer to next and previous node
		private DataEntry prev;

		private DataEntry parent; // Parent in the tree, if any.

		private DataEntry child; // Child node, if any.

		private int elem; // Element being stored here
		private int priority; // Its priority thst is the cost priority is the cost

		private DataEntry(int e, int p) {
			next = prev = this;
			elem = e;
			priority = p;
		}

		public int getDegree() {
			return degree;
		}

		public void setDegree(int degree) {
			this.degree = degree;
		}

		public boolean isMarked() {
			return isMarked;
		}

		public void setMarked(boolean isMarked) {
			this.isMarked = isMarked;
		}

		public DataEntry getNext() {
			return next;
		}

		public void setNext(DataEntry next) {
			this.next = next;
		}

		public DataEntry getPrev() {
			return prev;
		}

		public void setPrev(DataEntry prev) {
			this.prev = prev;
		}

		public DataEntry getParent() {
			return parent;
		}

		public void setParent(DataEntry parent) {
			this.parent = parent;
		}

		public DataEntry getChild() {
			return child;
		}

		public void setChild(DataEntry child) {
			this.child = child;
		}

		public int getElem() {
			return elem;
		}

		public void setElem(int elem) {
			this.elem = elem;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

	}

	/* Pointer to the minimum element in the heap. */
	private DataEntry minNodePointer = null;

	/* Cached size of the heap, so we don't have to recompute this explicitly. */
	private int sizeOfHeap = 0;

	/**
	 * Inserts the specified element into the Fibonacci heap with the specified
	 * priority.
	 * 
	 * @param val
	 *            The value to insert.
	 * @param prio
	 *            Its priority, which must be valid.
	 * @return An DataEntry representing that element in the tree.
	 */
	public DataEntry insertElem(int val, int prio) {

		/*
		 * Create the entry object, which is a circularly-linked list of length
		 * one.
		 */
		DataEntry result = new DataEntry(val, prio);

		/* Merge this singleton list with the tree list. */
		minNodePointer = mergeTwoLists(minNodePointer, result);

		/* Increase the size of the heap; we just added something. */
		++sizeOfHeap;

		/* Return the reference to the new element. */
		return result;
	}

	// returns if heap is empty
	public boolean isEmpty() {
		return minNodePointer == null;
	}

	public DataEntry min() {
		if (isEmpty())
			throw new NoSuchElementException("Fibonacci Heap is empty.");
		return minNodePointer;
	}

	public int size() {
		// get size of heap
		return sizeOfHeap;
	}

	/**
	 * given two pointers to disjoint circularly- linked lists, merges the two
	 * lists together into one circularly-linked list
	 * 
	 * @param one
	 *            A pointer into one of the two linked lists.
	 * @param two
	 *            A pointer into the other of the two linked lists.
	 * @return A pointer to the smallest element of the resulting list.
	 */
	private static DataEntry mergeTwoLists(DataEntry d1, DataEntry d2) {
		if (d1 == null && d2 == null)
			return null;
		else if (d1 == null && d2 != null)
			return d2;
		else if (d1 != null && d2 == null)
			return d1;
		else {
			DataEntry temp = d1.next;
			d1.next = d2.next;
			d1.next.prev = d1;
			d2.next = temp;
			d2.next.prev = d2;
			return d1.priority < d2.priority ? d1 : d2;
		}
	}

	public DataEntry deleteMin() {
		if (isEmpty()) {
			// delet min element
			throw new NoSuchElementException("Heap is empty.");
		}

		--sizeOfHeap;

		DataEntry minElem = minNodePointer; // temp reference to min entry we
											// will return later

		// remove mMin from the FHeap
		if (minNodePointer.next == minNodePointer) {
			// Case one, only one entry in top level list
			minNodePointer = null;
		} else {
			// Case two, there are other entries in the list of roots, then
			// remove the Min entry
			// from the list and arbitrary set the Min.
			minNodePointer.prev.next = minNodePointer.next;
			minNodePointer.next.prev = minNodePointer.prev;
			minNodePointer = minNodePointer.next; // Arbitrary element of the
													// root list.
		}

		// Next, clear the parent fields of all of the min element's children,
		// since they're about to become roots.
		if (minElem.child != null) {
			DataEntry curr = minElem.child;
			do {
				curr.parent = null;
				curr = curr.next;
			} while (curr != minElem.child);
		}

		// Next, splice the children of the root node into the top level list,
		// then set mMin to point somewhere in that list.
		minNodePointer = mergeTwoLists(minNodePointer, minElem.child);

		// If there are no entries left, then the min entry is the only entry in
		// the FHeap, we do not need the pairwise combination
		if (minNodePointer == null)
			return minElem;

		List<DataEntry> treeTable = new LinkedList<DataEntry>(); // used to
																	// record
																	// roots
																	// with
																	// different
																	// degree
		List<DataEntry> toVisit = new LinkedList<DataEntry>(); // used to record
																// all the entry
																// in the root
																// list

		// Add all node in the top level list to the toVisit
		for (DataEntry curr = minNodePointer; toVisit.isEmpty()
				|| toVisit.get(0) != curr; curr = curr.next) {
			toVisit.add(curr);
		}

		// Traverse this list
		for (DataEntry curr : toVisit) {
			while (true) {
				// Ensure that the list is long enough to hold degree
				while (curr.degree >= treeTable.size()) {
					treeTable.add(null);
				}

				// if there is no tree with the same degree we visited before,
				// just record this node
				if (treeTable.get(curr.degree) == null) {
					treeTable.set(curr.degree, curr);
					break; // note this is the only way we can break the while
							// loop
				}

				// Otherwise, merge with what's there.
				DataEntry other = treeTable.get(curr.degree);
				treeTable.set(curr.degree, null); // Clear the slot

				// Determine which of the two trees has the smaller root
				DataEntry min = (other.priority < curr.priority) ? other : curr;
				DataEntry max = (other.priority < curr.priority) ? curr : other;

				// Break max out of the root list, then merge it into min's
				// child list.
				max.next.prev = max.prev;
				max.prev.next = max.next;
				// Make max a singleton so that we can merge it.
				max.next = max.prev = max;
				min.child = mergeTwoLists(min.child, max);

				// Re-parent max.
				max.parent = min;
				// max can now lose another child. */
				max.isMarked = false;
				// Increase min's degree
				++min.degree;

				// notice the new tree may still have some other tree with the
				// same degree
				// it is still in the while loop and check whether there will be
				// another pairwise combination
				curr = min;
			}

			// Update the global min
			if (curr.priority <= minNodePointer.priority)
				minNodePointer = curr;
		}
		return minElem;
	}

	/**
	 * Decreases the key of the specified element to a new priority. Please make
	 * sure the node is in the FHeap.
	 */
	public void decreaseKey(DataEntry node, int newPriority) {

		if (newPriority > node.priority) {
			throw new IllegalArgumentException("New priority exceeds old.");
		}

		node.priority = newPriority;
		// if the node have parent and after decreaseKey its priority is < than
		// its parent
		// we have to cut this node out and merge it to top level list
		if (node.parent != null && node.priority <= node.parent.priority) {
			cutNode(node);
		}

		if (node.priority <= minNodePointer.priority)
			minNodePointer = node;
	}

	private void cutNode(DataEntry node) {
		node.isMarked = false; // update ChildCut

		if (node.parent == null)
			return; // if the node have no parent, done

		// the node have parent, next if it has siblings, update them
		if (node.next != node) {
			node.next.prev = node.prev;
			node.prev.next = node.next;
		}

		// If the node is the one identified by its parent as its child,
		// we need to rewrite that pointer to point to some arbitrary other
		// child.
		if (node.parent.child == node) {
			// If there are any other children, pick one of them arbitrarily.
			if (node.next != node) {
				node.parent.child = node.next;
			}
			// Otherwise, there aren't any other children
			else {
				node.parent.child = null;
			}
		}

		--node.parent.degree; // entry's parent lost a child

		// merge entry into the top level list
		node.prev = node.next = node;
		minNodePointer = mergeTwoLists(minNodePointer, node);

		// recursively check entry's parent, whether cut it or update mChildCut
		if (node.parent.isMarked) {
			cutNode(node.parent);
		} else {
			node.parent.isMarked = true;
		}

		node.parent = null; // update mParent
	}

	/**
	 * Merge two FHeap to get a new FHeap
	 */
	public static FabonacciHeap merge(FabonacciHeap one, FabonacciHeap two) {
		FabonacciHeap result = new FabonacciHeap();
		result.minNodePointer = mergeTwoLists(one.minNodePointer,
				two.minNodePointer);
		result.sizeOfHeap = one.sizeOfHeap + two.sizeOfHeap;

		// clear old FHeap
		one.sizeOfHeap = two.sizeOfHeap = 0;
		one.minNodePointer = null;
		two.minNodePointer = null;

		return result;
	}
	 
}
