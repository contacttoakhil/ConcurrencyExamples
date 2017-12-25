package main.completableFuture.basics;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
    CompletableFuture.supplyAsync
    supplyAsync accepts a Supplier as an argument and complete its job asynchronously. The result of supplier is run by a task from ForkJoinPool.commonPool() as default. We can also pass our
    Executor. Finally supplyAsync method returns CompletableFuture on which we can apply other methods.

    CompletableFuture.join
    join method returns the result after completion or throws CompletionException. This method waits for the completion of calling completion stage.

    https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
 */
public class CompletionFutureOne {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(10, 20, 30, 40);
        list.stream().map(data-> CompletableFuture.supplyAsync(() -> getSquare(data)))
                .map(compFuture -> compFuture.thenApply(n -> n*n))
                .map(t -> t.join())
                .forEach(s -> System.out.println(s));
    }

    private static int getSquare(int a){
        return a*a;
    }
}
