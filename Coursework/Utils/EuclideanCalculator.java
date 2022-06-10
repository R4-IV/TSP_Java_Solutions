package Coursework.Utils;

//This file is responsible for providing all the euclidean calculations necessary for the other algorithms to calcualte
//their matrices.

public class EuclideanCalculator {
    //Method builds upon the euclidean distance method to dynamically calculate.
    public static double returnEuclideanDistance(int cityOne , int cityTwo , double[][] euclideanMap , int[][] cityCoordinates){
        //Calculates the euclidean distance between 2 cities if the euclidean distance map for those coordinates is 0.
        //Only uses half the 2d array using the min/max function ensuring that the smallest of the 2 is first coordinate.
        if(euclideanMap[Math.min(cityOne -1 , cityTwo -1)][Math.max(cityOne -1 , cityTwo -1)] == 0.0){
            double euclideanDistance = euclideanDistance(cityCoordinates[cityOne - 1][1] , cityCoordinates[cityOne - 1][2] , cityCoordinates[cityTwo - 1][1] , cityCoordinates[cityTwo - 1][2]);
            euclideanMap[Math.min(cityOne -1 , cityTwo -1)][Math.max(cityOne -1 , cityTwo -1)] = euclideanDistance;
            return euclideanDistance;
        }
        //If the entry exists simply return it.
        else{
            return euclideanMap[Math.min(cityOne -1 , cityTwo -1)][Math.max(cityOne -1 , cityTwo -1)];
        }
    }

    //Method to calculate euclidean distance between 2 points (x,y) coordinates.
    private static double euclideanDistance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow((x2 - x1) , 2) + Math.pow((y2 - y1) , 2));
    }

    //Method returns a 2d array with all euclidean distances precalculated.
    public static double[][] calculateEdgeWeightMatrix(int numberOfCities , int[][] cityCoordinates){
        //Creates a new 2d array.
        double[][] edgeWeightMatrix = new double[numberOfCities][numberOfCities];
        for(int cityOne = 0; cityOne < numberOfCities; cityOne++){
            for(int cityTwo = 0; cityTwo < numberOfCities; cityTwo++){
                //Performs the euclidean calculation by calling city calculation method.
                edgeWeightMatrix[cityOne][cityTwo] = calculateEuclideanDistance(cityOne , cityTwo , cityCoordinates);
            }
        }
        //Returns the created 2d array.
        return edgeWeightMatrix;
    }

    //Method returns euclidean distance based on city input.
    private static double calculateEuclideanDistance(int cityOne , int cityTwo , int[][] cityCoordinates){
        int FIRST_COORD = 1;
        int SECOND_COORD = 2;
        //Builds the coordinates required by the euclidean distance function.
        int x1 = cityCoordinates[cityOne][FIRST_COORD];
        int y1 = cityCoordinates[cityOne][SECOND_COORD];

        int x2 = cityCoordinates[cityTwo][FIRST_COORD];
        int y2 = cityCoordinates[cityTwo][SECOND_COORD];

        //Passes the created coordinates to the euclidean distance method which then returns the answer.
        return euclideanDistance(x1 , y1 ,x2 ,y2);
    }

    //Method creates a fast edge weight matrix by only calculating less than half of it.
    //This can be done because the tsp is symmetric hence (a,b) == (b,a).
    public static double[][] fastEdgeWeightMatrix(int[][] coordinates){
        double[][] edgeWeightMatrix = new double[coordinates.length][coordinates.length];
        //The for loop shrinks over time.
        for(int firstCity = 0; firstCity < coordinates.length; firstCity++){
            for(int secondCity = firstCity + 1; secondCity < coordinates.length; secondCity++){
                edgeWeightMatrix[firstCity][secondCity] = calculateEuclideanDistance(firstCity , secondCity , coordinates);
            }
        }
        //The resultant edge weight matrix has holes so in order to access the correct values Math.min , Math.max.
        return edgeWeightMatrix;
    }
}
