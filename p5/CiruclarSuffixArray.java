
import java.util.*;
import java.lang.*;

public class CircularSuffixArray {

    private String s;
    private int[] arr;

    public CircularSuffixArray(String s)  {
        this.s = s;

        arr = new int[s.length()];
        for (int i = 0; i < s.length(); i++){
            arr[i] = i;
        }
        Arrays.sort(arr, new SuffixSort(s));
    }

    public int length()  {
        return s.length();
    }

    public int index(int i)  {
      return arr[i];
    }

    //TODO
    public static void main(String[] args)  { //required
    }
}


class SuffixSort implements Comparator<Integer> {

    private String s;

    public SuffixSort(String s) {
        this.s = s;
    }

    public int compare(Integer a, Integer b) {
        String suffixA = s.substring(a, s.length()) + s.substring(0, a);
        String suffixB = s.substring(b, s.lenght()) + s.substring(0, b);
        return suffixA.compareTo(suffixB);
    }

}
