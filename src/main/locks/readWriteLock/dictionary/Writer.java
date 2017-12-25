package main.locks.readWriteLock.dictionary;

/**
 * Created by Akhil on 21-02-2015.
 */
public class Writer extends Thread{
    private boolean keepRunning = true;
    private final Dictionary dictionary;

    public Writer(Dictionary dictionary, String threadName) {
        this.dictionary = dictionary;
        this.setName(threadName);
    }

    @Override
    public void run() {
        while (keepRunning){
            String[] keys = dictionary.getKeys();
            for(String key : keys) {
                String newValue = getNewValueFromBackEnd(key);
                dictionary.set(key,newValue);
            }

            //Update every 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWriter() {
        this.keepRunning = false;
        this.interrupt();
    }

    private String getNewValueFromBackEnd(String key) {
        return "NEW-VALUE";
    }
}
