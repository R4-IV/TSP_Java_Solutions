package Coursework.AntColonyOptimization;

//File contains an Ant object which is used for tsp traversal in the ACO algorithm.

public class Ant {

    //Total number of cities.
    private int cityNum;
    //Array containing the path the ant has travelled.
    private int[] pathTravelled;
    //An array of cities the ant has yet to visit in this tour.
    private int[] unvisitedNeighbours;
    private double pathDistanceLength = 0;
    //Length of the path lets the ant know when its completed a full path.
    private int pathCityLength = 1;
    //Identity of the current city the ant is at.
    private int currentLocation = 1;

    //Weight of shared knowledge exploitation.
    private double EXPLOITATION_FACTOR = 1.5;
    //Weight of exploration based decision-making.
    private double EXPLORATION_FACTOR;


    //Ant Object constructor.
    public Ant(int cityNum , double EXPLORATION_FACTOR){
        this.cityNum = cityNum;
        //Sets the exploration factor provided from the ACO algorithm. This is done so that over time explorations
        //influence on decision-making can be diminished.
        this.EXPLORATION_FACTOR = EXPLORATION_FACTOR;
        pathTravelled = new int[cityNum +1];
        //Sets the first city of the path travelled to be city 1. In this implementation all ants begin at city 1 and then proceed to complete full tours.
        pathTravelled[0] = 1;
        unvisitedNeighbours = new int[cityNum];
        //Sets city One as visited in the unvisited array.
        unvisitedNeighbours[0] = 1;
    }

    //Method used the ant's probabilistic decision-making to generate a complete tour starting at city 1.
    public void makeCompleteTour(double[][] pheromoneMatrix , double[][] lengthWeightMatrix){
        //loop to perform decisions until the path length is equal to the number of cities in the tsp.
        while (pathCityLength != cityNum ){
            int nextCity = makeDecision(pheromoneMatrix , lengthWeightMatrix);
            pathDistanceLength += lengthWeightMatrix[currentLocation -1][nextCity -1];
            unvisitedNeighbours[nextCity -1] = 1;
            pathTravelled[pathCityLength] = nextCity;
            pathCityLength++;
            currentLocation = nextCity;
        }
        //Return to city one, and update the accumulated path to reflect that.
        pathDistanceLength += lengthWeightMatrix[currentLocation -1][0];
        //sets the last city in the path travelled array so that if printed it will return 1,2,3...n,1.
        pathTravelled[pathTravelled.length - 1] = 1;
    }

    //Method used by the ant to make a probabilistic decision as to which city to travel to next.
    private int makeDecision(double[][] pheromoneMatrix , double[][] lengthWeightMatrix){
        //Random integer will be used to probabilistically determine the ants next destination.
        int decisionNumber = generateRandomInt();
        //Probability for decision = (probability of current city) / (sum of probabilities of all eligible cities)
        double totalProbabilityAccumulator = 0;
        int currentProbabilityAccumulator = 0;
        int[] unvisitedCities = new int[(pathTravelled.length -1) -pathCityLength];
        int iterator = 0;
        int nextCity = 0;

        //Calculate total probability for all cities that have yet to be visited in the current path.
        for(int city = 0; city < unvisitedNeighbours.length; city++){
            if(unvisitedNeighbours[city] == 0){
                unvisitedCities[iterator] = city;
                totalProbabilityAccumulator += Math.pow(pheromoneMatrix[currentLocation - 1][city] , EXPLOITATION_FACTOR) * Math.pow((1/lengthWeightMatrix[currentLocation -1][city]) , EXPLORATION_FACTOR);
                iterator++;
            }
        }
        //This loop used to calculate a running sum of the current city probability and compare it to the generated number.
        //In the best case if the generated number is low then subsequent calculations can be skipped.
        for(int city = 0; city < unvisitedCities.length; city++){
            currentProbabilityAccumulator += (int)((Math.pow(pheromoneMatrix[currentLocation - 1][unvisitedCities[city]] , EXPLOITATION_FACTOR) * Math.pow(1/(lengthWeightMatrix[currentLocation -1][unvisitedCities[city]]) , EXPLORATION_FACTOR)/totalProbabilityAccumulator) * 100);
            if(currentProbabilityAccumulator >= decisionNumber){
                nextCity = unvisitedCities[city] + 1;
                break;
            }
            //Handles the error that occurs from rounding doubles into integers which results in a shortfall of matches in the final case.
            else if(city == unvisitedCities.length -1){
                nextCity = unvisitedCities[city] + 1;
            }
        }
        //Returns the decision the ant came to in the form of its next destination.
        return nextCity;
    }

    //Method returns a random integer between 1 - 100, this is to simulate the probabilistic behaviour of the ant decision-making.
    private int generateRandomInt(){
        return (int)(Math.random() * 100) + 1;
    }

    /*-----------Getter Methods-------------*/
    public double getPathDistanceLength() {
        return pathDistanceLength;
    }

    public int[] getPathTravelled() {
        return pathTravelled;
    }
}
