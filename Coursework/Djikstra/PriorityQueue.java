package Coursework.Djikstra;

import java.util.Arrays;

//File contains priority queue class implemented as a binary min heap which guarantees the smallest element path-length wise
//to appear in array position 0. The file contains all PQ operations such as insertion, deletion, and fixing the heap structure.

public class PriorityQueue {
    //Initial size of the queue.
    private int qSize= 1000;
    //Priority queue as heap array.
    private QNode[] priorityQueue = new QNode[qSize];
    //Keeps track of at which index the next element should be inserted.
    private int insertionIndex = 0;

    //Method insert given QNode into the min heap based on the euclidean length.
    public void insertElement(QNode node){
        //Method checks whether the insertion will overflow the array and if it does double its size.
        expandArray();
        //Inserts the element in the currently final position of the array.
        priorityQueue[insertionIndex] = node;
        //Bubbles the element up so that a min heap is maintained.
        heapifyUp();
        //Prepares the insertion index for the next insertion.
        insertionIndex++;
    }

    //Method used for bubbling Node up the tree into the correct position.
    private void heapifyUp(){
        int heapifyTargetIndex = insertionIndex;
        //If the target index euclidean length is smaller than its parent then their positions get swapped.
        while(priorityQueue[heapifyTargetIndex].getEuclideanLength() < priorityQueue[getParentIndex(heapifyTargetIndex)].getEuclideanLength()){
            //Intermediate used for swapping parent and target.
            QNode intermediate = priorityQueue[heapifyTargetIndex];
            priorityQueue[heapifyTargetIndex] = priorityQueue[getParentIndex(heapifyTargetIndex)];
            priorityQueue[getParentIndex(heapifyTargetIndex)] = intermediate;
            //Updates the target index to its parent to perform the swap again if applicable.
            heapifyTargetIndex = getParentIndex(heapifyTargetIndex);
        }
    }

    //Returns if the array is empty //Debugging method.
    public boolean isEmpty(){
        return insertionIndex == 0;
    }

    //Returns the first element of the array and fixes the array to remain in a min heap structure.
    public QNode removeFirstElement(){
        QNode firstElem = priorityQueue[0];
        heapifyDown();
        return firstElem;
    }

    //Method called when element removed used to fill in the gap at the beginning of the array and restoring the structure by traversing down the heap tree.
    private void heapifyDown(){
        //Fill first index with the most recently inserted element.
        priorityQueue[0] = priorityQueue[insertionIndex -1];
        //Reduce insertion index in order to prevent a gap in the array on the next index.
        insertionIndex--;
        //Sets the last element that was transferred to index 0 to null.
        priorityQueue[insertionIndex] = null;
        //Moves the element at index 0 to its correct location in the min heap.
        traverseDownwards(0);
    }

    //Recursive function traverses the heap downwards until it satisfies heap requirements.
    private void traverseDownwards(int targetIndex){
        //Intermediate node assists in swapping parent with selected child.
        QNode intermediate = priorityQueue[targetIndex];
        //No left child means this is the final node in the heap tree hence traversal is done.
        if(hasLeftChild(targetIndex)){
            //Left child is smaller.
            if(isLeftSmaller(targetIndex)){
                //Left child is smaller than parent.
                if(priorityQueue[targetIndex].getEuclideanLength() > priorityQueue[getLeftChildIndex(targetIndex)].getEuclideanLength()){
                    //Swap parent with left child.
                    priorityQueue[targetIndex] = priorityQueue[getLeftChildIndex(targetIndex)];
                    priorityQueue[getLeftChildIndex(targetIndex)] = intermediate;
                    //Calls the function again on the swapped parent to move it into the correct location.
                    traverseDownwards(getLeftChildIndex(targetIndex));
                }
            }
            //Right child is smaller.
            else{
                //Right child is smaller than parent.
                if(priorityQueue[targetIndex].getEuclideanLength() > priorityQueue[getRightChildIndex(targetIndex)].getEuclideanLength()){
                    //Swap parent with right child.
                    priorityQueue[targetIndex] = priorityQueue[getRightChildIndex(targetIndex)];
                    priorityQueue[getRightChildIndex(targetIndex)] = intermediate;
                    //Calls the function again on the swapped parent to move it into the correct location.
                    traverseDownwards(getRightChildIndex(targetIndex));
                }
            }
        }
    }

    //Method calculates whether the left child is smaller than the right child.
    //Method used in the heapify down method.
    private boolean isLeftSmaller(int targetIndex){
        int leftIndex = getLeftChildIndex(targetIndex);
        int rightIndex = getRightChildIndex(targetIndex);

        //If right child doesn't exist then left child must be smaller.
        if(priorityQueue[rightIndex] == null){
            return true;
        }
        //Left is smaller.
        else if(priorityQueue[leftIndex].getEuclideanLength() <= priorityQueue[rightIndex].getEuclideanLength()){
            return true;
        }
        //Right must be smaller.
        else{
            return false;
        }
    }

    //Method checks whether the target index has left child.
    private boolean hasLeftChild(int parentIndex){
        int leftChild = getLeftChildIndex(parentIndex);
        //makes sure that accessing the left child doesn't cause an array index out of bounds error
        if(leftChild >= qSize){
            return false;
        }
        //Will be null if the left child is accessible but empty.
        else if(priorityQueue[leftChild] == null){
            return false;
        }
        return true;
    }

    //Getter methods.
    private int getLeftChildIndex(int parentIndex){
        return (2 * parentIndex) + 1;
    }

    private int getRightChildIndex(int parentIndex){
        return (2 * parentIndex) + 2;
    }

    private int getParentIndex(int childIndex){
        return (childIndex -1)/2;
    }

    //Method to expand priority queue array assuming the insertion would overflow its previous size.
    public void expandArray(){
        if(qSize == insertionIndex){
            qSize *=2;
            //old array copied to a new one, can't use the faster Arrays.clone as it does not accept the modified size
            priorityQueue = Arrays.copyOf(priorityQueue , priorityQueue.length * 2);
        }
    }

    //Prints all nodes euclidean length //Debugging method used to verify min heap correct function.
    public void printQ(){
        for(QNode node : priorityQueue){
            if(node != null){
                System.out.println(node.getEuclideanLength());
            }
        }
        System.out.println();
    }
}
