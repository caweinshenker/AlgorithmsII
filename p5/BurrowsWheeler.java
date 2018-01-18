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
        int i, j, pos, nxt = BinaryStdIn.readInt();
        int[] t = new int[];
        int[] next = new int[];
        int[] first = new int[];
        int[] count = new int[R + 1];
        int[] locations = new int[R];

        //Collect t and compute frequency counts
        for (i = 0; i < t.length; i++) {
            t[i] = BinaryStdIn.readChar();
            count[t[i] + 1]++;
        }

        //Compute inclusive sum reduce over frequency
        for (i = 0; i < R; i++)
          count[i+1] += count[i];

        //Move elements of t to proper position
        for (i = 0; i < R; i++) {
            pos = count[t[i]]++;
            first[pos] = t[i];
        }

        for (i = 0; i < t.length; i++) {
          while(t[locations[first[i]]] != first[i])
              locations[first[i]]++
          next[i] = locations[first[i]]]++;
        }

        for (i = nxt; i != t.length - 1; i++) {
            StdOut.printf("%c", t[nxt]);
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
