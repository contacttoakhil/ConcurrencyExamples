package main.basics.interrupt;

/**
 * Created by Akhil on 01-05-2015.
 */
public class InterruptibleJob implements Runnable {
    private volatile boolean isTerminated;

    public boolean isTerminated() {
        return isTerminated;
    }

    public void setTerminated(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }

    @Override
    public void run() {
        for (int i=0; i<Integer.MAX_VALUE; i++) {
            if(isTerminated()) break;
            System.out.println(i);
        }
    }
}
