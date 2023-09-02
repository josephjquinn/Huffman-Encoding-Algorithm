// Joseph Quinn
// Arapahoe Community College: CSC1061 Computer Science II
// April 20, 2023:

import java.util.Comparator; //importing comparator class, this will allow us to compare our nodes in order to create a working min-heap

class MyComparator implements Comparator<Node> // my comparator implements comparator class utilizing the node class, what this will do is allow us to access our node class objects under the imported comparator class
{
    public int compare(Node x, Node y) //function to compare 2 nodes for min heap
    {
        return x.val - y.val;
    }
}