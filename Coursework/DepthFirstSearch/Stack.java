package Coursework.DepthFirstSearch;

//File contains custom implementation of the stack class using a linked list approach

public class Stack {

    //Tracks the stack head.
    private StackNode head;

    //Stack constructor creates an empty node as the head.
    public Stack(){
        head = null;
    }

    //Takes int value and creates a node which is then added to the top of the stack.
    public void push(int data , int[] neighbours , double pathLength){
        StackNode dataStackNode = new StackNode(data , head , pathLength , neighbours);
        head = dataStackNode;
    }

    //Removes an element from the stack and returns the data int value.
    public StackNode pop(){
        //checks if stack is empty
        if(head == null){
            return null;
        }
        //Puts the head node into a temporary container then updates the head to its parent.
        //The temporary container is then returned.
        StackNode data = head;
        head = head.getParentNode();
        return data;
    }
    //Returns the head node gives me the option to access unvisited neighbour array.
    public StackNode peek(){
        return head;
    }
}
