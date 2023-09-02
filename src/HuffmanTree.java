// Joseph Quinn
// Arapahoe Community College: CSC1061 Computer Science II
// April 20, 2023:

//#############################
//HUFFMAN COMPRESSION ALGORITHM
//#############################

// This program takes a string input and will utilize a huffman compression algorithm
// to convert the string into a binary sequence
// It will  print  the frequency map and the corresponding character binary map
// It  then calculate the amount of bits required for the string in 8 and 7 bit ASCII
// and show the percentage of data saved using the compression algorithm
//
// Finally, utilizing the bitArray method. It will convert this binary sequence string into an efficient
// and usable bitArray for external data processing.

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.PriorityQueue;
import java.util.*;

public class HuffmanTree
{
    public static void main(String[] args)
    {
        //#####################
        //FORMATTING USER INPUT
        //#####################

        System.out.print("Enter String: ");
        Scanner input = new Scanner(System.in); // initiating scanner object and retrieving string from user input
        String word = input.nextLine();
        int lng = word.length();

        char[] wordArray = new char[lng]; //taking the string input and converting it into a char array
        for (int i = 0;i < lng;i++)
        {
            wordArray[i] = word.charAt(i);
        }


        int[] temp_freq = new int[lng];
        for(int i = 0; i <lng; i++) // for loop used to find the frequencies of each letter in the char array, then stores data into an integer array
        {
            temp_freq[i] = 1;
            for (int j = i + 1; j < lng; j++)
            {
                if (wordArray[i] == wordArray[j])
                {
                    temp_freq[i]++;
                    wordArray[j] = '0';
                }
            }
        }

        HashMap<Character,Integer> freqmap = new HashMap<Character,Integer>(); //hashmap creating for storing characters as keys and frequencies as values

        for(int i = 0; i <temp_freq.length; i++) //adding both arrays to the frequency hashmap, double checks to make sure '0' and ' ' aren't added to hashmap from frequency for loop
        {
            if(wordArray[i] != ' ' && wordArray[i] != '0')
            {
               freqmap.put(wordArray[i], temp_freq[i]);
            }
        }


        char[] ch = new char[freqmap.size()]; // creating char and frequency arrays for node objects
        int[] freq = new int[freqmap.size()];
        int count = 0;

        for (Map.Entry<Character,Integer> mapElement : freqmap.entrySet()) //iterating through frequency hashmap and adding its keys to the char array ands its values to the frequency array using count variable to determine array position
        {
            Character key = mapElement.getKey();
            int value = mapElement.getValue();
            ch[count] = key;
            freq[count] = value;
            count = count + 1;
        }

        //#####################
        //CREATING HUFFMAN TREE
        //#####################
        PriorityQueue<Node> Pq = new PriorityQueue<Node>(ch.length, new MyComparator()); //creating priority queue (utilizing our MyComparator class to compare the nodes), this will allow us to sort the nodes to build a min-heap

        for (int i = 0; i < ch.length; i++)
        {
            Node Lnode = new Node(freq[i], ch[i], null, null); // creating a leaf node object (from node class) for all unique characters

            Pq.add(Lnode); //adding leaf nodes to priority queue
        }

        Node root = null; // creating the root node, can be null for now as we have not pulled any nodes from teh priority queue

        while (Pq.size() > 1) // this while loop will continue to build the tree until there are no nodes remaining in the priority queue
        {
            Node node1 = Pq.peek(); // first extraction from the priority queue, extracting the lowest value from queue
            Pq.poll();

            Node node2 = Pq.peek(); // second extraction from queue
            Pq.poll();

            Node f1 = new Node(node1.val + node2.val,'-', node1, node2); // this is creating our first parent node which is going to be the sum values of both nodes previously extracted

            root = f1; // this parent node also becomes our 'current' root node

            Pq.add(f1);// then we just add this new combined node into our queue and repeat the process until our tree in constructed
        }

        //##################
        //PRINTING TREE DATA
        //##################

        HashMap<Character,String> binmap = new HashMap<Character,String>(); //creating hashmap to store characters as keys and their binary values as values
        binmap = (getbinmap(root)); //running getbinmap method on our root node to add keys and values to our binary hashmap

        String postcompression = ""; //empty string to store the post compression binary code of the original string

        wordArray = word.toCharArray(); //converting original string to char array (this is because when we found the frequency it changed our original word array

        for(int i = 0; i < wordArray.length; i++) //creating the complete binary code for our string by iterating through each char in our word and finding its corresponding value in our binary hashmap. Then enumerates the '0' or '1'
        {
            postcompression = postcompression + binmap.get(wordArray[i]);
        }


        System.out.println("[===Frequency Map===]"); //printing out frequency map
        for (Map.Entry<Character,Integer> mapElement : freqmap.entrySet())
        {
            Character key = mapElement.getKey();
            Integer value = mapElement.getValue();
            System.out.println(key + " : " + value);
        }


        System.out.println("[===Binary Map===]"); //printing out binary map
        for (Map.Entry<Character,String> mapElement : binmap.entrySet())
        {
            Character key = mapElement.getKey();
            String value = mapElement.getValue();
            System.out.println(key + " : " + value);
        }

        System.out.println("");
        System.out.println("Original String: " + word);
        System.out.println("|||---===Compression===---|||");
        System.out.println("Compressed String: " + postcompression); //printing full binary code
        System.out.println("");


        System.out.println("Original String would require " + word.length()*7 + " bits in 7-bit "); //printing more data
        System.out.println("Original String would require " + word.length()*8 + " bits in 8-bit ");
        System.out.println("Compressed String  requires " + postcompression.length() + " bits after Huffman compression");
        int memsaved = (int)(100 * (1 - ((float)postcompression.length()/(float)(word.length()*8))));
        System.out.println("This compression resulted in a roughly " + memsaved + "% reduction in memory space");

        log(postcompression, word); //logging compression data to DataLog.txt

        boolean[] compressionArray = bitArray(postcompression); //converts postcompression value from String -> bit using bitArray method

    }

