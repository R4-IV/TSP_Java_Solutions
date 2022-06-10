package Coursework.K_Opt;

import Coursework.BestFirstSearch.BestFirstSearch;

import java.util.Arrays;

import static Coursework.Utils.EuclideanCalculator.calculateEdgeWeightMatrix;
import static Coursework.Utils.EuclideanCalculator.fastEdgeWeightMatrix;

//File contains the KOpt algorithm.

public class K_Opt {

    //Constant holds number of cities.
    private int numberOfCities;
    //Constant holds the calculated edge weight matrix.
    private double[][] edgeWeightMatrix;

    //Constructor.
    public K_Opt(int[][] cityCoordinates) {
        numberOfCities = cityCoordinates.length;
        edgeWeightMatrix = calculateEdgeWeightMatrix(numberOfCities, cityCoordinates);
    }

    //Alternative constructor for faster three opt.
    public K_Opt(int[][] cityCoordinates , int searchingLimit , int greedyStartPosition){
        numberOfCities = cityCoordinates.length;
        edgeWeightMatrix = fastEdgeWeightMatrix(cityCoordinates);
        //BFS tour three opt entry position.
        BestFirstSearch bfs = new BestFirstSearch();
        int[] greedyStart = bfs.returnBestFirstPath(numberOfCities , greedyStartPosition , edgeWeightMatrix);
        //Performs three opt on the greedy tour.
        int[] optimisedPath = fasterThreeOpt(greedyStart,searchingLimit);
        //Calculates the tour length of the optimised path.
        double optimisedLength = calculateTourLength(optimisedPath);
        //Prints optimisation results to the console.
        System.out.println("Greedy Length: " + calculateTourLength(greedyStart));
        System.out.println("Faster Three Opt Algorithm");
        System.out.println("Path: " + Arrays.toString(optimisedPath));
        System.out.println("Distance: " + optimisedLength);
    }

    //Creates a starting tour 1,2,3...n.
    public int[] createPredicableTour() {
        int[] predictableTour = new int[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            predictableTour[i] = i + 1;
        }
        return predictableTour;
    }

    //Creates a greedy tour.
    public int[] returnGreedyTour(int startingIndex){
        BestFirstSearch greed = new BestFirstSearch();
        return greed.returnBestFirstPath(numberOfCities , startingIndex , edgeWeightMatrix);
    }


    //Returns the length of the provided tour including returning to city one which is a constant.
    public double calculateTourLength(int[] tour) {
        double tourLength = 0;
        for(int city = 0; city < numberOfCities -1; city++){
            tourLength += edgeWeightMatrix[min((tour[city] -1) , (tour[city + 1] -1))][max((tour[city] -1) , (tour[city + 1] -1))];
        }
        //Calculates the distance between the last city and the beginning of the tour.
        tourLength+= edgeWeightMatrix[min((tour[numberOfCities -1] -1) , (tour[0] - 1))][max((tour[numberOfCities -1] -1) , (tour[0] - 1))];
        return tourLength;
    }

    //Method to reverse given edge quickly.
    private int[] reverseEdge(Edge edge, int[] tour) {
        //Variables keeping track of the back and front position of the edge.
        int forwardIterator = edge.getLowerBound();
        int reverseIterator = edge.getUpperBound();
        int edgeLength = (edge.getUpperBound() - edge.getLowerBound() + 1);
        //Swaps the cities at upper and lower bounds, because I'm swapping the upper and the lower indexes simultaneously only (edge size/2) swaps are necessary.
        for (int swapNum = 0; swapNum < edgeLength / 2; swapNum++) {
            //Swapping edges at iterator positions with each other.
            tour[forwardIterator] = tour[reverseIterator] + tour[forwardIterator];
            tour[reverseIterator] = tour[forwardIterator] - tour[reverseIterator];
            tour[forwardIterator] = tour[forwardIterator] - tour[reverseIterator];
            //Move iterator updates position in preparation for the next swap.
            reverseIterator--;
            forwardIterator++;
        }
        //Returns full tour with swapped edge.
        return tour;
    }

    //Method returns an array of 1 and 0 (1 = reversed , 0 = normal) of edge orientations.
    private int[] generateEdgeOrientation(int caseNumber, int numberOfEdges) {
        int[] pathConfiguration = new int[numberOfEdges];
        int remainderFromCase = caseNumber;
        for (int binaryNumPosition = numberOfEdges - 1; binaryNumPosition >= 0; binaryNumPosition--) {
            pathConfiguration[binaryNumPosition] = remainderFromCase % 2;
            remainderFromCase = remainderFromCase / 2;
        }
        return pathConfiguration;
    }

    //Generates unique disjoint edge combination boundaries. Using methodology from 2.5 Opt I generate n-1 boundaries for n edges.
    private int[] generateEdges(int[] edgesArray) {
        for (int edge = edgesArray.length - 1; edge >= 0; edge--) {
            if (isEdgeAtLimit(edgesArray, edge)) {
                continue;
            } else {
                edgesArray[edge] += 1;
                for (int forwardEdgeIndex = edge + 1; forwardEdgeIndex < edgesArray.length; forwardEdgeIndex++) {
                    edgesArray[forwardEdgeIndex] = edgesArray[forwardEdgeIndex - 1] + 2;
                }
                break;
            }
        }
        return edgesArray;
    }

