/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * Custom node class.
 * Also has a total variable that is used for calculating 
 * total distance or price so far in a path.
*/

public class Node<T> {

	private T data;
	private Node<T> next;
	private Node<T> prev;
	private double total;

	public Node() {
		setData(null);
		setPrev(null);
		setNext(null);
	}

	public Node(T d) {
		setData(d);
		setPrev(null);
		setNext(null);
	}

	public Node(T d, Node<T> n, Node<T> p) {
		setData(d);
		setNext(n);
		setPrev(p);
	}

	public void setTotal(double amount) {
		total = amount;
	}

	public double getTotal() {
		return total;
	}

	public boolean hasNext() {
		return (next != null);
	}

	public void setData(T val) {
		data = val;
	}

	public void setNext(Node<T> n) {
		next = n;
	}

	public void setPrev(Node<T> p) {
		prev = p;
	}

	public Node<T> getNext() {
		return next;
	}

	public Node<T> getPrev() {
		return prev;
	}

	public T getData() {
		return data;
	}

}