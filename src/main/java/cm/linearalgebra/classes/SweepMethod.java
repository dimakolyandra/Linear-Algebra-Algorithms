package cm.linearalgebra.classes;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cm.linearalgebra.interfaces.LinearAlgebraAlgorithm;

/** Sweep method for solving system of linear equation */
public class SweepMethod implements LinearAlgebraAlgorithm{
	@Autowired
	@Qualifier("TridMatr")
	protected TridiagonalMatrix  matrix;
	
	@Autowired
	@Qualifier("Vect")
	protected Vector<Float> vectValues;
	
	@Autowired
	@Qualifier("Vect")
	protected Vector<Float> p;
	
	@Autowired
	@Qualifier("Vect")
	protected Vector<Float> q;
	
	@Autowired
	@Qualifier("Vect")
	protected Vector<Float> x;
	
	public Vector<Float> getX() {
		return x;
	}

	public void setX(Vector<Float> x) {
		this.x = x;
	}

	public Vector<Float> getP() {
		return p;
	}

	public void setP(Vector<Float> p) {
		this.p = p;
	}

	public Vector<Float> getQ() {
		return q;
	}

	public void setQ(Vector<Float> q) {
		this.q = q;
	}

	public TridiagonalMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(TridiagonalMatrix matrix) {
		this.matrix = matrix;
	}

	public Vector<Float> getVectValues() {
		return vectValues;
	}

	public void setVectValues(Vector<Float> vectValues) {
		this.vectValues = vectValues;
	}

	private void calcPandQ(){
		float p0 = (-matrix.c[0]) / matrix.b[0];
		float q0 = vectValues.get(0) / matrix.b[0];
		p.add(p0);
		q.add(q0);
		for(int i = 1; i < matrix.n;i++){
			float pi = (-matrix.c[i])/(matrix.b[i] + matrix.a[i]* p.get(i-1));
			float qi = (vectValues.get(i) - matrix.a[i]*q.get(i-1))
						/(matrix.b[i] + matrix.a[i]* p.get(i-1));
			p.add(pi);
			q.add(qi);
		}
		int n = matrix.n;
	}
	
	@Override
	public void algorithm() {
		calcPandQ();
		float xn = q.get(matrix.n - 1);
		x.add(xn);
		int k = 0;
		for(int i = matrix.n - 2; i >= 0;i--){
			float xi = p.get(i) * x.get(k) + q.get(i);
			x.add(xi);
			k++;
		}
		printResult();
	}

	@Override
	public void init() {
		try{
			String nameFile = Main.getFileWithInputData();
			File file = new File(nameFile);
			Scanner scan = new Scanner(file);			
			matrix.readMatrFromFile(scan);
			for(int j = 0; j < matrix.n; j++){
				float b = scan.nextFloat();
				vectValues.add(j, new Float(b));		
			}
		}catch(Exception ex){
			System.out.println("Can not open file:");
			ex.printStackTrace();
		}
	}

	@Override
	public void printResult() {
		File result = new File("C:\\Users\\User\\Desktop\\result.txt");
		try {
			if(!result.exists()){
				result.createNewFile();				
			}
			PrintWriter writer = new PrintWriter(result);
			writer.write("Результат:\r\n");
			int i = matrix.n;
			for(float tmp:x){
				writer.write("X" + i + "=" + tmp + " ");
				i--;
			}
			writer.close();
			OpenResult.launch("C:\\Users\\User\\Desktop\\result.txt");
		}catch(Exception ex){
			System.out.println("Can not write in file:");
			ex.printStackTrace();
		}
	}
}
