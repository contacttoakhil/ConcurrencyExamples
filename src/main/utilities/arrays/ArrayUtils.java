package main.utilities.arrays;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class ArrayUtils {
    public static int[][] slice(int[] array, int sliceSize) {
        int slicesCount = (int) Math.ceil(array.length/(double)sliceSize);
        int[][] results = new int[slicesCount][];
        int[] sliceArray;
        int index = 0;
        for(int i=array.length; i>0; i=i-sliceSize) {
            int start = array.length-i;
            int last = ((start+sliceSize) < array.length)? (start+sliceSize) : (array.length);
            sliceArray = Arrays.copyOfRange(array,start,last);
            results[index++] = sliceArray;
        }
        return results;
    }

    public static int[][] sliceUsingJavaStreams(int[] arr, int n) {
        return IntStream.range(0, (int) Math.ceil(arr.length / (double) n))
                .mapToObj(new IntFunction<int[]>() {
                    @Override
                    public int[] apply(int i) {
                        return Arrays.copyOfRange(arr, i * n, Math.min((i + 1) * n, arr.length));
                    }
                }).toArray(int[][]::new);
    }
}
