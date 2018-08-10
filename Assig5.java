/**
 * @author Daniel Mailloux
 * @version v1.0
 *
 * Simulates a portal for a fake airline.
 * Is able to grab routes between cities under different parameters.
*/

import java.util.Scanner;
import java.io.*;

public class Assig5 {

	private static Scanner input = new Scanner(System.in);
	private static Scanner fileScan;
	private static File file;
	private static String fileName;
	private static int numCities = 0;
	private static String[] cities;					//vertices
	private static MyLinkedList [] routes;	//edges
	private static int tripCounter = 0;
	private static boolean modification = false;
	private static boolean disconnect = false;

	public static void main (String [] args) throws IOException {
		System.out.println("\n\t!========================================!");
		System.out.println("\n\t\tWelcome to Oceanic Airlines\n");
  	System.out.print("\t\t  	  __|__\n" +
									   "\t\t    *---o--(_)--o---*\n");
  	System.out.println("\n\t\t  No Crashes since 2004!");
		System.out.println("\n\t!========================================!\n");
		System.out.print("Please enter the file name: ");

		//gets file from user
		try {
			fileName = input.nextLine();
		System.out.println();

			file = new File(fileName);
			fileScan = new Scanner(file);

		} catch (FileNotFoundException exc) {
			System.out.println("That is an incorrect file");
			System.exit(0);
		}

		initGraph();
		fileScan.close();

		while (true) {
		showMenu();
			String userChoice = input.nextLine();
			System.out.println();

			switch (userChoice) {
				case "1":
					outputFlights();
					break;
				case "2":
					getMST();
					break;
				case "3":
					getShortestPath(true);
					break;
				case "4":
					getShortestPath(false);
					break;
				case "5":
					fewestHops();
					break;
				case "6":
					flightsUnderCost();
					break;
				case "7":
					addNewFlight();
					break;
				case "8":
					removeFlight();
					break;
				case "9":
					quitProgram();
					break;
			}
		}
	}

	/* Initializes the graph
	*/
	public static void initGraph() {
		String nextLine = fileScan.nextLine();

		numCities = Integer.parseInt(nextLine);
		cities = new String[numCities];
		routes = new MyLinkedList[numCities];

		for (int i = 0; i < numCities; i++) {
			cities[i] = fileScan.nextLine();
		}

		while (fileScan.hasNextLine()) {
			try {
			int origin = fileScan.nextInt();
			int dest = fileScan.nextInt();
			int dist = fileScan.nextInt();
			double price = fileScan.nextDouble();

			Flight flight = new Flight(origin, dest, dist, price, false);
			addFlight(origin, flight);
			flight = new Flight(dest, origin, dist, price, true);
			addFlight(dest, flight);
			} catch (Exception  ex) { return;}

		}
	}

	/* Adds a flight to the correct LinkedList
	 * @param origin : origin city
	 * @param f : Flight to be added
	*/
	public static void addFlight(int origin, Flight f) {
		if (routes[origin - 1] == null ) { //if no linkedlist exists, create one
			routes[origin - 1] = new MyLinkedList<Flight>();
		}
		Node<Flight> node = new Node<Flight>(f);
		routes[origin - 1].add(node);
	}
	/* Displays menu
	*/
	public static void showMenu() {
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t+--------------------+");
		System.out.println("\t|\tMenu\t     |");
		System.out.println("\t+--------------------+");
		System.out.print("\t1.Display all flights\n" +
										 "\t2.Display MST\n" +
										 "\t3.Shortest route\n" +
										 "\t4.Lowest priced routes\n" +
										 "\t5.Route with least hops\n" + 
										 "\t6.Search for trips under a set price\n" +
										 "\t7.Add new route\n" +
										 "\t8.Remove a route\n" +
										 "\t9.Quit\n" +
										 "\t----------------------\n" +
										 "Enter your choice: ");
	}

	/* Outputs all the flights
	*/
	public static void outputFlights() {
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t\t+--------------------+");
		System.out.println("\t\t|     Flight List    |");
		System.out.println("\t\t+--------------------+");
		for (int i = 0; i < routes.length; i++) {
			MyLinkedList list = routes[i];
			if (list == null) continue;
			Node<Flight> n = list.getHead();
			while (true) {
				Flight f = n.getData();

				if (f == null) break;
				if (!f.getReverse()) {
					System.out.println(getFlightString(f));
				}
				
				n = n.getNext();		
			}
		}
		System.out.println();
	}

