/* Written By Daniel Mailloux
 * Last Updated: July 27, 2018
 * 
 * This is a class that represents a trip with multiple flights.
 * Keeps a running total of the cost of the flight.
*/

public class Trip{
	
	private boolean[] visited;
	private double totalCost;
	private MyQueue<Flight> flights;
	private int hops;

	public Trip(int numCities) {
		totalCost = 0;
		visited = new boolean[numCities];
		hops = 0;
	}

	public void addCost(double amount) {
		totalCost += amount;
	}

	public double getCost() {
		return totalCost;
	}

	public void addFlight(Flight f) {
		if (hops == 0) {
			flights = new MyQueue<Flight>(f);
		} else {
			flights.add(f);
		}
		hops++;
	}

	public Flight removeTail() {
		hops--;
		return flights.removeTail().getData();
	}

	public boolean getVisited(int index) {
		return visited[index];
	}

	public int getHops() {
		return hops;
	}

	public void setVisited(int index) {
		visited[index] = true;
	}

	public void setNotVisited(int index) {
		visited[index] = false;
	}

	public MyQueue<Flight> getFlights() {
		return flights;
	}

}