    //Method used to create the first edge array in an iteration following the formula j = i+2. This ensures all edges are disjoint.
    //Index 0 is skipped because I want to ensure the position of city 1 is kept constant
    private int[] generateInitialEdges(int numberOfEdges) {
        //Following the 2.5 opt strategy of creating edge bounds as opposed to actual edges means that I can generate n-1 bounds where n is the amount of edges required.
        int[] initialEdges = new int[numberOfEdges - 1];
        //Initial edge index.
        int edgeVal = 0;
        for (int edgeNum = 0; edgeNum < initialEdges.length; edgeNum++) {
            initialEdges[edgeNum] = edgeVal + 2;
            edgeVal += 2;
        }
        return initialEdges;
    }


    //Determines whether a given edge is at its max possible value while respecting the following rule: all edges must be disjoint
    private boolean isEdgeAtLimit(int[] edgesArray, int currentEdgeIndex) {
        int edgeMaxValue = (numberOfCities - 1) - (edgesArray.length - currentEdgeIndex) * 2;
        if (edgesArray[currentEdgeIndex] == edgeMaxValue) {
            return true;
        }
        return false;
    }

    //Creates edge objects which are needed for easy reversal function.
    private Edge[] createEdgeObjects(int[] edgesArray) {
        int INDEX_OF_FIRST_VARIABLE_CITY = 1;
        int INDEX_OF_LAST_VARIABLE_CITY = numberOfCities - 1;

        Edge[] edgeContainer = new Edge[edgesArray.length + 1];
        for (int edgeNum = 0; edgeNum < edgesArray.length; edgeNum++) {
            Edge edge;
            if (edgeNum == 0) {
                //First edge case.
                edge = new Edge(INDEX_OF_FIRST_VARIABLE_CITY, edgesArray[edgeNum]);
            } else {
                edge = new Edge(edgesArray[edgeNum - 1] + 1, edgesArray[edgeNum]);
            }
            edgeContainer[edgeNum] = edge;
        }
        //Last edge case.
        Edge edge = new Edge(edgesArray[edgesArray.length - 1] + 1, INDEX_OF_LAST_VARIABLE_CITY);
        edgeContainer[edgeContainer.length - 1] = edge;
        return edgeContainer;
    }

    //Recombines the tour based on edge objects and edge orientations.
    private int[] recombineTour(Edge[] edgesArray, int[] orientationArray , int[] tour ){
        int[] swappedTour = tour.clone();
        for(int edge = 0; edge < edgesArray.length; edge++){
            if(orientationArray[edge] == 1){
                swappedTour = reverseEdge(edgesArray[edge] , swappedTour);
            }
        }
        return swappedTour;
    }

    //Performs a K_OPT Operations until they no longer result in path gain.
    //This optimisation is done on a first gain basis.
    public int[] performOptimization(int numberOfEdges , int[] tour){
        double pathGain = 0.1;
        int INDEX_OF_FIRST_EDGE = 0;
        int[] edgesArray;
        int[] optimisedTour = tour;
        int[] bestPath = null;

        while(pathGain != 0){
            pathGain = 0;
            edgesArray = generateInitialEdges(numberOfEdges);
            //All edges.
            while(!isEdgeAtLimit(edgesArray , INDEX_OF_FIRST_EDGE)){
                //All permutations.
                //Create the baseline length.
                double baseline = calculateTourLength(optimisedTour);
                for(int edgeOrientation = 1; edgeOrientation < (int)Math.pow(2 , numberOfEdges); edgeOrientation++){
                    int[] orientationCase = generateEdgeOrientation(edgeOrientation , numberOfEdges);
                    Edge[] edges = createEdgeObjects(edgesArray);
                    int[] permutationTour = recombineTour(edges , orientationCase , optimisedTour.clone());
                    double permutationLength = calculateTourLength(permutationTour);
                    //Calculates the greatest path gain from all permutations in the current edge boundary.
                    if(baseline - permutationLength > pathGain){
                        pathGain = baseline - permutationLength;
                        bestPath = permutationTour;
                    }
                }
                //Updates the edge boundaries in preparation for the next loop.
                edgesArray = generateEdges(edgesArray);
                //If there is gain on this edge combination update the best tour and restart the loop.
                if(pathGain > 0){
                    optimisedTour = bestPath;
                    break;
                }
            }
        }
        return optimisedTour;
    }


    //Simplified min/max method so that it takes up less line space in the three opt method
    private int min(int firstNum , int secondNum){
        return Math.min(firstNum ,secondNum);
    }

    private int max(int firstNum , int secondNum){
        return Math.max(firstNum ,secondNum);
    }
    //-------------------------------------------------

