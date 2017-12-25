package phaser.buffer;

import phaser.buffer.implementations.BetterBuffer;
import phaser.buffer.implementations.IBuffer;
import phaser.buffer.implementations.NeatBuffer;
import phaser.buffer.worker.BufferCleaner;
import phaser.buffer.worker.BufferWriter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Akhil on 07-03-2015.
 */
public class BufferMainApp {
    public static void main(String[] args) {
        System.out.println("Starting");
        //IBuffer buffer = new UglyBuffer();
        //IBuffer buffer = new NeatBuffer();
        IBuffer buffer = new BetterBuffer();
        Set<Thread> writers = initiateRandomWriters(buffer);
        BufferCleaner cleaner = initiateSingleCleaner(buffer);

        try {
            Thread.currentThread().sleep(60000);    //sleep for 60s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopAllWorkers(writers);
        cleaner.stopCleanup();
    }

    private static void stopAllWorkers(Set<Thread> writers) {
        for (Thread writer : writers) {
            ((BufferWriter)writer).stopWriting();
        }
    }

    private static BufferCleaner initiateSingleCleaner(IBuffer buffer) {
        BufferCleaner cleaner = new BufferCleaner(buffer,"Cleaner");
        cleaner.start();
        return cleaner;
    }

    private static Set<Thread> initiateRandomWriters(IBuffer buffer) {
        Set<Thread> writers = new HashSet<>();
        Random random = new Random();
        BufferWriter writer;
        int writersCount = random.nextInt(12);
        if(writersCount == 0) writersCount =+ 4;     // At least 4 threads.
        for (int i = 0; i < writersCount; i++) {
            writer = new BufferWriter(buffer,"Writer" + i, "Value" + i);
            writers.add(writer);
            writer.start();
        }
        System.out.println("Total writers are: " + writers.size());
        return writers;
    }
}

