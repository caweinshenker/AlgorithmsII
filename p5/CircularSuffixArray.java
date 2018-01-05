
import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;


public class CircularSuffixArray {

    private final int CUTOFF = 2;
    private int[] index;

    public CircularSuffixArray(String s)  {
        if (s == null)
          throw new java.lang.IllegalArgumentException("Null string");

        index = new int[s.length()];
        for (int i = 0; i < s.length(); i++){
            index[i] = i;
        }

        sort(s, 0, index.length - 1, 0);
    }

    public int length()  {
        return index.length;
    }

    public int index(int i)  {
      if (i < 0 || i >= length())
        throw new java.lang.IllegalArgumentException("Index out of range");

      return index[i];
    }

    private int charAt(String s, int i, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt((index[i] + d) % index.length);
    }

    private void sort (String s, int lo, int hi, int d) {

      // cutoff to insertion sort for small subarrays
      if (hi <= lo + CUTOFF) {
        insertion(s, lo, hi, d);
        return;
      }

        int lt = lo, gt = hi;
        int v = charAt(s, lo, d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(s, i, d);
            if      (t < v) exch(index, lt++, i++);
            else if (t > v) exch(index, i, gt--);
            else              i++;
       }

       sort(s, lo, lt-1, d);
       if (v >= 0) sort(s, lt, gt, d+1);
       sort(s, gt+1, hi, d);
    }


    // sort from a[lo] to a[hi], starting at the dth character
    private  void insertion(String s, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(s, j, j-1, d); j--)
                exch(index, j, j-1);
    }

    // is v less than w, starting at character d
    private boolean less(String s, int v, int w, int d) {
        //assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < length(); i++) {
            if (charAt(s, v, i) < charAt(s, w, i)) return true;
            if (charAt(s, v, i) > charAt(s, w, i)) return false;
        }
        return false;
    }

    // exchange a[i] and a[j]
    private void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args)  {
        if (args.length > 0){
            for (int i = 0; i < args.length; i++) {
                CircularSuffixArray csa = new CircularSuffixArray(args[i]);
                StdOut.printf("Argument %d: %s\n", i, args[i]);
                StdOut.printf("String length: %d\n", csa.length());
                for (int suf = 0; suf < args[i].length(); suf++) {
                    StdOut.printf("Index of sorted suffix %d : %d\n", suf, csa.index(suf));
                }
                    StdOut.printf("\n");
              }
        }
    }
}
