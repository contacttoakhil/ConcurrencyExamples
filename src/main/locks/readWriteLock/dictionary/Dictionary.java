package main.locks.readWriteLock.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The synchronized keyword is used to acquire a exclusive lock on an object. When a thread acquires a lock of an object either for reading or writing, other threads must wait until
 * the lock on that object is released. If we have many reader threads that reads a shared data frequently and only one writer thread that updates shared data. It’s not necessary to
 * exclusively lock access to shared data while reading because multiple read operations can be done in parallel unless there is a write operation.
 *
 * A ReadWriteLock maintains a pair of associated main.locks, one for read-only operations and one for writing. The read lock may be held simultaneously by multiple reader threads, so
 * long as there are no writers. The write lock is exclusive. Both the main.locks are pessimistic. ReentrantReadWriteLock has a huge overhead.
 *
 * When readers are given priority then writers might never be able to complete (Java 5)
 * But when writers are given priority readers might be starved (javaspecialists.eu, issue165)
 */
public class Dictionary {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Map<String,String> dictionary = new HashMap<>();

    public void set(String key, String value) {
        readWriteLock.writeLock().lock();
        try {
            dictionary.put(key, value);
        } finally {
             readWriteLock.writeLock().unlock();
        }
    }

    public String get(String key) {
        readWriteLock.readLock().lock();
        try{
            return dictionary.get(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public String[] getKeys() {
        readWriteLock.readLock().lock();
        try {
            return (String[]) dictionary.keySet().toArray();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        dictionary.set("java",  "object oriented");
        dictionary.set("linux", "rules");
        Writer writer  = new Writer(dictionary, "Writer");
        Reader reader1 = new Reader(dictionary ,"ReaderOne");
        Reader reader2 = new Reader(dictionary ,"ReaderTwo");
        Reader reader3 = new Reader(dictionary ,"ReaderThree");
        Reader reader4 = new Reader(dictionary ,"ReaderFour");
        writer.start();
        reader1.start();
        reader2.start();
        reader3.start();
        reader4.start();
    }
}
