package cm.linearalgebra.classes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;

import cm.linearalgebra.interfaces.LinearAlgebraAlgorithm;

public class SimpleIterationMethod implements LinearAlgebraAlgorithm{
	@Autowired
	@Qualifier("Matrix")
	protected Matrix A;
	protected int numbIter = 0;
	@Autowired
	@Qualifier("Vect")
	protected Vector<Float> vectValues;
	
	@Autowired
	@Qualifier("Vect")
	protected Vector<Float> X;	
	
	protected float eps;
	
	public Vector<Float> getX() {
		return X;
	}

	public void setX(Vector<Float> x) {
		X = x;
	}

	public Matrix getA() {
		return A;
	}
	
	public void setA(Matrix a) {
		A = a;
	}

	public Vector<Float> getVectValues() {
		return vectValues;
	}

	public void setVectValues(Vector<Float> vectValues) {
		this.vectValues = vectValues;
	}
	
	protected Vector<Float> calcBetaVect(){
		Vector<Float> beta = new Vector<Float>();
		for(int i = 0; i < A.n; i++){
			float b = vectValues.get(i)/A.matr[i][i];
			beta.add(new Float(b));
		}
		return beta;
	}
	protected Matrix calcAlphaMatrix(){
		Matrix alpha = new Matrix(A.n);
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n; j++){
				if(i!=j){
					alpha.matr[i][j] = (-A.matr[i][j])/A.matr[i][i];					
				}
			}
		}
		return alpha;
	}
	protected void printVector(Vector<Float> vect){
		for(int i = 0; i < vect.size();i++){
			System.out.print(vect.get(i) + " ");		
		}
		System.out.println("");		
	}

	protected float calcAccuracy(double normAlpha,Vector<Float> X_prev){
		Vector<Float> difference;
		difference = Matrix.calcDifferenceVectors(X,X_prev);
		double normDiff = Matrix.calcNormVector(difference);
		return  (float)((normAlpha * normDiff)/(1 - normAlpha));
	}
	protected float calcAccuracy(double normAlpha,Vector<Float> X_prev, int fictiousParam){
		Vector<Float> difference;
		difference = Matrix.calcDifferenceVectors(X,X_prev);
		double normDiff = Matrix.calcNormVector(difference);
		return (float)normDiff;
	}
	protected void copyVectors(Vector<Float> A,Vector<Float> B){
		for(int i = 0; i < A.size();i++){
			A.set(i,B.get(i));
		}
	}
	@Override
	public void algorithm() {
		Vector<Float> beta = calcBetaVect();
		Matrix alpha = calcAlphaMatrix();
		float accuracy = 10;
		double normAlpha = alpha.calcNormMatrix();
		for(int i = 0; i < A.n; i++){
				float elem = beta.get(i);
				X.add(new Float(elem));
			}
			while(accuracy > eps){
				numbIter++;
				Vector<Float> X_prev = new Vector<Float>(X);
				Vector<Float> tmp = Matrix.multMatrixOnVector(alpha,X_prev);
				tmp = Matrix.sumTwoVectors(tmp,beta);
				X = tmp;
				if(normAlpha < 1){ 	
					accuracy = calcAccuracy(normAlpha,X_prev);
				}
				else{
					accuracy = calcAccuracy(normAlpha,X_prev,1);		
				}
			}
			printResult();
	}
	@Override
	public void init() {
		try{
			String nameFile = Main.getFileWithInputData();
			File file = new File(nameFile);
			Scanner scan = new Scanner(file);
			A.readMatrFromFile(scan);
			for(int i = 0; i < A.n;i++){
				float a = scan.nextFloat();
				vectValues.add(new Float(a));
			}
			eps = scan.nextFloat();
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
			writer.write("Входная матрица:\r\n");
			A.printMatr(writer);
			writer.write("Результат:\r\n");
			int i = 1;
			for(float tmp:X){
				writer.write("X" + i  + "=" + tmp + " ");
				i++;
			}
			writer.write("\r\nЧисло итераций:");
			writer.write(Integer.toString(numbIter));
			writer.close();
			OpenResult.launch("C:\\Users\\User\\Desktop\\result.txt");
		}catch(Exception ex){
			System.out.println("Can not write in file:");
			ex.printStackTrace();
		}
	}

}
