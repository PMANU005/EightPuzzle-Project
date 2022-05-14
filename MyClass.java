import java.util.*;
import java.util.stream.Collectors;
import java.util.Scanner;
//defining our own comparator to sort according to the astar values in a node.
class ourcomparator implements Comparator<Node> {
    // we are overriding compare method from comparator class to sort the queue according to astar values.
    public int compare(Node n1, Node n2) {
        //we are returning -1 because we want order to be in ascending order
        if (n1.getAstar() < n2.getAstar()) {
            return -1;
        } else if (n1.getAstar() > n2.getAstar()) {
            return 1;
        }
        //if both nodes astar values are equal then we are sorting according to smallest g(n).
        else {
            //sorting the nodes in ascending order comparing with nodes g(n) when nodes astar values are equal.
            if (n1.g < n2.g) {
                return -1;
            } else if (n1.g > n2.g) {
                return 1;
            } else {

                return 0;
            }
        }
    }
}
//defining node class
class Node{
    //defining instance variables array a, g(n),h(n)
    int[][] a;
    int g;
    int h;
    //Initialization node class with varaiables
    Node(int g,int h,int[][] a){
        this.g=g;
        this.h=h;
        this.a=a;
    }
    //Defining a function Astar to return f(n) which is equal to g(n)+h(n)
    int getAstar(){
        return g+h;
    }
}
public class MyClass {
    //Defining function of manhattan heuristic

