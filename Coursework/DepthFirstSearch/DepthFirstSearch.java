package Coursework.DepthFirstSearch;

import java.util.Arrays;
import static Coursework.Utils.EuclideanCalculator.returnEuclideanDistance;

//File used to perform depth first search, it holds all functions needed to perform the exhaustive search

public class DepthFirstSearch {
    //Stores calculated euclidean distance, because it's a symmetric tsp problem I only use half the array.
    private double[][] euclideanDistanceMap;
    //Coordinates imported from data-loader I need this as my distance is dynamically calculated on A as needed basis.
    private int[][] cityCoordinates;
    //Total amount of cities in the tsp problem.
    private int numberOfCities;
    //Stack holds current path.
    private Stack stack = new Stack();
    //Array holds a 1 for visited and 0 for not visited in the current tour.
    private int[] visitedInCurrentTourArray;
    //Array holding best path.
    private int[] bestPath;
    //Euclidean length of the current best path, initialised to max double value which guarantees that the best path is smaller even for extreme coordinates.
    private double bestPathDistance = Double.MAX_VALUE;
    //Tracks the length of the current tour initialised to 1 as city 1 is a constant at position 1.
    private int currentTourLength = 1;

    //DFS constructor.
    public DepthFirstSearch(int[][] cityCoordinates){
        numberOfCities = cityCoordinates.length;
        this.cityCoordinates = cityCoordinates;
        euclideanDistanceMap = new double[numberOfCities][numberOfCities];
        visitedInCurrentTourArray = new int[numberOfCities];
    }

    //Each stack node holds an unvisited neighbour array this function creates the initial values it takes.
    public int[] createUnvisitedNeighboursArray(int cityNum){
        int[] unvisitedNeighbours = new int[numberOfCities];
        //City 1 is a constant in the tour hence it cannot be visited as a potential neighbour.
        unvisitedNeighbours[0] = 1;
        //Sets the target city to visited as well since in tsp visiting the same city twice is not a valid state.
        unvisitedNeighbours[cityNum - 1] = 1;
        return  unvisitedNeighbours;
    }

    //Method selects the next city based on current node unvisited neighbours and the global visited tour cities.
    public int selectNextCity(int[] unvisitedNeighbours , int[] visitedInCurrentTour){
        //Staggered from 1 as I don't need to check city 1.
        //Initialised to -1 in the event that no match is found -1 is returned which will signalise that the stack requires popping.
        int nextCityNumber = -1;
        for(int city = 1; city < numberOfCities; city++){
            if(unvisitedNeighbours[city] == 0 && visitedInCurrentTour[city] == 0){
                nextCityNumber = city +1;
                //Need to break to ensure that the first eligible city is chosen and not overwritten.
                break;
            }
        }
        //Returns the next city based on the local global comparison.
        return nextCityNumber;
    }

    //Method performs exhaustive search through the pushing and popping nodes in the stack.
    public void search(){
        //Perform initial steps to put city 1 into the stack.
        stack.push(1 , createUnvisitedNeighboursArray(1) , 0);
        //Sets the first city in the tour as 1.
        visitedInCurrentTourArray[0] = 1;

        //Will continue until node 1 is popped.
        while(stack.peek() != null){
            //Calculates the next eligible city to push into the stack if the result is not -1 then it's a push operation.
            int nextCity = selectNextCity(stack.peek().getUnvisitedNeighbours() , visitedInCurrentTourArray);
            //Push operations.
            if(nextCity != -1){
                //Update neighbours of the current node this ensures that repeat tour permutations are not possible.
                stack.peek().getUnvisitedNeighbours()[nextCity - 1] = 1;
                //Pushes next city into the stack.
                stack.push(nextCity , createUnvisitedNeighboursArray(nextCity).clone() , (stack.peek().getCurrentPathLength() + returnEuclideanDistance(stack.peek().getCityNumber() , nextCity , euclideanDistanceMap , cityCoordinates)));
                //Updates the tour length so that I can later use it to establish when the tour is complete.
                currentTourLength++;
                visitedInCurrentTourArray[nextCity - 1] = 1;
            }
            //Selection of next city returns -1 therefore pop operations need to begin.
            else{
                //Tour is complete.
                pathIsComplete(currentTourLength);
                //Removes the current city from the global visited array.
                visitedInCurrentTourArray[stack.peek().getCityNumber() -1] = 0;
                stack.pop();
                currentTourLength--;
            }
        }
        //Prints the best result of the search to the console.
        printBestPath();
    }

    //Method used to handle a complete path situations.
    private void pathIsComplete(int currentTourLength){
        if(currentTourLength == numberOfCities){
            //Keeps track of the current complete path length.
            double completePathLength = stack.peek().getCurrentPathLength() + returnEuclideanDistance(stack.peek().getCityNumber() , 1 , euclideanDistanceMap , cityCoordinates);

            //Checks if this is better than the current best, if it isn't the subsequent tour building is not necessary.
            if(completePathLength < bestPathDistance){
                bestPathDistance = completePathLength;
                int[] currentTour = new int[numberOfCities + 1];
                //Sets the final city to 1 to simulate returning to the first city in the tour.
                currentTour[currentTour.length -1] = 1;
                //Reference to the head of the stack.
                StackNode nodePath = stack.peek();
                //Build the tour by iterating through the stack non-destructively.
                for(int currentCity = numberOfCities -1; currentCity >= 0; currentCity--){
                    currentTour[currentCity] = nodePath.getCityNumber();
                    nodePath = nodePath.getParentNode();
                }
                //Updates current path to be the best path.
                bestPath = currentTour;
            }
        }
    }

    //Once the algorithm completes its search this function will print the best path along with its distance.
    private void printBestPath(){
        System.out.println("Optimal Path Length: " + bestPathDistance + " Path: " + Arrays.toString(bestPath));
    }
}
