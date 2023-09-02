// Joseph Quinn
// Arapahoe Community College: CSC1061 Computer Science II
// April 20, 2023:

class Node //node class utilized to create 'node' objects
{
    int val; // frequency of the character
    char c; // name of the character

    Node left; // left node of the current node
    Node right; // right node of the current node

    public Node(int val, char c, Node left, Node right) // constructor for object variables
    {
        this.val = val;
        this.c = c;
        this.left = left;
        this.right = right;
    }
}