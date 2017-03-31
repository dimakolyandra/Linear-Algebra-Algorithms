package cm.linearalgebra.classes;

import java.io.PrintWriter;

public class Complex {
	
	private float re;
	private float im;
	
	Complex(float x,float y){
		this.re = x;
		this.im = y;
	}

	public void printComplex(){
		if(im >= 0){
			System.out.println(re + " +" + im + "*i");			
		}
		else{
			System.out.println(re + " " + im + "*i");				
		}
	}
	public void printComplex(PrintWriter writer){
		if(im >= 0){
			writer.write(re + " +" + im + "*i\r\n");			
		}
		else{
			writer.write(re + " " + im + "*i\r\n");				
		}
	}
	
	public static Complex minusComplex(Complex a,Complex b){
		float real = a.re - b.re;
		float image = a.im - b.im;
		return new Complex(real,image);
	}
	
	public float absComplex(){
		return (float) Math.sqrt(re*re + im*im);
	}
}
