package Coursework.DepthFirstSearch;

//File contains stack node which is used to interact with the Stack class.
//It stores information such as current path length, city number, unvisited neighbours, and the parent node.

public class StackNode {

    //Keeps track of the city identity.
    private int cityNumber;
    //Keeps track of city previously unvisited neighbour cities.
    private int[] unvisitedNeighbours;
    //Keeps track of cumulative euclidean distance up to this node city, this reduces the calculation time when a complete path is formed.
    private double currentPathLength;
    //Parent Node kept so that stack traversal via linked list is possible.
    private StackNode parentNode;

    //Node constructor.
    public StackNode(int cityNumber , StackNode parentNode , double currentPathLength , int[] unvisitedNeighbours){
        this.cityNumber = cityNumber;
        this.parentNode = parentNode;
        this.unvisitedNeighbours = unvisitedNeighbours;
        this.currentPathLength = currentPathLength;
    }

    /*----------Getter methods----------*/
    public int getCityNumber() {
        return cityNumber;
    }

    public int[] getUnvisitedNeighbours() {
        return unvisitedNeighbours;
    }

    public double getCurrentPathLength() {
        return currentPathLength;
    }

    public StackNode getParentNode() {
        return parentNode;
    }
}