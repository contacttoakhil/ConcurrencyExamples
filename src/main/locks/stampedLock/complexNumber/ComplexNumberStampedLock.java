package main.locks.stampedLock.complexNumber;

import java.util.concurrent.locks.StampedLock;

/**
 * Created by Akhil on 24-02-2015.
 */
public class ComplexNumberStampedLock {
    private final double real;
    private final double imaginary;
    private final StampedLock stampedLock = new StampedLock();

    public ComplexNumberStampedLock(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double abs() {
        long stamp = stampedLock.tryOptimisticRead();   // No locking just optimistic read.
        double currentReal = real, currentImaginary = imaginary;
        if(!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try{
                currentReal = real; currentImaginary = imaginary;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.hypot(real,imaginary);
    }
    public double phase() { return Math.atan2(real, imaginary);} // between PI and -PI

    public ComplexNumberStampedLock add(ComplexNumberStampedLock c) {
        double real = this.real + c.getReal();
        double imaginary = this.imaginary - c.getImaginary();
        return new ComplexNumberStampedLock(real,imaginary);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

}
