package Coursework.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This file takes input from a text file and reads it then stores it in an array.
//This array is then accessed by other algorithms to retrieve coordinate data.

public class DataLoader {
    //Stores the string path to the file being loaded.
    private String dataPath = "";
    //Array to store each line of coordinates.
    private int[] coordinates;
    int SUB_ARRAY_SIZE = 3;
    //Used to store an unknown amount of coordinates.
    private int[][] coordinateContainer = new int[200][SUB_ARRAY_SIZE];
    int cityIndex = 0;

    //DataLoader constructor sets up path and loads in data.
    public DataLoader(String dataPath){
        this.dataPath = dataPath;
        loadData();
    }

    //Uses a scanner and some regex to load data from differently spaced integer data sets.
    private void loadData(){
        //Scanner initialised so that it is in scope for the finally statement regardless of exception.
        Scanner dataIn = null;
        try {
            //File created from the string path.
            File fileToRead = new File(dataPath);
            dataIn = new Scanner(fileToRead);
            //Creates a regex pattern that matches 1 or more integers between 0-9 including negative numbers , this removes all the arbitrary amount of spaces from the data.
            Pattern pattern = Pattern.compile("([-0-9]+)");
            while(dataIn.hasNextLine()){
                //Keeps track of the position to put the found regex numbers in.
                int index = 0;
                //Creates a new empty array to keep all the integer coordinates in.
                coordinates = new int[3];
                Matcher matcher = pattern.matcher(dataIn.nextLine());
                while(matcher.find()){
                    coordinates[index] = Integer.parseInt(matcher.group(1));
                    index++;
                }
                coordinateContainer[cityIndex++] = coordinates;
            }
        }
        //If the file in the path doesn't exist this will fire.
        catch (FileNotFoundException fnfe){
            System.out.println("File not found");
        }
        //Performs this code regardless of the try catch loop success.
        finally {
            dataIn.close();
        }
    }

    //Method used to trim the initial size 200 array into a tight-fitting array of size n (number of cities).
    public int[][] returnCoordinates(){
        int[][] cityCoordinates = Arrays.copyOf(coordinateContainer , cityIndex);
        return cityCoordinates;
    }
}
