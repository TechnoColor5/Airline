/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * This is a custom node made to be used with DJHeap
*/

public class DJNode {

	private int index, prevCity;
	private double total;

	public DJNode() {
		setIndex(-1);
		setPrevCity(-1);
	}

	public DJNode(int i) {
		setIndex(i);
		setPrevCity(-1);
		setTotal(Double.POSITIVE_INFINITY);
	}

	public void setTotal(double amount) {
		total = amount;
	}

	public void setIndex(int i) {
		index = i;
	}

	public void setPrevCity(int i) {
		prevCity = i;
	}

	public int getPrevCity() {
		return prevCity;
	}

	public int getIndex() {
		return index;
	}

	public double getTotal() {
		return total;
	}
}