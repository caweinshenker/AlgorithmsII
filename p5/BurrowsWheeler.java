import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class BurrowsWheeler {

    private int firstSuffixIndex;
    private int[] t;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = StdIn.readString();
        t = new int[s.length()];
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int sortedSuffixIndex = 0;

        for (int suffixNumber = 0; suffixNumber < s.length(); suffixNumber++) {
          sortedSuffixIndex = csa.index(suffixNumber);
          if (sortedSuffixIndex == 0) firstSuffixIndex = suffixNumber;
          int ch = s.charAt((sortedSuffixIndex + s.length() - 1) % s.length());
          t[suffixNumber] = ch;
        }
        BinaryStdOut.write(firstSuffixIndex);
        BinaryStdOut.write(new String(t));
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        String s = BinaryStdIn.readString();
        int[] next = new int[s.length()];


    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
      if (args.length != 1)
          throw new java.lang.IllegalArgumentException("Wrong number of arguments\n");

      switch (args[0]) {
          case "-":
              transform();
              break;
          case "+":
              inverseTransform();
              break;
          default:
              throw new java.lang.IllegalArgumentException("First argument must be +|-\n");
      }
    }
}
