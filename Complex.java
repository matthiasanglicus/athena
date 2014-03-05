/**
 * A complex number is composed of two parts, a real part, and an imaginary part.
 */
public class Complex {
	private double real;
	private double imaginary;
    
	public Complex() {
		this.real = 0;
		this.imaginary = 0;
	}
    
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
    
	public Complex(Complex c) {
		this.real = c.getReal();
		this.imaginary = c.getImaginary();
	}
    
	public double getReal() {
		return this.real;
	}
    
	public double getImaginary() {
		return this.imaginary;
	}
    
	public void setReal(double real) {
		this.real = real;
	}
    
	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
    
	public Complex plus(Complex op) {
		Complex result = new Complex();
		result.setReal(this.real + op.getReal());
		result.setImaginary(this.imaginary + op.getImaginary());
		return result;
	}
    
	public Complex minus(Complex op) {
		Complex result = new Complex();
		result.setReal(this.real - op.getReal());
		result.setImaginary(this.imaginary - op.getImaginary());
		return result;
	}
    
	public Complex mul(Complex op) {
		Complex result = new Complex();
		result.setReal(this.real * op.getReal() - this.imaginary * op.getImaginary());
		result.setImaginary(this.real * op.getImaginary() + this.imaginary * op.getReal());
		return result;
	}
	
	public Complex mul(double op) {
		Complex result = new Complex();
		result.setReal(this.real * op);
		result.setImaginary(this.imaginary * op);
		return result;
	}
    
	public Complex div(Complex op) {
		Complex result = new Complex(this);
		result = result.mul(op.getConjugate());
		double opNormSq = op.getReal() * op.getReal() + op.getImaginary() * op.getImaginary();
		result.setReal(result.getReal() / opNormSq);
		result.setImaginary(result.getImaginary() / opNormSq);
		return result;
	}
	
	public Complex div(double op) {
		Complex result = new Complex();
		result.setReal(this.real / op);
		result.setImaginary(this.imaginary / op);
		return result;
	}
    
	public static Complex fromPolar(double magnitude, double angle) {
		Complex result = new Complex();
		result.setReal(magnitude * Math.cos(angle));
		result.setImaginary(magnitude * Math.sin(angle));
		return result;
	}
    
	public double getNorm() {
		return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
	}
    
	public double getAngle() {
		return Math.atan2(this.imaginary, this.real);
	}
    
	public Complex getConjugate() {
		return new Complex(this.real, this.imaginary * (-1));
	}
    
	public String toString() {
		if (this.real == 0) {
			if (this.imaginary == 0) {
				return "0";
			} else {
				return (this.imaginary + "i");
			}
		} else {
			if (this.imaginary == 0) {
				return String.valueOf(this.real);
			} else if (this.imaginary < 0) {
				return (this.real + " " + this.imaginary + "j");
			} else {
				return (this.real + " +" + this.imaginary + "j");
			}
		}
	}
}