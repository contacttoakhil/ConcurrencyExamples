package phaser.buffer.implementations;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    A CDL is a good option but will fail in cyclic scenario. We may consider a CyclicBarrier but that fails becuase we dont know the
    no of threads being used.
 */
public class BetterBuffer implements IBuffer {
    private final Phaser phaser = new Phaser(1); // One party to arrive.
    private final Queue<String> bufferMemory = new ConcurrentLinkedQueue<>();

    public void write(String value){
        int phase = phaser.getPhase();
        phaser.awaitAdvance(phase);// The phase number starts at zero, and advances when all parties arrive at the phaser, wrapping around to zero after reaching Integer.MAX_VALUE.
        // Writes are green to go now.
        bufferMemory.add(value);
        System.out.println("Value added: " + value);
    }

    public void cleanUp() {
        while(!bufferMemory.isEmpty()){
            String item = bufferMemory.remove();
            //System.out.println("Removed: " + item);
        }
        System.out.println("Clean up over");
        phaser.arrive();
    }

    public int size() {
        return bufferMemory.size();
    }
}

