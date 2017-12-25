package main.streams.primeNumber;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeNumbersService {
    public static Set<Integer> findPrimesUsingStreams(int maxPrimeToTry) {
        return IntStream.rangeClosed(2, maxPrimeToTry)
                .filter(i -> IntStream.rangeClosed(2, (int) (Math.sqrt(i))).noneMatch(j -> i%j == 0))
                .mapToObj(i -> Integer.valueOf(i))
                .collect(Collectors.toSet());
    }
    public static Set<Integer> findPrimesUsingParallelStreams(int maxPrimeToTry) {
        return IntStream.rangeClosed(2, maxPrimeToTry)
                .parallel()
                .filter(i -> IntStream.rangeClosed(2, (int) (Math.sqrt(i))).noneMatch(j -> i%j == 0))
                .mapToObj(i -> Integer.valueOf(i))
                .collect(Collectors.toSet());
    }
}
