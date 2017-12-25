package phaser.buffer.implementations;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NeatBuffer implements IBuffer {
    private CountDownLatch flushCDL = new CountDownLatch(1);
    private final Lock flushLock = new ReentrantLock();
    private final Queue<String> bufferMemory = new ConcurrentLinkedQueue<>();

    public void write(String value){
        try {
            flushCDL.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Clean up is over. Reset the main.latch.
        flushLock.lock();
        flushCDL = new CountDownLatch(1);
        flushLock.unlock();
        // Writes are green to go now.
        bufferMemory.add(value);
        System.out.println("Value added: " + value);
    }

    public void cleanUp() {
        // Only one reader will be there that will read all items from bufferMemory.
        flushLock.lock();
        while(!bufferMemory.isEmpty()){
            String item = bufferMemory.remove();
            //System.out.println("Removed: " + item);
        }
        flushCDL.countDown();
        flushLock.unlock();
        System.out.println("Clean up over");
    }

    public int size() {
        return bufferMemory.size();
    }
}

