package main.executorCompletionService.basic;

import java.util.concurrent.Callable;

/**
 * Created by Akhil on 01-05-2015.
 */
public class SleepingCallable  implements Callable<String> {
    final String name;
    final long period;

    public SleepingCallable(String name, long period) {
        this.name = name;
        this.period = period;
    }

    @Override
    public String call() throws Exception {
        try {
            Thread.sleep(period);
        } catch (InterruptedException ex) { }
        return name;
    }
}