	/* Makes a string for the flight
	 * @param f : flight to change into a string
	 * @return a proper string that has all of the details of a flight
	*/
	public static String getFlightString(Flight f) {
		StringBuilder str = new StringBuilder("");
		String origin = cities[f.getOrigin() - 1];
		String dest = cities[f.getDest() - 1];
		str.append(origin + " <--to--> " + dest + " | Distance: " + f.getDistance() + " | Price: $" + f.getPrice());
		return str.toString();
	}

	/* Creates a Minimum Spanning Tree 
	 * If there is a disconnect, it will say so.
	*/
	public static void getMST() {
		System.out.println("-------------------------------------------------------------------\n");
		System.out.println("\t+-----------------------------+");
		System.out.println("\t|    Minimum Spanning Tree    |");
		System.out.println("\t+-----------------------------+");
		Flight []mst = findMST();

		disconnect = false;

		for (int i = 0; i < mst.length; i++) {
			Flight f = mst[i];

			if (f == null) {
				disconnect = true;
				continue;
			}
			System.out.println(getFlightString(f));
		}

		if (disconnect) {
			System.out.println("A gap exists in the MST");
		}
		System.out.println("\n");
	}

	/* Does the actual work of finding the MST
	 * @return an array fo flights as the MST
	*/
	public static Flight[] findMST() {
		Flight mst[] = new Flight[numCities - 1];

		boolean visited[] = new boolean[numCities];
		FlightHeap minPQ = new FlightHeap();
		int numVisited = 1;
		//int startIndex = 0;

		for (boolean b: visited){
			b = false;
		}

		visited[0] = true;
		Node<Flight> node = routes[0].getHead();

		minPQ = getNeighbors(minPQ, node);

		while (numVisited < numCities) {
			if (minPQ.isEmpty()) {
				break;
			}

			Flight min = minPQ.removeMin();
			int destination = min.getDest();

			if (!visited[destination - 1]) {
				mst[numVisited - 1] = min;
				visited[destination - 1] = true;
				numVisited++;

				node = routes[destination - 1].getHead();
				minPQ = getNeighbors(minPQ, node);
			}
		}
		return mst;
	}

	/* Gets all the neighbors of a node and adds it to the minPQ
	 * @param minPQ : the minPQ to add the neighbors to
	 * @param n : node to use
	 * @return the minPQ
	*/
	public static FlightHeap getNeighbors(FlightHeap minPQ, Node<Flight> n) {
		while (n.getData() != null) {
			Flight f = n.getData();
			minPQ.add(f);
			n = n.getNext();
		}
		return minPQ;
	}

	/* Gets the path
	 * @param mode : if true, base off of distance, if false base off of price
	*/
	public static void getShortestPath(boolean mode) {
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t+--------------------+");
		System.out.println("\t|   Shortest Path    |");
		System.out.println("\t+--------------------+\n");
		int[] userCities = getCities();

		int originIndex = userCities[0] - 1;
		int destIndex = userCities[1] - 1;

		DJNode[] spt = shortestPath(originIndex, destIndex, mode);

		if (mode) {
			System.out.println("Shortest route from " + cities[originIndex] + " to " + cities[destIndex] + " by distance:\n");
			int currentIndex = originIndex;
			int total = 0;
			if (spt == null) {
				System.out.println("No path exists between those cities. There is a disconnect.");
				return;
			}
			while (currentIndex != destIndex) {
				DJNode n = spt[currentIndex];
				int prev = n.getPrevCity();
				if (prev < 0) {
					System.out.println("No path exists between those cities. There is a disconnect.");
					return;
				}
				int routeDistance = (int) n.getTotal() - (int) spt[prev].getTotal();
				total += routeDistance;

				System.out.println(cities[currentIndex] + " to " + cities[prev] + "\t" + routeDistance);
				currentIndex = prev;
			}
			System.out.println("\t--------");
			System.out.println("Total distance:\t"+ total);
			System.out.println();
		} else {
			System.out.println("Shortest route from " + cities[originIndex] + " to " + cities[destIndex] + " by cost:\n");
			int currentIndex = originIndex;
			double total = 0;
			if (spt == null) {
				System.out.println("No path exists between those cities. There is a disconnect.");
				return;
			}
			while (currentIndex != destIndex) {
				DJNode n = spt[currentIndex];
				int prev = n.getPrevCity();
				if (prev < 0) {
					System.out.println("No path exists between those cities. There is a disconnect.");
					return;
				}
				double routeCost = n.getTotal() - spt[prev].getTotal();
				total += routeCost;

				System.out.println(cities[currentIndex] + " to " + cities[prev] + "\t" + routeCost);
				currentIndex = prev;
			}
			System.out.println("\t--------");
			System.out.println("Total cost:\t"+ total);
			System.out.println();
		}
	}

