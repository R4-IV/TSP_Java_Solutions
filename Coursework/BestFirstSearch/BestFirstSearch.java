package Coursework.BestFirstSearch;

//File contains best first search algorithm.
public class BestFirstSearch {

    //Constructor.
    public BestFirstSearch(){}

    //Method that returns a greedy tour given a starting city.
    public int[] returnBestFirstPath(int numberOfCities , int startingPoint , double[][] edgeWeightMatrix){
        int PATH_LENGTH = numberOfCities;
        int[] bestFirstPath = new int[PATH_LENGTH + 1];
        int[] cityHasBeenVisited = new int[PATH_LENGTH];
        int currentPathLength = 1;
        int bestFirstIndexIterator = 1;
        int currentBestIndex;
        double currentBestEdgeWeight;
        //Variable created in the event that bfs is used independently of another algorithm and is required to calculate its own length.
        double totalAccumulatedLength = 0;

        //Initialises the starting point of the greedy tour.
        bestFirstPath[0] = startingPoint;
        cityHasBeenVisited[startingPoint -1] = 1;

        //Performs best first search on remaining cities not in the cities has been visited array.
        while(currentPathLength != PATH_LENGTH){
            currentBestIndex = 0;
            currentBestEdgeWeight = Double.MAX_VALUE;
            for(int city = 0; city < numberOfCities; city++){
                double weightBetweenCities = edgeWeightMatrix[Math.min((bestFirstPath[bestFirstIndexIterator-1] -1) , city)][Math.max((bestFirstPath[bestFirstIndexIterator-1] -1) , city)];
                if(cityHasBeenVisited[city] != 1){
                    if(weightBetweenCities < currentBestEdgeWeight){
                        currentBestEdgeWeight = weightBetweenCities;
                        currentBestIndex = city;
                    }
                }
            }
            totalAccumulatedLength += currentBestEdgeWeight;
            bestFirstPath[bestFirstIndexIterator++] = currentBestIndex + 1;
            cityHasBeenVisited[currentBestIndex] = 1;
            currentPathLength++;
        }
        //Adds the starting city to the end of the tour to show returning to the home city.
        bestFirstPath[PATH_LENGTH] = startingPoint;
        totalAccumulatedLength+= edgeWeightMatrix[bestFirstPath[PATH_LENGTH - 1] -1][startingPoint -1];

        return bestFirstPath;
    }
}
