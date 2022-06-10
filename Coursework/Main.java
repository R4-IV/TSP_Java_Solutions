package Coursework;

//Imports of all required algorithms.
import Coursework.AntColonyOptimization.AntColonyOptimization;
import Coursework.DepthFirstSearch.DepthFirstSearch;
import Coursework.Djikstra.Dijkstra;
import Coursework.K_Opt.K_Opt;
import Coursework.Utils.TurboLoader;

import java.util.Arrays;


//This class and its methods will return results of all the required tsp optimisations.
//INSTRUCTIONS:
//1. Change the ABSOLUTE_PATH constant to the required test/train problem file, this file should be somewhere accessible by the program.
//2. Based on the selected file uncomment the specific test method.
//   Example: file: test_4_2021.txt -> uncomment testFour method.
//3. Re comment the used method before proceeding to the next file

public class Main {

    public static void main(String[] args) {
        //Starts the timer.
        long currentTime = System.nanoTime();

        //Constants containing max iterations for test 3 and a starting position for test 4.
        int TEST_THREE_ITERATIONS = 400;
        int GREEDY_START_POS = 1;


        //Place absolute path to file here. If the file is not found the program will notify and exit
        //I used absolute paths but a relative path in the directory will presumably work.
        String ABSOLUTE_PATH = "PUT DATA FILE HERE";
        TurboLoader tb = new TurboLoader(ABSOLUTE_PATH);
        int[][] coordinates = tb.readData();


        //If file is a train problem uncomment this method.
        //trainProblems(coordinates);

        //If file is either a test problem 1 or 2 uncomment this method.
        //Uses test 3 iterations in the event that the cityNum is too great for dijkstra to handle and switches over to the ACO algorithm.
        //testOneTwo(coordinates , TEST_THREE_ITERATIONS);

        //If the file is a test problem 3 uncomment this method.
        //testThree(coordinates , TEST_THREE_ITERATIONS);

        //If the file is a test problem 4 uncomment this method.
        //testFour(coordinates , GREEDY_START_POS);

        //Returns file running time in milliseconds.
        long finalTime = System.nanoTime() - currentTime;
        System.out.printf( "%.3f" , finalTime/1000000.0);
        System.out.print("ms");

    }

    //Method that will optimise all 3 train problems and return optimal results
    private static void trainProblems(int[][] coordinateData){
        DepthFirstSearch dfs = new DepthFirstSearch(coordinateData);
        System.out.println("Depth First Search Algorithm:");
        dfs.search();
    }

    //Method that will optimise test 1 and test 2 using the Dijkstra algorithm.
    private static void testOneTwo(int[][] coordinateData , int testThreeIterations){
        if(coordinateData.length > 13){
            System.out.println("City value above projected dijkstra capability, Switching algorithms...");
            testThree(coordinateData , testThreeIterations );
        }
        else{
            Dijkstra dijkstra = new Dijkstra(coordinateData);
            System.out.println("Dijkstra Algorithm:");
            dijkstra.search();
        }
    }

    //Method that will perform optimisation on test 3
    private static void testThree(int[][] coordinateData , int maxIteration) {
        int[] bestPath = null;
        double bestLength = Double.MAX_VALUE;

        //performs ACS for the selected amount iterations and saves the shortest path found.
        for(int iteration = 1; iteration <= maxIteration; iteration++ ){
            AntColonyOptimization acs = new AntColonyOptimization(coordinateData);
            acs.antColonyOptimisation();
            //Run 5 OPT on the returned ant path to remove them from any local minimum.
            K_Opt threeOPt = new K_Opt(coordinateData);
            int[] optimisedPath = threeOPt.performOptimization(5 ,acs.getBestPath());
            if(threeOPt.calculateTourLength(optimisedPath) < bestLength){
                bestLength = threeOPt.calculateTourLength(optimisedPath);
                bestPath = optimisedPath;
            }
        }
        //Prints required metrics.
        System.out.println("Ant Colony Optimization Algorithm:");
        System.out.println("Optimal Path " + Arrays.toString(bestPath));
        System.out.println("Optimal Length " + bestLength);
    }

    //Method performs test 4 by performing threeOpt until no improvement
    private static void testFour(int[][] coordinates , int startingIndex){
        K_Opt threeOpt = new K_Opt(coordinates);
        int[] optimizedPath =threeOpt.performOptimization(3 , threeOpt.returnGreedyTour(startingIndex));
        System.out.println("Algorithm Three Opt:");
        System.out.println("Path " + Arrays.toString(optimizedPath));
        System.out.println("Length " + threeOpt.calculateTourLength(optimizedPath));
    }
}
