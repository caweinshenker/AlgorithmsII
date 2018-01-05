import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = StdIn.readString();
        StringBuilder sb = new StringBuilder();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
          int loc = csa.index(i);
          if (loc == 0) index = i;
          sb.append(s.charAt((loc + s.length()) % s.length()));
        }
        StdOut.printf("%d\n%s", csa.index(0), sb.toString());
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {

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
