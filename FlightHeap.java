/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * This is a custom made heap made to be used with a Flight object.
 * Operates like a minimum PQ.
*/

public class FlightHeap {

	int next;
	private Flight[] heap;


	public FlightHeap() {
		heap = new Flight[16];
		next = 0;
	}

	public void add(Flight f) {
		//resize heap if needed
		if (next == heap.length) {
			resize();
		}

		heap[next] = f;
		floatFlight();
		next++;
	}

	public void floatFlight() {
		int parent = next / 2;
		floatRecurse(parent, next);
	}

	public void floatRecurse(int parent, int added) {
		if (parent < 0) {
			return;
		}

		Flight pFlight = heap[parent];
		Flight cFlight = heap[added];

		if (cFlight.getDistance() < pFlight.getDistance()) {
			Flight temp = heap[parent];
			heap[parent] = heap[added];
			heap[added] = temp;
			added = parent;
			parent = parent / 2;

			floatRecurse(parent, added);
		}
	}

	//Returns the Flight with the greatest priority
	public Flight getMin() {
		return heap[0];
	}

	//Removes and returns the flight with the greatest priority
	public Flight removeMin() {
		Flight min = heap[0];

		next--;

		heap[0] = heap[next];
		heap[next] = null;

		sink(0);
		return min;
	}

	//Sinks a flight to its proper position in the tree
	public void sink(int index) {
		if (index < 0 ) {
			return;
		}
		int leftIndex = index * 2;
		int rightIndex = (index * 2) + 1;
		Flight left, right;
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
		} else if (left.getDistance() <= right.getDistance()) {
			compareIndex = leftIndex;
		} else compareIndex = rightIndex;

		Flight compare = heap[compareIndex];
		Flight parent = heap[index];

		if (parent.getDistance() > compare.getDistance()) {
			Flight temp = parent;
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
		Flight[] temp = new Flight[heap.length * 2];

		for (int i = 0; i < heap.length; i++) {
			temp[i] = heap[i];
		}
		heap = temp;
	}
}