package phaser.buffer.worker;

import phaser.buffer.implementations.IBuffer;

/**
 * Created by Akhil on 07-03-2015.
 */
public class BufferCleaner extends Thread{
    private final IBuffer buffer;
    private volatile boolean keepRunning = true;

    public BufferCleaner(IBuffer buffer, String name) {
        this.buffer = buffer;
        this.setName(name);
    }

    @Override
    public void run() {
        while(keepRunning) {
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread: " + getName() + " started");
            buffer.cleanUp();
        }
    }

    public void stopCleanup() {
        keepRunning = false;
        //this.interrupt();
    }
}
