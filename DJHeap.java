/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * This is a custom made heap class that is used for Djikstra's algorithm
 * It operates like a minimum PQ.
*/

public class DJHeap {

	int next;
	private DJNode[] heap;


	public DJHeap() {
		heap = new DJNode[16];
		next = 0;
	}

	public void add(DJNode n) {
		//resize heap if needed
		if (next == heap.length) {
			resize();
		}

		heap[next] = n;
		floatNode();
		next++;
	}

	public void floatNode() {
		int parent = next / 2;
		floatRecurse(parent, next);
	}

	public void floatRecurse(int parent, int added) {
		if (parent < 0) {
			return;
		}

		DJNode pNode = heap[parent];
		DJNode cNode = heap[added];

		if (cNode.getTotal() < pNode.getTotal()) {
			DJNode temp = heap[parent];
			heap[parent] = heap[added];
			heap[added] = temp;
			added = parent;
			parent = parent / 2;

			floatRecurse(parent, added);
		}
	}

	public DJNode getMin() {
		return heap[0];
	}

	public DJNode removeMin() {
		DJNode min = heap[0];

		next--;

		heap[0] = heap[next];
		heap[next] = null;

		sink(0);
		return min;
	}

	public void sink(int index) {
		if (index < 0 ) {
			return;
		}
		int leftIndex = index * 2;
		int rightIndex = (index * 2) + 1;
		DJNode left, right;
		int compareIndex;

		if (leftIndex > heap.length - 1){
			left = null;
		} else left = heap[leftIndex];

		if (rightIndex > heap.length - 1) {
			right = null;
		} else right = heap[rightIndex];

		if (right == null && left == null) {
			return;
		} else if (left != null && right == null) {
			compareIndex = leftIndex;
		} else if (left == null && right != null) {
			compareIndex = rightIndex;
		} else if (left.getTotal() <= right.getTotal()) {
			compareIndex = leftIndex;
		} else compareIndex = rightIndex;

		DJNode compare = heap[compareIndex];
		DJNode parent = heap[index];

		if (parent.getTotal() > compare.getTotal()) {
			DJNode temp = parent;
			heap[index] = compare;
			heap[compareIndex] = temp;

			sink(compareIndex);
		}
	}

	public boolean isEmpty() {
		return (heap[0] == null);
	}

	//Resizes the heap
	public void resize() {
		DJNode[] temp = new DJNode[heap.length * 2];

		for (int i = 0; i < heap.length; i++) {
			temp[i] = heap[i];
		}
		heap = temp;
	}
}