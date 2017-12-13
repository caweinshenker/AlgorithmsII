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

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
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

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean[][] visited = new boolean[rows][cols];
                ArrayList<String> words = new ArrayList<String>();
                String seed = Character.toString(board.getLetter(row, col));
                dfs(seed, row, col, words, visited, board);
                validWords.addAll(words);
            }
        }
        return validWords;
    }


    private void dfs(String curString, int row, int col, ArrayList<String> words, boolean[][] visited, BoggleBoard board) {

        //StdOut.printf("%s\n", curString);
        if (visited[row][col]) return;
        if (!tst.keysWithPrefix(curString).iterator().hasNext()) return;
        if (tst.contains(curString)) words.add(curString);
        boolean[][] visitedCopy = new boolean[rows][cols];
        for (int i = 0; i < rows; i++){
          visitedCopy[i] = visited[i].clone();
        }
        visitedCopy[row][col] = true;


        for (int rowDelta = Math.max(0, row - 1); rowDelta <= Math.min(row + 1, rows - 1); rowDelta++) {
            for (int colDelta = Math.max(0, col - 1); colDelta <= Math.min(col + 1, cols - 1) ; colDelta++) {
                String newSeed = curString + Character.toString(board.getLetter(rowDelta, colDelta));
                dfs(newSeed, rowDelta, colDelta, words, visitedCopy, board);
            }
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("Null word cannot be scored\n");

        int score = 0;
        String wordCopy = word;
        BoyerMoore bm = new BoyerMoore("Qu");
        while (wordCopy.length() > 0) {
            int index = bm.search(wordCopy);
            if (index < wordCopy.length()) score--;
            wordCopy = wordCopy.substring(index);
        }

        if      (word.length() < 3)  return score + 0;
        else if (word.length() < 5)  return score + 1;
        else if (word.length() == 5) return score + 2;
        else if (word.length() == 6) return score + 3;
        else if (word.length() == 7) return score + 5;
        else return score + 11;
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
