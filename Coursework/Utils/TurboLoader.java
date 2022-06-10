package Coursework.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

//File contains a significantly faster implementation of autoloader. It uses Buffered reader with 16kb buffer and fast string operations like string.substring and charAt operations.
//The loader depends on uniform offsets between coordinates which it uses to split the input string without having to iterate over all the characters. In the case of non-uniform offsets
//The loader will throw a number format exception and gracefully fail into the slower autoloader implementation which can handle such cases.
public class TurboLoader {
    //Absolute path to file.
    private String PATH_TO_FILE;
    //Buffer size 16kb.
    private int BUFFER_SIZE = 16384;
    //Sub array size.
    private int SUB_ARR_SIZE = 3;
    //Stores coordinate data in an array of initial size 100. This is to avoid expensive array resize calls.
    private int[][] initialArray = new int[100][SUB_ARR_SIZE];
    //Used as a counter to constrain the output array.
    private int cityNum = 0;

    //Constructor passes file path to the loader.
    public TurboLoader(String PATH_TO_FILE){
        this.PATH_TO_FILE = PATH_TO_FILE;
    }

    public int[][] readData(){

        try {
             BufferedReader reader = new BufferedReader(new FileReader(PATH_TO_FILE), BUFFER_SIZE);
             //Current Line stores the value of the current line in the buffer. Needed to perform operations on line 1.
             String currentLine = reader.readLine();
             //Substring operations are destructive hence the line is copied over.
             String temporaryLine = currentLine;
             //Certain files have leading spaces this variable accounts for that
             boolean leadingSpace = false;
             //Used to infer how wide the redundant data of the file is.
             //Also used for setting index[0] of my output array.
             int lineIndex = 0;
             //int size of spaces assuming spaces are symmetrical.
             int sizeOfSpace;

             //Find out if there is a leading space in this file.
             if(Character.isWhitespace(currentLine.charAt(0))){
                leadingSpace = true;}
             //Gets the uniform size of the whitespaces and sets it to an integer variable.
             sizeOfSpace = sizeOfWhiteSpace((leadingSpace) ? temporaryLine : temporaryLine.substring(1));

             //Set up values of uniform offset allows for the skipping of parsing city numbers which can be inferred from a loop variable.
             int uniformOffset = leadingSpace ? 2 * sizeOfSpace : sizeOfSpace;
             //Line offset handling the first city.
             int lineOffset = uniformOffset + 1;
             initialArray[lineIndex] = allocateCoordinatesToArray(sizeOfSpace , currentLine.substring(lineOffset) , lineIndex);

             lineIndex++;
             //Handles the rest of the buffer using line spacing calculated by dissecting line 1.
             while((currentLine = reader.readLine()) != null){
                 lineOffset = uniformOffset + (((lineIndex + 1)/10 > 0) ? 2 : 1);
                 initialArray[lineIndex] = allocateCoordinatesToArray(sizeOfSpace , currentLine.substring(lineOffset) , lineIndex);
                 lineIndex++;
             }
             cityNum = lineIndex;
             //Buffered reader complains if it doesn't have this. This also fires in the event of a file not found exception.
        } catch (FileNotFoundException fnfe){
            System.out.println("File not found exiting program...");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Number format exception occurs when the space lengths between coordinates is not uniform.
        //The loader then fails gracefully into calling the slower implementation that is programmed to address this oversight.
        catch (NumberFormatException n){
            System.out.println("Number Format Exception: Switching to backup data loader...");
            DataLoader dt = new DataLoader(PATH_TO_FILE);
            return dt.returnCoordinates();

        }
        int[][] constrainedArray = Arrays.copyOf(initialArray , cityNum);
        return constrainedArray;
    }

    //Method allocates a line into an array and then puts that array into the initial array.
    private int[] allocateCoordinatesToArray(int sizeOfSpace , String coordinateSubstring, int index){
        int[] allocatedCoordinates = new int[SUB_ARR_SIZE];
        allocatedCoordinates[0] = index + 1; //Sets the city number need this as all algorithms assume an array of size [x][3].
        String firstCoordinate ="";
        //Finds out the length of the first coordinate by looping through the string until a whitespace is found.
        for(int character = 0; character < coordinateSubstring.length(); character++){
            if(!Character.isWhitespace(coordinateSubstring.charAt(character))){
                firstCoordinate += coordinateSubstring.charAt(character);
            }
            else{
                //Allocate inferred coordinates to array.
                allocatedCoordinates[1] = Integer.parseInt(firstCoordinate);
                allocatedCoordinates[2] = Integer.parseInt(coordinateSubstring.substring(character + sizeOfSpace));
                break;
            }
        }
        return allocatedCoordinates;
    }

    //Calculates the uniform whitespace delimiters in the file.
    private int sizeOfWhiteSpace( String currentLine){
        String identityOfSpace = "";
        for(int character = 0; character < currentLine.length(); character++ ){
            if(Character.isWhitespace(currentLine.charAt(character))){
                identityOfSpace+= character;
            }
            else{
                break;
            }
        }
        return identityOfSpace.length();
    }

}