    public static HashMap<Character, String> getbinmap(Node root) //this method will return a complete binary map will characters as keys and binary strings as values. Takes the root node as parameter
                                                                  // and uses the traversal method to recursively move down the tree collecting the leaf nodes binary codes
    {
        HashMap<Character, String> map = new HashMap<>();// creating hashmap
        traversal(root, "", map); //utilizing the traversal method to move down the tree
        return map;
    }

    //##########################
    //RECURSION TRAVERSAL METHOD
    //##########################

    // recursive method used to progress down the huffman tree adding leaf nodes to our binary hashmap
    // it takes a node, string, and map as parameters. The node is the starting point of the traversal, the string (code) will store the binary code for said node ('1' and '0')
    // and the map to write the data too. The method will first check to make sure the parameter node isn't empty, if it is it will exit the method.
    // Then it will check the left and right branch nodes. if both are empty this means we have a leaf node and can write the current string (code) binary value to our map
    // If the left or right branch nodes are NOT empty then it is not a branch node, and we begin recursion
    // This will create two new branches in recursion, one starting as if the left branch node is the root node, and one where the right branch node is the root
    // It will also at a '0' to the string (code) for the left branch, and a '1' to the string (code) for the right branch
    // It will then check the branch nodes of the new root nodes and see if they are leaf nodes themselves.
    // If they are we can add the string(code) to the map and use the return keyword to end the method branch completely
    // This will continue until there are no more leaf nodes in the tree.
    public static void traversal(Node node, String code, Map<Character, String> map)
    {
        if (node == null)
        {
            return;
        }
        if (node.left == null && node.right == null) {
            map.put(node.c, code);
            return;
        }
        traversal(node.left, code + "0", map);
        traversal(node.right, code + "1", map);
    }





    //##################
    //TXT LOGGING METHOD
    //##################

    // log method used to log compression data to Datalog.txt file located in /src/resources/
    //
    public static void log(String huffmanString, String word)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String timeStamp = dateFormat.format(cal.getTime());
        File log = new File("src/resources/DataLog.txt");

        try {
            FileWriter fileWriter = new FileWriter(log, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(dateFormat.format(cal.getTime()));
            bufferedWriter.newLine();
            bufferedWriter.write(word);
            bufferedWriter.write("->");
            bufferedWriter.write(huffmanString);
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.flush();
            bufferedWriter.close();

            System.out.println("Log Completed");
        }
        catch (IOException e)
        {
            System.out.println("COULD NOT LOG!!");
        }

    }


    //################################
    //STRING ->>> BIT ARRAY CONVERSION
    //################################

    // method used for creating a practical bitArray to be used for data manipulation
    // the postcompression value that was created useing the huffman tree methods is stored in a string value
    // this method converts the postcompression string to a boolean(bit) array to minimize memory storage and
    // to create a usable data structure for external usage.

    public static boolean[] bitArray (String huffmanCode)
    {
        int length = huffmanCode.length();
        boolean[] booleanArray = new boolean[huffmanCode.length()]; // using boolean array as it uses a true or false(0 or 1)
                                                                    // datatype in order to maximize efficiency for our compression

        for (int i = 0; i < length; i++) // filling boolean array
        {
            booleanArray[i] = huffmanCode.charAt(i) == '1';
        }

        return booleanArray;
    }




}

