import java.util.Comparator;
import java.util.Arrays;

public class CircularSuffixArray {
    private final String s;
    private final int sLen; // s length
    private final Integer[] suffixArr;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        sLen = length();
        suffixArr = new Integer[sLen];
        for (int i = 0; i < sLen; i++) {
            suffixArr[i] = i;
        }
        Arrays.sort(suffixArr, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                for (int i = 0; i < sLen; i++) {
                    // check a/b is out of bound
                    if (a >= sLen) a -= sLen;
                    if (b >= sLen) b -= sLen;
                    char charA = s.charAt(a);
                    char charB = s.charAt(b);
                    if (charA < charB) return -1;
                    else if (charA > charB) return +1;
                    else {
                        a++;
                        b++;
                    }
                }
                return 0;
            }
        });
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= sLen) throw new IllegalArgumentException();
        return suffixArr[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csArr = new CircularSuffixArray("AAABBBBBBB");
        for (int i = 0; i < csArr.length(); i++) {
            System.out.println(csArr.index(i));
        }
    }

}
