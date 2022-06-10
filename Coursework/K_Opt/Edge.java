package Coursework.K_Opt;

//File contains Edge class, This class is useful for K_opt reversal.

public class Edge {
    private int lowerBound;
    private int upperBound;

    //Constructor.
    public Edge (int lowerBound , int upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    //Method swaps the position of upper and lower bound of the edges.
    public void reverseOrientation(){
        lowerBound = upperBound + lowerBound;
        upperBound = lowerBound - upperBound;
        lowerBound = lowerBound - upperBound;
    }

    /*-------Getter Methods--------*/
    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }
}
