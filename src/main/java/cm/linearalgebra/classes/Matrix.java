package cm.linearalgebra.classes;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

public class Matrix {
	
	protected int n;
	protected float[][] matr;

	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public Matrix(){
		n = 0;
	}
	public Matrix(int n){
		this.n = n;
		setMatr(new float[n][n]);
	}
	public Matrix(float[][] matr){
		this.matr = matr;
	}
	public float[][] getMatr() {
		return matr;
	}
	public void setMatr(float[][] matr) {
		this.matr = matr;
	}
	public static void copyMatrix(Matrix A,Matrix B){
		for(int i = 0; i < B.n; i++){
			for(int j = 0; j < B.n;j++){
				A.matr[i][j] = B.matr[i][j];
			}
		}
	}
	public void printMatr(){
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				System.out.print(matr[i][j] + " ");
			}
			System.out.println("");
		}
	}
	public void readMatrFromFile(Scanner scan){
		n = scan.nextInt();
		setMatr(new float[n][n]);
		try {
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					matr[i][j] = scan.nextFloat();				
				}
			}
		} catch (Exception e) {
			System.out.println("Can not open file:");
			e.printStackTrace();
		}	
	}
	public void printMatr(PrintWriter writer){
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				writer.write(Float.toString(matr[i][j]) + " ");
			}
			writer.write("\r\n");
		}
	}
	public double calcNormMatrix(){
		double result = 0;
		for(int i = 0; i < n;i++){
			for(int j = 0; j < n;j++){
				result = result + matr[i][j]*matr[i][j];
			}
		}
		return Math.sqrt(result);
	}
	public void init(){
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				matr[0][0] = 0;
			}
		}
	}
	public static Vector<Float> calcDifferenceVectors(Vector<Float> one, Vector<Float> two){
		Vector<Float> result = new Vector<Float>();
		for(int i = 0; i < one.size();i++){
			float diff = one.get(i) - two.get(i);
			result.add(new Float(diff));
		}
		return result;
	}
	public static Matrix calcDifferenceMatrix(Matrix A, Matrix B){
		Matrix result = new Matrix(A.n);
		for(int i = 0; i < A.n;i++){
			for(int j = 0;j<A.n;j++){
				result.matr[i][j] = A.matr[i][j] - B.matr[i][j];
			}
		}
		return result;
	}
	public static double calcNormVector(Vector<Float> vect){
		double result = 0;
		for(int i = 0; i < vect.size();i++){
			result+=vect.get(i)*vect.get(i);
		}
		return Math.sqrt(result);
	}
	public static Vector<Float> sumTwoVectors(Vector<Float> one, Vector<Float> two){
		Vector<Float> result = new Vector<Float>();
		for(int i = 0; i < one.size();i++){
			float elem = one.get(i) + two.get(i);
			result.add(new Float(elem));
		}
		return result;
	}
	public static Vector<Float> multMatrixOnVector(Matrix A, Vector<Float> B){
		Vector<Float> result = new Vector<Float>();
		for(int i = 0; i < A.n; i++){
			float elem = 0;
			for(int j = 0; j < A.n; j++){
				elem += A.matr[i][j] * B.get(j);
			}
			result.add(new Float(elem));
		}
		return result;
	}
	public static Matrix transposeMatrix(Matrix A){
		Matrix transMatr = new Matrix(A.n);
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n;j++){
				transMatr.matr[j][i] = A.matr[i][j];
			}
		}
		return transMatr;
	}
	protected void initSingleMatrix(){
		for(int i = 0; i < this.n;i++){
			for(int j = 0; j <this.n;j++){
				if(i!=j){
					this.matr[i][j] = 0;					
				}
				else{
					this.matr[i][j] = 1;
				}
			}
		}
	}
	public static float scalarMult(Vector<Float> a, Vector<Float> b){
		float res = 0;
		for(int i = 0; i < a.size();i++){
			res += a.get(i) * b.get(i);
		}
		return res;
	}
	public static void multMatrixOnScal(Matrix A,float k){
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n; j++){
				A.matr[i][j] = A.matr[i][j] * k;
			}
		}
	}
	public static Matrix multiplicationMatrix(Matrix A, Matrix B){
		Matrix res = new Matrix(A.n);
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n;j++){
				for(int k = 0; k < A.n; k++){
					res.matr[i][j] += A.matr[i][k] * B.matr[k][j];
				}
			}
		}
		return res;
	}
}
