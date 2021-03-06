import edu.princeton.cs.algs4.*;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;

public class BoggleSolver
{

    private TrieSET tst;
    private int rows;
    private int cols;
    boolean[][] visited;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        //Quick3string.sort(dictionary);
        tst = new TrieSET();

        for (int i = 0; i < dictionary.length; i++) {
            tst.add(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new HashSet<String>();
        rows = board.rows();
        cols = board.cols();
        visited = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String seed = Character.toString(board.getLetter(row, col));
                if (seed.equals("Q")) seed="QU";
                dfs(seed, row, col, validWords, board);
            }
        }
        return validWords;
    }


    private void dfs(String curString, int row, int col, Set<String> words, BoggleBoard board) {

        //StdOut.printf("Dead end: %s %d\n", curString, curString.length());
        if (!tst.keysWithPrefix(curString).iterator().hasNext()) {
          return;
        }
        if (curString.length() >= 3 && tst.contains(curString)) words.add(curString);

        visited[row][col] = true;

        for (int rowDelta = Math.max(0, row - 1); rowDelta < Math.min(row + 2, rows); rowDelta++) {
            for (int colDelta = Math.max(0, col - 1); colDelta < Math.min(col + 2, cols) ; colDelta++) {
                if (visited[rowDelta][colDelta]) continue;
                String newSeed = Character.toString(board.getLetter(rowDelta, colDelta));
                if (newSeed.equals("Q")) newSeed = "QU";
                newSeed = curString + newSeed;
                dfs(newSeed, rowDelta, colDelta, words, board);
            }
        }

        visited[row][col] = false;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("Null word cannot be scored\n");

        int length = word.length();

        if (!tst.contains(word) || length < 3) return 0;

        if      (length < 5)  return 1;
        else if (length == 5) return 2;
        else if (length == 6) return 3;
        else if (length == 7) return 5;
        else return 11;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
