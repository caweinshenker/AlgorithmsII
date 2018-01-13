import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        int i, c;
        int[] aux, chars = new int[R];

        for (i = 0; i < R; i++) {
            chars[i] = i;
        }

        while (StdIn.hasNextChar()) {
            c = StdIn.readChar();
            aux = new int[R];
            for (i = 0; i < R; i++) {
                if (c == chars[i]) {
                    StdOut.printf("%c", (char) i);
                    for (int j = 1; j < R; j++) {
                      if (j <= i) aux[j] = chars[j -1];
                      else aux[j] = chars[j];
                    }
                    aux[0] = c;
                    chars = aux;
                    break;
                };
            }
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

      int i, c, ch;
      int[] aux, chars = new int[R];

      for (i = 0; i < R; i++) {
          chars[i] = i;
      }

      while (StdIn.hasNextChar()) {
          c = StdIn.readChar();
          ch = chars[c];
          StdOut.printf("%c", ch);
          aux = new int[R];

          for (i = 1; i < R; i++) {
              if (i <= c) aux[i] = chars[i -1];
              else aux[i] = chars[i];
          }
          
          aux[0] = ch;
          chars = aux;
          }
      }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
      if (args.length != 1)
          throw new java.lang.IllegalArgumentException("Wrong number of arguments\n");

      switch (args[0]) {
          case "-":
              encode();
              break;
          case "+":
              decode();
              break;
          default:
              throw new java.lang.IllegalArgumentException("First argument must be +|-\n");
          }
      }
}
