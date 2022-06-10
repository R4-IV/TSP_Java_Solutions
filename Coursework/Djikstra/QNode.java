package Coursework.Djikstra;

//File contains the QNode class which is created to interact with the priority queue

public class QNode {
    //Tracks parent node allows for backward traversal to get the path.
    private QNode parent;
    //Keeps track of the city id.
    private int cityNum;
    //Cumulative euclidean length up to this city.
    private double euclideanLength;
    //Current tour length.
    private int pathLength;

    //Constructor.
    public QNode(QNode parent , int cityNum , double euclideanLength , int pathLength){
        this.parent = parent;
        this.cityNum = cityNum;
        this.euclideanLength = euclideanLength;
        this.pathLength = pathLength;
    }

    /*-------------Getter methods--------------*/
    public double getEuclideanLength() {
        return euclideanLength;
    }

    public int getCityNum() {
        return cityNum;
    }

    public int getPathLength() {
        return pathLength;
    }

    public QNode getParent() {
        return parent;
    }
}

