package cm.linearalgebra.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TridiagonalMatrix {
	protected int n; 
	protected float[] a;
	protected float[] b;
	protected float[] c;
	
	private void readFirstStr(Scanner scan){
		a[0] = 0;
		b[0] = scan.nextFloat();
		c[0] = scan.nextFloat();
	}
	private void readLastStr(Scanner scan){
		a[n-1] = scan.nextFloat();
		b[n-1] = scan.nextFloat();
		c[n-1] = 0;
	}
	
	public void readMatrFromFile(Scanner scan){
		n = scan.nextInt();	
		a = new float[n];
		b = new float[n];
		c = new float[n];
		readFirstStr(scan);
		for(int i = 1; i < n - 1;i++){
			a[i] = scan.nextFloat();
			b[i] = scan.nextFloat();
			c[i] = scan.nextFloat();
		}
		readLastStr(scan);
	}
	public void init(){
	}
}
