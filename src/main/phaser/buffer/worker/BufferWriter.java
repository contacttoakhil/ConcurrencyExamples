package phaser.buffer.worker;

import phaser.buffer.implementations.IBuffer;

/**
 * Created by Akhil on 07-03-2015.
 */
public class BufferWriter extends Thread{
    private final IBuffer buffer;
    private final String valueToWrite;
    private volatile boolean keepRunning = true;

    public BufferWriter(IBuffer buffer, String name, String valueToWrite) {
        this.buffer = buffer;
        this.valueToWrite = valueToWrite;
        this.setName(name);
    }

    @Override
    public void run() {
        while(keepRunning) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread: " + getName() + " going to write value: " + valueToWrite);
            buffer.write(valueToWrite);
        }
    }

    public void stopWriting() {
        keepRunning = false;
        //this.interrupt();
    }
}
