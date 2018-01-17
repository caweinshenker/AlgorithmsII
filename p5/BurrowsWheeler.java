import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class BurrowsWheeler {

    private static int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = StdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
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
        int i, j, nxt = BinaryStdIn.readInt();
        char[] t = new char[csa.length()];
        int[] next = new int[csa.length()];
        int[] count = new int[R + 1];

        for (j = 0; j < t.length; j++) {
            t[j] = BinaryStdIn.readChar();
            count[t[j]]++;
        }








        LSD.sort(t, 1);

        for (i = 0; i < t.length; i++) {
            StdOut.printf("%s", t[nxt]);
            nxt = next[nxt];
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