    public int manhattanheuristic(int[][] a) {
        int r = a.length;
        int c = a[0].length;
        int count = 0;
        int ans = 0;
        //updating the answer variable only when value is not equal to corresponding count value.
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (a[i][j] != 0) {
                    //finding corresponding element in the target 2d array by getting corresponding row and column.
                    int row = (a[i][j]-1) / c;
                    //since all elements are sequentially arranged in target array we can simply take percentile of 3 to get target loc.
                    int column = (a[i][j]-1) % c;
                    ans = ans + Math.abs(i - row) + Math.abs(j - column);
                }
            }
        }
        return ans;
    }
    //I am defining a function to assign a given arrayvalues to other array.
    public int[][] assignthearray(int[][] given) {
        int[][] newarr = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newarr[i][j] = given[i][j];
            }
        }
        return newarr;
    }
    public int misplacedheuristic(int[][] a){
        int r=a.length;
        int c=a[0].length;
        int count=0;
        int ans=0;
        //updating the answer variable only when value is not equal to corresponding count value.
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                count=count+1;
                if(a[i][j]!=0){
                    if(a[i][j]!=count){
                        ans=ans+1;
                    }
                }
            }
        }
        return ans;
    }
    //I am defining another function to ocnvert array to list.
    public List<List<Integer>> convertarrtoList(int[][] arr) {

        List<List<Integer>> nestedLists =
                Arrays.
                        //Convert the 2d array into a stream.
                                stream(arr).
                        //Map each 1d array (internalArray) in 2d array to a List.
                                map(
                                //Stream all the elements of each 1d array and put them into a list of Integer.
                                internalArray -> Arrays.stream(internalArray).boxed().collect(Collectors.toList()
                                )
                                //Put all the lists from the previous step into one big list.
                        ).collect(Collectors.toList());
        return nestedLists;
    }
    public static void main(String args[]) {
        int i1 = 0;
        int j1 = 0;
        //int[][] ;b={{1,2,3},{4,5,6},{0,7,8}};
        int[][] b;
        int[][] target = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int r=target.length;
        int c=target[0].length;
        Scanner input = new Scanner(System.in);
        System.out.println("enter the difficulty for the Matrix");
        //hardcoded a random array if difficulty value is 1.
        int difficulty = input.nextInt();
        if (difficulty == 1) {
            b = new int[][]{{1, 2, 3}, {5, 0, 6}, {4, 7, 8}};
        } else {
            b = new int[3][3];
            for (int row = 0; row < r; row++) {
                for (int col = 0; col < c; col++) {
                    System.out.println("enter the elementss for the Matrix");
                    b[row][col] = input.nextInt();
                }
            }
        }
        System.out.println("enter the heuristic you want to use");
        int typeofheuristic=input.nextInt();
        // defining the directions array to add children into the queue.
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int ichild;
        int jchild;
        int noofnodes = 0;
        int maxqsize = 0;
        Node proot;
        Node child;
        Stack<Node> st = new Stack<Node>();
        Queue<Node> qu = new LinkedList<Node>();
        //Defining hash set to see that parent is not added in the queue again by children.
        HashSet<List<List<Integer>>> h = new HashSet<List<List<Integer>>>();
        MyClass m1 = new MyClass();
        //creating the Node class with g(n),h(n),b array
        //hardcoded heuristic type with 1,2,3 to define uniform,misplacedheuristic and manhattanheuristic.
        if(typeofheuristic==1){
            proot = new Node(0,0, b);}
        else if(typeofheuristic==2){
            proot = new Node(0, m1.misplacedheuristic(b), b);}
        else{
            proot = new Node(0, m1.manhattanheuristic(b), b);
        }
        //In java we can implement heap using priority queue.
        //priority queue contains pair class of node and sorted the queue using our own comparator which sorts the queue using nodes astar value.
        PriorityQueue<Node> q = new PriorityQueue<Node>(new ourcomparator());
        q.add(proot);
        while (!q.isEmpty() && q.size() < 200000) {
            Node temp = q.poll();
            //Incrementing the node when a node is popped.
            noofnodes = noofnodes + 1;
            // Converting the 2d array into list.
            List<List<Integer>> nestedListsfunc = m1.convertarrtoList(temp.a);
            //adding into hashset to avoid repeated parents.
            h.add(nestedListsfunc);
            st.add(temp);
            qu.add(temp);
            //System.out.println("one element popped");
            int[][] arr = new int[r][c];
            //declaring a new array and making it equal to current popped node array
            arr = m1.assignthearray(temp.a);
            // getting zero location of the popped node thereby we can add its children into the queue.
            out:
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (arr[i][j] == 0) {
                        //System.out.println("poppedzero"+i+" coordinates "+j+" space "+temp.g+" g and h "+temp.h);
                        i1 = i;
                        j1 = j;
                        break out;
                    }
                }
            }
            //checking whether popped node is goal state
            if (Arrays.deepEquals(arr, target)) {

              int tl=0;
              while(tl<2 && !qu.isEmpty()) {
                    //popping the nodes to print the values.
                    Node out = qu.poll();
                    int[][] outarr = m1.assignthearray(out.a);
                    List<List<Integer>> li = m1.convertarrtoList(outarr);
                    System.out.println("first two nodes  a g(n) = " + out.g + " and h(n) = " + out.h + " is " + li);
                    tl++;
                }
                //to print last two nodes
                int t=0;
                while(t<2 && !st.isEmpty())
                {
                    //popping the nodes to print the values.

                    Node out = st.pop();
                    int[][] outarr = m1.assignthearray(out.a);
                    List<List<Integer>> li = m1.convertarrtoList(outarr);
                    System.out.println("Last two nodes g(n) = " + out.g + " and h(n) = " + out.h + " is " + li);
                    t++;
                }
                System.out.println("Goal State!");
                System.out.println("Solution depth was: " + temp.g);
                System.out.println("Number Of Nodes  expanded :" + noofnodes);
                System.out.println("Maximum Queue size: " +  maxqsize);
                break;
            }
            //if poppednode is not equal to goal state then add it's children
            else {
                // to swap zero with 4 directions.
                for (int[] direction : directions) {
                    int[][] arrchild = new int[3][3];
                    arrchild = m1.assignthearray(arr);
                    //updating i and j by exploring 4 directions
                    ichild = i1 + direction[0];
                    jchild = j1 + direction[1];
                    //checking the boundary conditions
                    if (ichild >= 0 && ichild < 3 && jchild >= 0 && jchild < 3) {
                        //swapping the the zero with the updated inew and jnew value in the array
                        arrchild[i1][j1] = arr[i1][j1] + arr[ichild][jchild];
                        arrchild[ichild][jchild] = 0;
                        //converting this new array into list
                        List<List<Integer>> nestedLists1 = m1.convertarrtoList(arrchild);
                        //creating a node for the children only if the children doesn't exist in the hashset
                        if (!h.contains(nestedLists1)) {
                            //checking heuristic to call correspoding heuristic function.
                            //create node class with updated g(n) which will be current level +1 and h(n) and newarray
                            if(typeofheuristic==1){
                                child = new Node(temp.g + 1, 0, arrchild);}
                            else if(typeofheuristic==2){
                                child = new Node(temp.g + 1, m1.misplacedheuristic(arrchild), arrchild);}
                            else{
                                child = new Node(temp.g + 1, m1.manhattanheuristic(arrchild), arrchild);
                            }
                            //add the child into the queue
                            q.add(child);
                            maxqsize = Math.max(maxqsize, q.size());
                        }
                    }
                }

            }

        }

    }
}









