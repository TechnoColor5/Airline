/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * Holds all of the necessary information for each flight.
*/

public class Flight implements Comparable<Flight>{

	private int origin, destination, distance;
	private double price;
	private boolean reversed;

	public Flight(int orig, int dest, int dist, double cost, boolean reverse) {
		setOrigin(orig);
		setDest(dest);
		setDistance(dist);
		setPrice(cost);
		reversed = reverse;
	}

	public int compareTo(Flight f) {
		if (origin == f.getOrigin() && destination == f.getDest()){
			return 1;
		}
		return 0;
	}

	public void setOrigin(int orig) 	{  origin = orig;  }

	public void setDest(int dest) 		{  destination = dest;  }

	public void setDistance(int dist) {  distance = dist;  }

	public void setPrice(double cost) {  price = cost;  }

	public int getOrigin() 		{  return origin;  }

	public int getDest() 			{  return destination;  }

	public int getDistance() 	{ return distance;  }

	public double getPrice() 			{  return price;  }

	public boolean getReverse()  {  return reversed;  }
}