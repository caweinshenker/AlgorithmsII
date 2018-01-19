import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        int i, c;
        int[] chars = new int[R];

        for (i = 0; i < R; i++)
            chars[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            for (i = 0; i < R; i++) {
                if (c == chars[i])
                    break;
            }

            for (int j = i; j > 0; j--)
              chars[j] = chars[j-1];

            chars[0] = c;

            BinaryStdOut.write(i, 8);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

      int i, c, ch;
      int[] chars = new int[R];

      for (i = 0; i < R; i++)
          chars[i] = i;

      while (!BinaryStdIn.isEmpty()) {
          c = BinaryStdIn.readChar();
          ch = chars[c];
          for (i = c; i > 0; i--) {
              chars[i] = chars[i - 1];
          }
          chars[0] = ch;
          BinaryStdOut.write(ch, 8);
      }

       BinaryStdOut.flush();

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
