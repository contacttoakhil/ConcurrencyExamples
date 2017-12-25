package main.locks.stampedLock.complexNumber;

/**
 * Created by Akhil on 24-02-2015.
 */
public class ComplexNumber {
    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double abs() { return Math.hypot(real,imaginary);}
    public double phase() { return Math.atan2(real, imaginary);} // between PI and -PI

    public ComplexNumber add(ComplexNumber c) {
        double real = this.real + c.getReal();
        double imaginary = this.imaginary - c.getImaginary();
        return new ComplexNumber(real,imaginary);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }
}