	/* Gets the index of the city
	 * @param str : string of city
	 * @return index of city in array. If -1, the string was not found
	*/
	public static int indexOf(String str) {
		for (int i = 1; i <= cities.length; i++) {
			if (cities[i - 1].equals(str)) {
				return i;
			}
		}
		return -1;
	}

	/* Does the work of getting the shortest path
	 * @param origin : index of origin city
	 * @param destination : index of destination city
	 * @param mode : cost/distance
	 * @return array of nodes representing the flights
	*/
	public static DJNode[] shortestPath(int origin, int destination, boolean mode) {
		DJNode[] spt = new DJNode[numCities];
		DJHeap heap;
		boolean visited[] = new boolean[numCities];
		int current = destination;

		for (int i = 0; i < numCities; i++) {
			spt[i] = new DJNode(i);
			visited[i] = false;
		}

		spt[destination].setTotal(0);

		if (routes[current] == null) {
			return null;
		}

		while (current != origin) {
			Node<Flight> currNode = routes[current].getHead();
			heap = new DJHeap();		//Resets the heap

			while (currNode.getData() != null) {
				Flight f = currNode.getData();
				int destIndex = f.getDest();

				if (!visited[destIndex - 1]) {
					double total;

					if (mode) {
						total = f.getDistance() + spt[current].getTotal();
					} else {
						total = f.getPrice() + spt[current].getTotal();
					}

					if (total < spt[destIndex - 1].getTotal()) {
						spt[destIndex - 1].setTotal(total);
						spt[destIndex - 1].setPrevCity(current);
					}
				}
				currNode = currNode.getNext();
			}
			visited[current] = true;

			for (int i = 0; i < numCities; i++) {
				if (!visited[i]){
					heap.add(spt[i]);
				}
			}

			if (heap.isEmpty()) {
				return null;
			}

			DJNode min = heap.removeMin();
			current = min.getIndex();
		}

		return spt;
	}

	/* Finds the trip with the fewest hops using a BFS
	*/
	public static void fewestHops() {
		disconnect = false;
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t+--------------------+");
		System.out.println("\t|    Fewest Hops     |");
		System.out.println("\t+--------------------+\n");
		int[] userCities = getCities();

		int originIndex = userCities[0] - 1;
		int destIndex = userCities[1] - 1;

		int destinations[] = getFewestHops(originIndex, destIndex);

		if (disconnect || destinations == null) {
			System.out.println("No path exists between those cities. There is a disconnect.");
			return;
		}

		System.out.println("Fewest hops from " + cities[originIndex] + " to " + cities[destIndex] + ":\n");

		int total = 0;
		int current = originIndex;
		while (current != destIndex) {
			System.out.print(cities[current] + " to " + cities[destinations[current]] + "\n");
			total++;
			current = destinations[current];
		}
		System.out.println("Total hops: " + total + "\n");
	}

	/* Does the actual work of a BFS
	 * @param originIndex : index of origin city
	 * @param destIndex : index of destination city
	 * @retun array of cities showing which flights were used to get to where
	*/
	public static int[] getFewestHops(int originIndex, int destIndex) {
		MyQueue<Integer> queue = new MyQueue<Integer>(destIndex);
		int []destinations = new int[numCities];	//array used to track flights
		boolean visited[] = new boolean[numCities];

		//sets all of visited to false
		for(int i = 0; i < numCities; i++) {
			visited[i] = false;
		}

		int current = 0;
		visited[destIndex] = true;

		//Keeps going until we see the origin
		while (!visited[originIndex]) {
			//If the queue is empty and we have not seen the origin, there is a disconnect
			if (queue.getSize() == 0) {
				disconnect = true;
				break;
			}

			current = queue.remove().getData();
			if (routes[current] == null) {
				return null;
			}
			Node<Flight> n = routes[current].getHead();

			//iterates through all the neighbors
			while (n.getData() != null) {
				int next = n.getData().getDest() - 1;

				if (!visited[next]) {
					queue.add(next);

					//Set the means of getting to this node as the currentIndex
					destinations[next] = current;
					visited[next] = true;
				}
				n = n.getNext();
			}
		}
		return destinations;
	}

