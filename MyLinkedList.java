/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * Custom linked list class.
*/

public class MyLinkedList<T extends Comparable<T>> {
	
	private Node<T> head;
	private int size;

	public MyLinkedList() {
		setHead(new Node<T>());
		size = 0;
	}

	public MyLinkedList(Node<T> h) {
		setHead(h);
		size = 1;
	}

	public void add(Node<T> node) {
		Node<T> temp = head;
		setHead(node);
		temp.setPrev(head);
		head.setNext(temp);
		size++;
	}

	public void setHead(Node<T> h) {
		head = h;
	}

	public Node<T> getHead() {
		return head;
	}

	public void decrementSize() {
		size--;
	}

	public int size() {
		return size;
	}
}