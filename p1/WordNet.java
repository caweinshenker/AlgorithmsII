import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;



public class WordNet {

    private HashMap<Integer, String> idToNoun;
    private HashMap<String, ArrayList<Integer>> synsets;
    private Digraph graph;
    private int vertices;


    public WordNet(String synsetFile, String hypernyms) {
        if (synsetFile == null || hypernyms == null)
            throw new java.lang.IllegalArgumentException("Null constructor arguments");

        initVertices(synsetFile);
        graph = new Digraph(vertices);
        initEdges(hypernyms);

        validateGraph();
     }

    public Iterable<String> nouns() {
      List<String> nounsList = new ArrayList<String>();
      for (String noun: synsets.keySet())
          nounsList.add(noun);
      return nounsList;
    }

    public boolean isNoun(String word) {
        if (word == null) {
          throw new java.lang.IllegalArgumentException("Null constructor arguments");
        }

        return synsets.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.IllegalArgumentException("Null constructor arguments");

        SAP sap = new SAP(graph);
        ArrayList<Integer> aIds = synsets.get(nounA);
        ArrayList<Integer> bIds = synsets.get(nounB);
        return sap.length(aIds, bIds);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.IllegalArgumentException("Null constructor arguments");

        SAP sap = new SAP(graph);
        int ancestorId = sap.ancestor(synsets.get(nounA), synsets.get(nounB));
        return idToNoun.get(ancestorId);
  }

    public static void main(String[] args) {
      if (args == null)
        throw new java.lang.IllegalArgumentException("Null constructor arguments");

      WordNet net = new WordNet(args[0], args[1]);

    }

    private void initVertices(String synsetFile) {

      synsets = new HashMap<String, ArrayList<Integer>>();
      idToNoun = new HashMap<Integer, String>();
      In synsetsIn = new In(synsetFile);
      String line;

      while ((line=synsetsIn.readLine()) != null) {
        int id = Integer.parseInt(line.split(",")[0]);
        List<String> synonyms = Arrays.asList(line.split(",")[1].split(" "));

        for (String synonym : synonyms){
            idToNoun.put(id, synonym);
            if (synsets.get(synonym) == null)
                synsets.put(synonym, new ArrayList<Integer>()); 
            synsets.get(synonym).add(id);
            //StdOut.printf("Synsets has key: %s : %d %d\n", synonym, synsets.containsKey(synonym) ? 1 : 0);
         }
         vertices++;
       }
    }

    private void initEdges(String hypernyms){
        In hypernymsIn = new In(hypernyms);
        String line;

        while ((line=hypernymsIn.readLine()) != null) {

          List<String> relationship = Arrays.asList(line.split(","));
          int synset = Integer.parseInt(relationship.get(0));

          for (int i = 1; i < relationship.size(); i++){
            graph.addEdge(synset, Integer.parseInt(relationship.get(i)));
          }
        }
    }

    private void validateGraph() {
        validateNoCycles();
        validateSingleRoot();
    }

    private void validateNoCycles() {
        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle())
          throw new java.lang.IllegalArgumentException("Cyclic graph");

    }

    private void validateSingleRoot() {
        DepthFirstOrder dfsOrder = new DepthFirstOrder(graph);
        int root = dfsOrder.post().iterator().next();

        DirectedDFS directedDFS = new DirectedDFS(graph.reverse(), root);
        for (int i = 0; i < graph.V(); i++){
          if (!directedDFS.marked(i)){
            throw new java.lang.IllegalArgumentException("Multiple roots");
          }
        }
    }
}