	/* Asks the user to enter an origin and destination city
	 * @return array of int, index 0 = origin, index 1 = destination
	*/
	public static int[] getCities() {
		int originIndex = -1;
		int destIndex = -1;

		while (originIndex < 0) {
			System.out.print("Enter starting city: ");
			String origin = input.nextLine();
			originIndex = indexOf(origin);
		}

		while (destIndex < 0) {
			System.out.print("Enter destination city: ");
			String destination = input.nextLine();
			destIndex = indexOf(destination);
		}
		System.out.println();


		int cities[] = new int[2];
		cities[0] = originIndex;
		cities[1] = destIndex;
		return cities;
	}

	/* Finds all the lfights under a certain amount of money
	*/
	public static void flightsUnderCost() {
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t+------------------------------------+");
		System.out.println("\t|   Flights Under a Certain Price    |");
		System.out.println("\t+------------------------------------+\n");
		System.out.print("Enter cost limit: ");
		double max;

		try {
			max = Double.parseDouble(input.nextLine());
		} catch (Exception e) {
			System.out.println("You did not enter a correct amount!\n");
			return;
		}
		System.out.println();

		tripCounter = 0;
		for (int i = 0; i < numCities; i++) {
			Trip trip = new Trip(numCities);
			flightsUnderCostRecurse(max, trip, i);
		}
	}

	/* Recursive method that finds all of the flights under a certain price
	 * @param max : max amount a flight can cost
	 * @param trip : the current trip
	 * @param currentIndex : the current index the algorithm is at
	*/
	public static void flightsUnderCostRecurse(double max, Trip trip, int currentIndex) {
		Node<Flight> node = routes[currentIndex].getHead();
		trip.setVisited(currentIndex);

		while (node.getData() != null) {
			//Get the flight and add it
			Flight f = node.getData();
			int dest = f.getDest();

			if (!trip.getVisited(dest - 1)) {
				double cost = trip.getCost() + f.getPrice();

				if (cost <= max) {
					trip.setVisited(f.getDest() - 1);
					trip.addFlight(f);
					trip.addCost(f.getPrice());
					
					//prints the valid trip
					printTrip(trip);
					//recurse
					flightsUnderCostRecurse(max, trip, f.getDest() - 1);

					//backtrack
					Flight removed = trip.removeTail();
					trip.addCost(removed.getPrice() * -1);
					trip.setNotVisited(removed.getDest() - 1);
				}
			} 

			node = node.getNext();
		}
		trip.setNotVisited(currentIndex);
	}

	/* Prints the current trip
	 * @param trip : trip to be prined out
	*/
	public static void printTrip(Trip trip) {
		tripCounter++;
		MyQueue<Flight> flights = trip.getFlights();
		System.out.println("Trip #" + tripCounter + " Total cost: " + trip.getCost());

		Node<Flight> node = flights.getHead();
		//if (node == null) return;

		int i = 0;
		while (node != null) {
			Flight f = node.getData();

			if (i == trip.getHops() - 1) {
				System.out.print(cities[f.getOrigin() - 1] + " " + f.getPrice() + " " + cities[f.getDest() - 1] + "\n");
			} else {
				System.out.print(cities[f.getOrigin() - 1] + " " + f.getPrice() + " ");
			} 
			node = node.getNext();
			i++;
		}
		System.out.println("-----\n");
	}

	/* Adds a user-inputted flight
	*/
	public static void addNewFlight() {
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t+-----------------------+");
		System.out.println("\t|   Add a new Flight    |");
		System.out.println("\t+-----------------------+\n");
		int originIndex = -1;
		int destIndex = -1;
		String destination, origin;
		while (originIndex < 0) {
			System.out.print("Enter origin: ");
			origin = input.nextLine();
			originIndex = indexOf(origin);
			if (originIndex < 0) {
				System.out.println("Invalid City");
			}
			System.out.println();
		}

		while (destIndex < 0) {
			System.out.print("Enter destination: ");
			destination = input.nextLine();
			destIndex = indexOf(destination);			
			if (destIndex < 0) {
				System.out.println("Invalid City");
			}
			System.out.println();
		}

		boolean good = false;
		int distance = 0;
		while (!good) {
			good = true;
			System.out.print("Enter distance: ");
			try {
				distance = Integer.parseInt(input.nextLine());
			} catch (Exception e) {
				good = false;
				System.out.println("You did not enter a correct amount\n");
			}	
		}
		System.out.println();

		System.out.print("Enter price: ");
		good = false;
		Double price = 0.0;
		while (!good) {
			good = true;
			try {
				price = Double.parseDouble(input.nextLine());
			} catch (Exception e) {
				good = false;
				System.out.println("You did not enter a correct amount\n");
			}
		}

		Flight flight = new Flight(originIndex, destIndex, distance, price, false);

		boolean exists = alreadyExists(flight);
		if (exists) { //If the flight exists, does not add it
			System.out.println("A flight between those two cities already exists");
			return;
		}

		//Adds two flights (1 reversed and 1 not)
		addFlight(originIndex, flight);
		flight = new Flight(destIndex, originIndex, distance, price, true);
		addFlight(destIndex, flight);
		modification = true;
		System.out.println("Added successfully");
	}

