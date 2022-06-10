package Coursework.AntColonyOptimization;

import java.util.Arrays;
import static Coursework.Utils.EuclideanCalculator.calculateEdgeWeightMatrix;

//File contains the Ant Colony Optimisation algorithm. This algorithm is based on a natural optimisation approach.

public class AntColonyOptimization {
    //Constants.
    //Constant storing the maximum iterations the current ACO should run for.
    private int ITERATION_NUM = 200;
    //Constant storing the colony size at each iteration
    private int COLONY_SIZE = 200;
    //Constant storing the value of the vaporisation percentage that will reduce pheromone levels across the tsp at the end of each iteration.
    private double VAPORISATION_RATE = 0.6;
    //Constant passed to ant objects used to impact the influence of exploration on the ants decision-making.
    private double EXPLORATION_RATE = 8.0;

    //Stores the total number of cities in this tsp.
    private int cityNum;
    //Matrices storing pheromones and edge weights.
    private double[][] edgeWeightMatrix;
    private double[][] pheromoneDistributionMatrix;
    private double[][] changeInPheromoneMatrix;

    //Variables used to store algorithm metrics.
    private int[] bestPath;
    private double bestLength = Double.MAX_VALUE;
    private int bestIteration = 0;
    private int currentIteration = 1;


    //Constructor.
    public AntColonyOptimization(int[][] cityCoordinates){
        cityNum = cityCoordinates.length;
        edgeWeightMatrix = new double[cityNum][cityNum];
        pheromoneDistributionMatrix = new double [cityNum][cityNum];
        changeInPheromoneMatrix = new double[cityNum][cityNum];
        //Calculates adjacency matrix.
        edgeWeightMatrix = calculateEdgeWeightMatrix(cityNum , cityCoordinates);

        //Initialises the pheromone matrix with a constant, this is done so that probability in the decision-making function works.
        for(double[] pheromoneAtEdge : pheromoneDistributionMatrix){
            Arrays.fill(pheromoneAtEdge , 0.5);
        }
    }

    //This method performs an iteration of searching, and adds to the change in pheromone matrix.
    public void antSearch(){
        Ant ant;
        //Loop to create ant objects equal to the size of the ant colony
        for(int ants = 0; ants < COLONY_SIZE; ants++){
            ant = new Ant(cityNum , EXPLORATION_RATE );
            ant.makeCompleteTour(pheromoneDistributionMatrix , edgeWeightMatrix);
            int[] path = ant.getPathTravelled();
            double pathLength = ant.getPathDistanceLength();
            if(pathLength < bestLength){
                bestLength = pathLength;
                bestPath = path;
                bestIteration = currentIteration;
            }
            //Uses the ants path to update the change in pheromone matrix levels by adding 1/L to the tour at each edge.
            for(int j = 0; j < path.length - 1; j++){
                changeInPheromoneMatrix[path[j] - 1][path[j + 1] - 1] += 1/pathLength;
            }
        }
    }

    //Method used at the end of an iteration to update the global pheromone matrix. This is done by multiplying the global with the vaporisation rate constant
    //and then performing matrix addition on the change in pheromone matrix.
    private void changeInPheromones(){
        for(int cityOne = 0; cityOne < pheromoneDistributionMatrix.length; cityOne++){
            for(int cityTwo = 0; cityTwo < pheromoneDistributionMatrix.length; cityTwo++){
                pheromoneDistributionMatrix[cityOne][cityTwo] = (pheromoneDistributionMatrix[cityOne][cityTwo] * VAPORISATION_RATE) + changeInPheromoneMatrix[cityOne][cityTwo];
            }
        }
    }

    //Method performs the optimisation over a set number of iterations.
    public void antColonyOptimisation(){
        while (currentIteration != ITERATION_NUM){
            //Checks whether there has been a change in best in the last 5 iterations and if there wasn't then a minimum is assumed anf the loop exits.
            if(currentIteration > bestIteration + 5){
                break;
            }
            //Reduces exploration rate over time so that exploitation/pheromones end up influencing the probability to a greater degree.
            if(EXPLORATION_RATE != 1.0){
                EXPLORATION_RATE -= 0.2;
            }
            //performs an iteration of ant searching.
            antSearch();
            //Updates the global matrix in preparation for the next iteration.
            changeInPheromones();
            currentIteration++;
        }
    }

    /*----------Getter Methods------*/

    public double getBestLength() {
        return bestLength;
    }

    public int[] getBestPath() {
        return bestPath;
    }

    public int getBestIteration() {
        return bestIteration;
    }
}
