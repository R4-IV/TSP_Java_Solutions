package Coursework.Djikstra;

import Coursework.AntColonyOptimization.AntColonyOptimization;
import java.util.Arrays;
import static Coursework.Utils.EuclideanCalculator.returnEuclideanDistance;

//File contains the Dijkstra algorithm.

public class Dijkstra {

    //Copies city coordinates from data loader, required for dynamic euclidean distance calculation.
    private int[][] cityCoordinates;
    //Matrix containing euclidean distance between cities.
    private double[][] euclideanDistanceMap;
    //Total amount of cities in the tsp problem.
    private int numberOfCities;
    //Priority queue used to store explored partial paths.
    PriorityQueue priorityQueue = new PriorityQueue();
    double upperLimit;

    //Constructor.
    public Dijkstra(int[][] cityCoordinates){
        numberOfCities = cityCoordinates.length;
        this.cityCoordinates = cityCoordinates;
        euclideanDistanceMap = new double[numberOfCities][numberOfCities];
        setUpperLimit(cityCoordinates);
    }

    //Sets an upper limit for the search allowing the removal of some partial paths that would otherwise be inserted into the queue
    //this provides a large memory save and a small-time save.
    private void setUpperLimit(int[][] cityCoordinates){
        //initialised heuristic algorithm
        AntColonyOptimization aco = new AntColonyOptimization(cityCoordinates);
        //search performed on the heuristic
        aco.antColonyOptimisation();
        upperLimit = aco.getBestLength();
    }

    //Search function performs search and returns the shortest path.
    public void search(){
        //Creates the first node of the search.
        QNode initialNode = new QNode( null ,1 , 0 , 1);
        QNode currentNode = initialNode;

        while(currentNode.getPathLength() != numberOfCities){
            //Creates an array where all cities that are visited will have a 1 at their index, this lets me explore neighbours without creating invalid tours due to duplicate cities.
            int[] unvisitedInNodeTour = new int[numberOfCities];
            //Save the current node so that when traversal takes place current node will not be destroyed.
            QNode retrieveTour = currentNode;
            //Traverses through the partial tour to extract all cities currently visited in the tour.
            while (retrieveTour != null){
                unvisitedInNodeTour[retrieveTour.getCityNum() -1] = 1;
                retrieveTour = retrieveTour.getParent();
            }
            for(int city = 0; city < unvisitedInNodeTour.length; city++){
                //Makes sure the selected city has not already appeared in the current tour.
                if(unvisitedInNodeTour[city] == 0){
                    //Updates the euclidean distance using current city + i +1(next city).
                    double newEuclideanDistance = currentNode.getEuclideanLength() + returnEuclideanDistance(city + 1 , currentNode.getCityNum() , euclideanDistanceMap , cityCoordinates);
                    //If it's a full path add a return distance to city 1 as well.
                    if(currentNode.getPathLength() + 1 == numberOfCities){
                        newEuclideanDistance+= returnEuclideanDistance(city + 1, 1 , euclideanDistanceMap , cityCoordinates);
                    }
                    //Checks if the new distance is not greater than the upper limit before adding it to the priority queue.
                    if(newEuclideanDistance <= upperLimit){
                        QNode newNode = new QNode( currentNode , city + 1 , newEuclideanDistance , currentNode.getPathLength() + 1);
                        priorityQueue.insertElement(newNode);
                    }
                }
            }
            //Updates the current node for the next traversal by retrieving the next shortest tour.
            currentNode = priorityQueue.removeFirstElement();
        }
        printOptimalPath(currentNode);
    }

    //Method prints optimal results to the console.
    private void printOptimalPath(QNode finalNode){
        //Retrieves the cumulative euclidean distance from the provided node.
        double finalPathLength = finalNode.getEuclideanLength();
        //Creates an array of cityNum + 1 size to hold complete tour.
        int[] finalPath = new int[numberOfCities + 1];
        //For loop iterates through the node to retrieve previous nodes and places them in the correct index position.
        for(int city = numberOfCities -1; city >= 0; city--){
            finalPath[city] = finalNode.getCityNum();
            //Updates the final node to that of its parent.
            finalNode = finalNode.getParent();
        }
        //Sets the final position of the array to 1 to show that the tour ends up returning to city 1.
        finalPath[numberOfCities] = 1;
        //Prints optimal parameters.
        System.out.println("Optimal Path Length: " + finalPathLength + " Path: " + Arrays.toString(finalPath));
    }
}
