import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {


    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
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
          BinaryStdOut.write(t[suffixNumber], 8);
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int i, R = 256;
        int nxt = BinaryStdIn.readInt();
        ArrayList<Integer> t = new ArrayList<Integer>();
        int[] next, first;
        int[] count = new int[R + 1];
        int[] locations = new int[R];

        //Collect t and compute frequency counts
        while (!BinaryStdIn.isEmpty()) {
            t.add(new Integer(BinaryStdIn.readChar()));
            count[t.get(t.size() - 1) + 1]++;
        }

        next = new int[t.size()];
        first = new int[t.size()];

        //Compute inclusive sum reduce over frequency
        for (i = 0; i < R; i++)
          count[i+1] += count[i];

        //Move elements of t to proper position
        for (i = 0; i < t.size(); i++)
            first[count[t.get(i)]++] = t.get(i);

        for (i = 0; i < t.size(); i++) {
          while (t.get(locations[first[i]]) != first[i])  locations[first[i]]++;
          next[i] = locations[first[i]]++;
          //StdOut.printf("%d\n", next[i]);
        }

        for (i = 0; i < t.size(); i++){
             BinaryStdOut.write(first[nxt], 8);
             nxt = next[nxt];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
      if (args.length != 1)
          throw new IllegalArgumentException("Wrong number of arguments\n");

      switch (args[0]) {
          case "-":
              transform();
              break;
          case "+":
              inverseTransform();
              break;
          default:
              throw new IllegalArgumentException("First argument must be +|-\n");
      }
    }
}
