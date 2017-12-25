package main.utilities.roundRobin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class FileReaderRoundRobinUsingPhaser {

    final List<Runnable> tasks = new ArrayList<>();
    final int numberOfLinesToRead;

    private static class LinePrinterJob implements Runnable {
        private BufferedReader bufferedReader;

        public LinePrinterJob(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
        }

        @Override
        public void run() {
            String currentLine;
            try {
                currentLine = bufferedReader.readLine();
                System.out.println(currentLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileReaderRoundRobinUsingPhaser(int numberOfFilesToRead, int numberOfLinesToRead) {
        this.numberOfLinesToRead = numberOfLinesToRead;
        String fileLocation = "src/main.utilities/roundRobin/temp/";
        for(int j=0; j<(numberOfFilesToRead-1); j++ ){
            try {
                tasks.add(new LinePrinterJob(new BufferedReader(new FileReader(fileLocation + "Temp" + j))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void startPrinting( ) {
        final Phaser phaser = new Phaser(1){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("Phase Number: " + phase +" Registeres parties: " + getRegisteredParties() + " Arrived: " + getArrivedParties());
                return ( phase >= numberOfLinesToRead || registeredParties == 0);
            }
        };

        for(Runnable task : tasks) {
            phaser.register();
            new Thread(() -> {
                do {
                    phaser.arriveAndAwaitAdvance();
                    task.run();
                } while(!phaser.isTerminated());
            }).start();
        }
        phaser.arriveAndDeregister();
    }

    public static void main(String[] args) {
        // Files will be accessed in round robin fashion but not exactly in same order always. For example it can read 4 files as 1234 then 1342 or 1243 etc.
        FileReaderRoundRobinUsingPhaser fileReaderRoundRobin = new FileReaderRoundRobinUsingPhaser(4, 4);
        fileReaderRoundRobin.startPrinting();
    }

}
