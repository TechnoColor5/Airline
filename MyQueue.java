/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * Custom Queue class.
*/

public class MyQueue<T extends Comparable<T>> {
	private Node<T> head, tail;
	private int size;

	public MyQueue() {
		Node<T> n = new Node<T>();
		setHead(n);
		setTail(n);
	}

	public MyQueue(T obj) {
		Node<T> n = new Node<T>(obj);
		setHead(n);
		setTail(n);
		size = 1;
	}

	public void setHead(Node<T> n) {
		head = n;
	}

	public Node<T> getHead() {
		return head;
	}

	public void setTail(Node<T> n) {
		tail = n;
	}

	public Node<T> getTail() {
		return tail;
	}

	public void add(T obj) {
		Node<T> n = new Node<T>(obj);
		if (size == 0) {
			setHead(n);
			setTail(n);
		} else {
			tail.setNext(n);
			n.setPrev(tail);
			setTail(n);
		}

		size++;
	}

	public Node<T> remove() {
		Node<T> temp = head;
		if (size == 1) {
			setHead(null);
		} else {
			setHead(head.getNext());
		}
		size--;
		return temp;
	}

	public Node<T> removeTail() {
		Node<T> temp = tail;
		Node<T> prev = tail.getPrev();
		if (size == 1) {
			setTail(null);
			setHead(null);
		} else {
			setTail(prev);
			prev.setNext(null);
		}

		size--;
		return temp;
	}

	public int getSize() {
		return size;
	}
}