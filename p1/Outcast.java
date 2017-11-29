
import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class Outcast {

   private WordNet net;

   public Outcast(WordNet wordnet) {
       
       if (wordnet == null) {
           throw new java.lang.IllegalArgumentException("WordNet is null");
       }

       net = wordnet;

   }   

   public String outcast(String[] nouns)  {

       if (nouns == null){
           throw new java.lang.IllegalArgumentException("Null args");
       }
       
       int minDistance = Integer.MIN_VALUE;
       String outcast = null;
       for (int i = 0; i < nouns.length; i++) {

           int curDistance = 0;

           for (int j = 0; j < nouns.length; j++) {
               if (i == j) continue;
               curDistance += net.distance(nouns[i], nouns[j]); 
           }

           if (curDistance > minDistance){
                minDistance = curDistance;
                outcast = nouns[i];
           }
       }

       return outcast;
   }
   
   public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
   
}