	/* Removes a user-inputted flight
	*/
	public static void removeFlight() {
		System.out.println("-------------------------------------------------------------------");
		System.out.println("\t+----------------------+");
		System.out.println("\t|   Remove a Flight    |");
		System.out.println("\t+----------------------+\n");
		int originIndex = -1;
		int destIndex = -1;
		String destination, origin;
		while (originIndex < 0) {
			System.out.print("Enter origin: ");
			origin = input.nextLine();
			originIndex = indexOf(origin);
			if (originIndex < 0) {
				System.out.println("Invalid City");
			}
			System.out.println();
		}

		while (destIndex < 0) {
			System.out.print("Enter destination: ");
			destination = input.nextLine();
			destIndex = indexOf(destination);			
			if (destIndex < 0) {
				System.out.println("Invalid City");
			}
			System.out.println();
		}

		Flight remove = new Flight(originIndex, destIndex, 0, 0.0, false);
		removeWork(remove);
		Flight removeReverse = new Flight(destIndex, originIndex, 0, 0.0, true);
		removeWork(removeReverse);
		modification = true;
		System.out.println("Flight removed successfully");
	}

	/* Does the work of removing a flight
	 * @param f : flight to remove
	*/
	public static void removeWork(Flight f) {
		int originIndex = f.getOrigin();
		int destIndex = f.getDest();
		MyLinkedList list = routes[originIndex - 1];
		Node<Flight> node = list.getHead();

		while (node.getData() != null) {
			Flight nFlight = node.getData();
			if (nFlight.getDest() == destIndex) {
				Node<Flight> next = node.getNext();
				Node<Flight> prev = node.getPrev();

				if (next != null && prev !=null) {
					prev.setNext(next);
					next.setPrev(prev);
				} else if (next != null && prev == null) { //removing head
					next.setPrev(null);
					list.setHead(next);
				} else if (next == null && prev != null) { //removing tail
					prev.setNext(null);
				} else {	//flight was the only one in the list
					list.setHead(new Node<Flight>());
				}
				list.decrementSize();
			}
			node = node.getNext();
		} 
	}

 	/* Lets us know if a flight already exists
 	 * @param f : flight to check
 	 * @return true if the flight exists, false otherwise
 	*/
	public static boolean alreadyExists(Flight f) {
		if (routes[f.getOrigin() - 1] == null) {
			return false;
		}
		Node<Flight> node = routes[f.getOrigin() - 1].getHead();
		int destination = f.getDest();
		while (node.getData() != null) {
			int nodeDest = node.getData().getDest();
			if (nodeDest == destination) {
				return true;
			}
			node = node.getNext();
		}
		return false;
	}

	/* Quits the program and saves anmy modifications made
	*/
	public static void quitProgram() throws IOException {
		//If a modifcation was made, save it
		if (modification) {
			System.out.println("Saving changes...");
			file = new File(fileName);
			PrintWriter writer = new PrintWriter(file);

			writer.println(numCities);
			for (String city: cities) {
				writer.println(city);
			}

			//Prints out the flights and their information
			for (int i = 0; i < numCities; i++) {
				Node<Flight> node = routes[i].getHead();

				while (node.getData() != null) {
					Flight f = node.getData();
					if (!f.getReverse()) {
						writer.println((f.getOrigin()) + " " + 
							(f.getDest()) + " " + f.getDistance() + " " + f.getPrice());
					}

					node = node.getNext();
				}
			}
			writer.close();
		}
		System.out.println("Thanks for flying with Oceanic Airlines!\n");
		System.exit(0);
	}
}