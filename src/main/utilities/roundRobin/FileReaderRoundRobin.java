package main.utilities.roundRobin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReaderRoundRobin {
    public Object[] locks;

    private static class LinePrinterJob implements Runnable {
        private final Object currentLock;
        private final Object nextLock;
        private final String fileToRead;
        BufferedReader bufferedReader = null;

        public LinePrinterJob(String fileToRead, Object currentLock, Object nextLock) {
            this.currentLock = currentLock;
            this.nextLock = nextLock;
            this.fileToRead = fileToRead;
            try {
                this.bufferedReader = new BufferedReader(new java.io.FileReader(fileToRead));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            String currentLine;
            synchronized(currentLock) {
                try {
                    while ( (currentLine = bufferedReader.readLine()) != null) {
                        try {
                            currentLock.wait();
                            System.out.println(currentLine);
                        }
                        catch(InterruptedException e) {}
                        synchronized(nextLock) {
                            nextLock.notify();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized(nextLock) {
                nextLock.notify(); /// this ensures all worker threads exiting at the end
            }
        }
    }

    public FileReaderRoundRobin(int numberOfFilesToRead) {
        locks = new Object[numberOfFilesToRead];
        int i;
        String fileLocation = "src/main.utilities/roundRobin/temp/";
        //Initialize lock instances in array.
        for(i = 0; i < numberOfFilesToRead; ++i) locks[i] = new Object();
        //Create threads
        int j;
        for(j=0; j<(numberOfFilesToRead-1); j++ ){
            Thread linePrinterThread = new Thread(new LinePrinterJob(fileLocation + "Temp" + j,locks[j],locks[j+1]));
            linePrinterThread.start();
        }
        Thread lastLinePrinterThread = new Thread(new LinePrinterJob(fileLocation + "Temp" + j,locks[numberOfFilesToRead-1],locks[0]));
        lastLinePrinterThread.start();
    }

    public void startPrinting() {
        synchronized (locks[0]) {
            locks[0].notify();
        }
    }

    public static void main(String[] args) {
        FileReaderRoundRobin fileReaderRoundRobin = new FileReaderRoundRobin(4);
        fileReaderRoundRobin.startPrinting();
    }

}
