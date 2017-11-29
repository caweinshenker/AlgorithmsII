import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;


public class SAP {

   private Digraph graph;

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {

       if (G == null)
            throw new java.lang.IllegalArgumentException("Null graph");

       graph = new Digraph(G);
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w){

        BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(graph, w);
        return minSAPLength(bfpV, bfpW);
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w){

        BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(graph, w);
        return minSAPAncestor(bfpV, bfpW);
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w){

        checkNullArgsVW(v, w);
        BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(graph, w);
        return minSAPLength(bfpV, bfpW);
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
       
       checkNullArgsVW(v ,w);
       BreadthFirstDirectedPaths bfpV = new BreadthFirstDirectedPaths(graph, v);
       BreadthFirstDirectedPaths bfpW = new BreadthFirstDirectedPaths(graph, w);
       return minSAPAncestor(bfpV, bfpW);
   }

    private void checkNullArgsVW(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null || w == null)
            throw new IllegalArgumentException("Null args");
    }

    private int sizeOfIterable(Iterable<Integer> iterable){
        int size = 0;
        for (int i : iterable){
            size++;
        }
        return size;
   }

   private int minSAPLength(BreadthFirstDirectedPaths bfpV, BreadthFirstDirectedPaths bfpW) {
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++){
            if (bfpV.hasPathTo(i) && bfpW.hasPathTo(i))  {
                int curLength = this.sizeOfIterable(bfpV.pathTo(i)) + this.sizeOfIterable(bfpW.pathTo(i)) - 2;
                if (curLength < minLength){
                    minLength = curLength;
                } 
            }
        }
        return minLength < Integer.MAX_VALUE ? minLength : -1;
   }

   private int minSAPAncestor(BreadthFirstDirectedPaths bfpV, BreadthFirstDirectedPaths bfpW) {
        int minLength = Integer.MAX_VALUE;
        int ancestorWithShortestPath = -1;
        for (int i = 0; i < graph.V(); i++){
             if (bfpV.hasPathTo(i) && bfpW.hasPathTo(i)){
                int curLength = this.sizeOfIterable(bfpV.pathTo(i)) + this.sizeOfIterable(bfpW.pathTo(i)) - 2;
                if (curLength < minLength){
                    minLength = curLength;
                    ancestorWithShortestPath = i;
                } 
            }
        }
        return ancestorWithShortestPath;
   }

      public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
