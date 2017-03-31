package cm.linearalgebra.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cm.linearalgebra.interfaces.LinearAlgebraAlgorithm;

public class SpinsMethod implements LinearAlgebraAlgorithm{
	private float eps;
	private float fi;
	
	@Autowired
	@Qualifier("Matrix")
	private Matrix A;
	
	@Autowired
	@Qualifier("Matrix")
	private Matrix U;
	
	@Autowired
	@Qualifier("Matrix")
	private Matrix eigenVectors;
			
	private float endCriterior(){
		float res = 0;
		for(int i = 0; i < A.n; i++){
			for(int j = 0; j < A.n;j++){
				if(i < j){
					res+=A.matr[i][j]*A.matr[i][j];
				}
			}
		}
		return (float)Math.sqrt(res);
	}
	@Override
	public void algorithm() {
		// TODO Auto-generated method stub
		float accuracy = endCriterior();
		eigenVectors.printMatr();
		while(accuracy > eps){
			CoordInMatr maxElem = chooseMaksElem();
			calkFi(maxElem);
			findU(maxElem);
			buildNewA();
			accuracy = endCriterior();
			Matrix tmp = Matrix.multiplicationMatrix(eigenVectors,U);
			eigenVectors = tmp;
		}
		printResult();
	}

	private void calkFi(CoordInMatr a){
		float arg = 0;
		if(A.matr[a.i][a.i]==A.matr[a.j][a.j]){
			arg = 2*A.matr[a.i][a.j] / (A.matr[a.i][a.i] - A.matr[a.j][a.j]);	
		}
		else{
			arg = (float)Math.PI / 4;
		}
		fi = (float)(Math.atan(arg)/2);
	}
	
	private void buildNewA() {
		// TODO Auto-generated method stub
		Matrix Utrans = Matrix.transposeMatrix(U);
		Matrix tmp = Matrix.multiplicationMatrix(Utrans,A);
		A = Matrix.multiplicationMatrix(tmp,U);
	}
	
	private void findU(CoordInMatr coord) {
		// TODO Auto-generated method stub
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n;j++){
				if(i!=j){
					U.matr[i][j] = 0;
				}
				else{
					U.matr[i][j] = 1;
				}
			}
		}
		U.matr[coord.i][coord.j] = (float) (-Math.sin(fi));
		U.matr[coord.j][coord.i] = (float)(Math.sin(fi));
		U.matr[coord.i][coord.i] = (float)(Math.cos(fi));
		U.matr[coord.j][coord.j] = (float)(Math.cos(fi));
	}
	private CoordInMatr chooseMaksElem() {
		CoordInMatr res = new CoordInMatr();
		float max = 0;
		for(int i = 0; i < A.n; i++){
			for(int j = 0; j < A.n; j++){
				if((i < j)&& Math.abs(A.matr[i][j]) > max){
					max = Math.abs(A.matr[i][j]);
					res.i = i;
					res.j = j;
				}
			}
		}
		return res;
	}
	@Override
	public void init() {
		String nameFile = Main.getFileWithInputData();
		File file = new File(nameFile);
		try {
			Scanner scan = new Scanner(file);
			A.readMatrFromFile(scan);
			initMatr(U);
			initMatr(eigenVectors);
			initSingle(eigenVectors);
			eps = scan.nextFloat();
		} catch (FileNotFoundException e) {
			System.out.println("Can not open file:");
			e.printStackTrace();
		}
	}
	private void initSingle(Matrix in){
		for(int i = 0; i < in.n;i++){
			for(int j = 0; j < in.n;j++){
				if(i==j){
					in.matr[i][j] = 1;
				}
			}
		}
	}
	private void initMatr(Matrix in){
		in.setN(A.n);
		in.setMatr(new float[A.n][A.n]);
	}
	@Override
	public void printResult() {
		File result = new File("C:\\Users\\User\\Desktop\\result.txt");
		try {
			if(!result.exists()){
				result.createNewFile();				
			}
			PrintWriter writer = new PrintWriter(result);
			writer.write("Собственные значения:\r\n");
			for(int i = 0; i < A.n;i++){
				writer.write(Float.toString(A.matr[i][i]) + "\r\n");
			}
			writer.write("Собственные векторы:\r\n");
			eigenVectors.printMatr(writer);		
			writer.close();
			OpenResult.launch("C:\\Users\\User\\Desktop\\result.txt");
		}catch(Exception ex){
			System.out.println("Can not write in file:");
			ex.printStackTrace();
		}
	}
	class CoordInMatr{
		public int i;
		public int j;
	}
}
