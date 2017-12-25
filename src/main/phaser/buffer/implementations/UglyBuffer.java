package phaser.buffer.implementations;

import phaser.buffer.implementations.IBuffer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UglyBuffer implements IBuffer {
    private volatile long lastFlushTime = System.currentTimeMillis();
    private final Object flushMonitor = new Object();
    private final Queue<String> bufferMemory = new ConcurrentLinkedQueue<>();

    public void write(String value){
        long entryTime = System.currentTimeMillis();
        synchronized (flushMonitor) {
            while(lastFlushTime <= entryTime) {
                try {
                    flushMonitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // Seems like all writes can go now.
        bufferMemory.add(value);
        System.out.println("Value added: " + value);
    }

    public void cleanUp() {
        // Only one reader will be there that will read all items from bufferMemory.
        synchronized (flushMonitor) {
            while(!bufferMemory.isEmpty()){
                String item = bufferMemory.remove();
                //System.out.println("Removed: " + item);
            }
            System.out.println("Clean up over");
            lastFlushTime = System.currentTimeMillis();
            flushMonitor.notifyAll();
        }
    }

    public int size() {
        return bufferMemory.size();
    }
}

