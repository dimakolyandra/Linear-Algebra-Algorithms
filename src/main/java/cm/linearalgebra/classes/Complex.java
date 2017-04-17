package cm.linearalgebra.classes;

import java.io.PrintWriter;

/** Class that implements complex number */
public class Complex {
	
	/** Real part */
	private float re;
	
	/** Imaginary part */
	private float im;
	
	Complex(float x,float y){
		this.re = x;
		this.im = y;
	}
	
	/** Print complex number */
	public void printComplex(){
		if(im >= 0){
			System.out.println(re + " +" + im + "*i");			
		}
		else{
			System.out.println(re + " " + im + "*i");				
		}
	}
	
	/** Print complex number into file */
	public void printComplex(PrintWriter writer){
		if(im >= 0){
			writer.write(re + " +" + im + "*i\r\n");			
		}
		else{
			writer.write(re + " " + im + "*i\r\n");				
		}
	}
	
	/** Difference of two complex number */
	public static Complex minusComplex(Complex a,Complex b){
		float real = a.re - b.re;
		float image = a.im - b.im;
		return new Complex(real,image);
	}
	
	/** Absolute value of compex number*/
	public float absComplex(){
		return (float) Math.sqrt(re*re + im*im);
	}
}