    //This method allows for returning length of tour based on its edge connections hence removing the need for total tour length calculation which will take longer.
    private double calculateRelativeEdgeLength(int[] tour , Edge[] edges , int[] edgeOrientation){
        //For loop reverses edge orientation in edge objects so that the below tour progression is consistent regardless of reversed or normal edge inputs.

        for(int edge = 0; edge < edges.length; edge++){
            if(edgeOrientation[edge] == 1){
                edges[edge].reverseOrientation();
            }
        }

        //Calculates edge connections: constant city -> edgeOneL + edgeOneU -> edgeTwoL + edgeTwoU-> edgeThreeL + edgeThreeU -> constant city.
        double relativeEdgeLength = edgeWeightMatrix[min((tour[0] - 1),(tour[edges[0].getLowerBound()] -1))][max((tour[0] - 1),(tour[edges[0].getLowerBound()] -1))];
        relativeEdgeLength+= edgeWeightMatrix[min((tour[edges[0].getUpperBound()] -1) , (tour[edges[1].getLowerBound()] -1))][max((tour[edges[0].getUpperBound()] -1) , (tour[edges[1].getLowerBound()] -1))];
        relativeEdgeLength+= edgeWeightMatrix[min((tour[edges[1].getUpperBound()] -1) , (tour[edges[2].getLowerBound()] -1))][max((tour[edges[1].getUpperBound()] -1) , (tour[edges[2].getLowerBound()] -1))];
        relativeEdgeLength+= edgeWeightMatrix[min((tour[edges[2].getUpperBound()] -1) , (tour[0] -1))][max((tour[edges[2].getUpperBound()] -1) , (tour[0] -1))];

        //returns edge length
        return relativeEdgeLength;
    }

    //Method meant to optimise three opt operations does not work currently prefers to return bfs instead of meaningfully optimising.
    private int[] fasterThreeOpt(int[] initialTour , int searchingLimit){
        //6 cases because case 0 and case 7 are the same this is addressed in the for loop that calculates them
        int NUM_OF_THREE_OPT_CASES = 8;
        int lowerLimit = 1;
        int upperLimit = numberOfCities;

        //Reference to provided tour allows for manipulation of the tour continuously.
        int[] optimisingTour = initialTour;

        //Boolean value for gain determines whether gain has been made.
        boolean gain = true;
        //Variable to be compared against searching limit.
        int currentSearchNum = 1;

        //Label for the loop so that an inner loop can break it. This occurs once the currentSearchNum reaches the limit value supplied.
        optimisationLoop:
        while(gain){
            //Reset gain.
            gain = false;

            //Store edge information to be accessed via the reverse function in a greater scope.
            int[] orientationToBeReversed = null;
            Edge[] edgesToBeReversed = null;

            //Loop for edge selection to be broken if gain is achieved.
            outerLoop:
            for(int edgeOne = lowerLimit + 2 + (numberOfCities/3) ; edgeOne < upperLimit - 4; edgeOne++){
                for(int edgeTwo = edgeOne + 2; edgeTwo +(numberOfCities/6) < upperLimit -2; edgeTwo++){
                    //Creates all 3 edges from the edgeOne/Two variables.
                    Edge first = new Edge(lowerLimit + 1 , edgeOne);
                    Edge second = new Edge(edgeOne + 1 , edgeTwo);
                    Edge third = new Edge(edgeTwo + 1 , upperLimit -1);
                    Edge[] edges = new Edge[]{first , second , third};

                    //Relative length of the current edge in all non-reversed configuration.
                    double defaultEdgeLength = calculateRelativeEdgeLength(optimisingTour , edges.clone() , new int[]{0,0,0});
                    //double defaultEdgeLength = calculateTourLength(recombineTour(edges , new int[]{0,0,0} ,optimisingTour));

                    //Works out the 6 unique cases , starting with three opt cases and moving to 2opt cases. The first gain found is selected.
                    for(int edgeCase = NUM_OF_THREE_OPT_CASES -1; edgeCase > 0; edgeCase--) {
                        int[] edgeOrientation = generateEdgeOrientation(edgeCase, 3);

                        double caseEdgeLength = calculateRelativeEdgeLength(optimisingTour, edges.clone(), edgeOrientation);
                        //double caseEdgeLength = calculateTourLength(recombineTour(edges , edgeOrientation , optimisingTour));
                        //Checks if the case has made a gain on the current tour.
                        if (defaultEdgeLength > caseEdgeLength) {
                            gain = true;
                            orientationToBeReversed = edgeOrientation;
                            edgesToBeReversed = edges;
                            //Recombine tour for the next iteration of optimisation.
                            optimisingTour = recombineTour(edgesToBeReversed , orientationToBeReversed , optimisingTour);
                            //Exists loop to while loop so next iteration of optimisation can occur
                            break outerLoop;
                        }
                    }

                    //Handles depth of searching in the Three Opt will exit early if no gain is found after x amount of edges.
                    if(!gain){
                        if(currentSearchNum == searchingLimit){
                            break optimisationLoop;
                        }
                        else{
                            currentSearchNum++;
                        }
                    }
                    else{
                        currentSearchNum = 0;
                    }
                }
            }
        }
        return optimisingTour;
    }
}

