import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class BurrowsWheeler {


    private static CircularSuffixArray csa;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = StdIn.readString();
        csa = new CircularSuffixArray(s);
        int suffixNumber, ch, firstSuffixIndex = 0, sortedSuffixIndex = 0;
        int[] t = new int[s.length()];


        for (suffixNumber = 0; suffixNumber < s.length(); suffixNumber++) {
          sortedSuffixIndex = csa.index(suffixNumber);
          if (sortedSuffixIndex == 0) firstSuffixIndex = suffixNumber;
          ch = s.charAt((sortedSuffixIndex + s.length() - 1) % s.length());
          t[suffixNumber] = ch;
        }

        BinaryStdOut.write(firstSuffixIndex);
        for (suffixNumber = 0; suffixNumber < t.length; suffixNumber++)
          BinaryStdOut.write((char) t[suffixNumber]);
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int i, j, firstSuffixIndex = BinaryStdIn.readInt();
        int[] t = new int[csa.length()], next = new int[csa.length()];

        for (i = 0; i < t.length; i++) {
          t[i] = BinaryStdIn.readChar();
        }

        for (j = 0; j < csa.length(); j++) {
            i = csa.index(j);
            next[i] = csa.index(j + 1);
        }
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
