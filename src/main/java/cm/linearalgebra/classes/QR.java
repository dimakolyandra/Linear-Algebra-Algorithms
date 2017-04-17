package cm.linearalgebra.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cm.linearalgebra.interfaces.LinearAlgebraAlgorithm;

/** QR decomposition of matrix for finding eigen values*/
public class QR implements LinearAlgebraAlgorithm{
	
	@Autowired
	@Qualifier("Matrix")
	private Matrix A;
	
	@Autowired
	@Qualifier("Matrix")
	private Matrix Q;
		
	@Autowired
	@Qualifier("Matrix")
	private Matrix R;
	
	private float[] vectReal;
	private Complex[] vectComplex;
	
	private float eps;
	
	private void findComplex(int m){
		float a = 1.0f;
		float b = 0 - (A.matr[m][m] + A.matr[m+1][m+1]);
		float c = A.matr[m][m]*A.matr[m+1][m+1] - A.matr[m][m+1]*A.matr[m+1][m];
		float d = b*b - 4 * a * c;
		if(d >= 0){			
			float val1 = (float) ((-b + Math.sqrt(d))/(2*a));
			float val2 = (float) ((-b - Math.sqrt(d))/(2*a));
			vectReal[m] = val1;
			vectReal[m + 1] = val2;
		}
		else{
			float real = (-b) / (2 * a);
			float imaginary = (float) (Math.sqrt(-d)/(2 * a)); 
			Complex first = new Complex(real,imaginary);
			Complex second = new Complex(real,-imaginary);
			vectComplex[m] = first;
			vectComplex[m + 1] = second;
		}
	}
	private Matrix multVtranspOnV(Vector<Float> v){
		Matrix res = new Matrix(A.n);
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < v.size(); j++){
				res.matr[i][j] = v.get(i)*v.get(j);
			}
		}
		return res;
	}
	private Matrix findHausholder(Vector<Float> v){
		Matrix E = new Matrix(A.n);
		E.initSingleMatrix();
		Matrix H = new Matrix();
		Matrix vTransv = multVtranspOnV(v);
		float k = (2 / Matrix.scalarMult(v, v));
		Matrix.multMatrixOnScal(vTransv, k);
		H = Matrix.calcDifferenceMatrix(E, vTransv);
		return H;
	}
	private int signX(float x){
		if(x < 0){
			return -1;
		}
		else{
			return 1;
		}
	}
	private float calcNormB(int i){
		float res = 0;
		for(int j = i; j < A.n;j++){
			res += A.matr[j][i] * A.matr[j][i];
		}
		return (float) Math.sqrt(res);
	}
	
	private Vector<Float> findUVect(int i){
		Vector<Float> res = new Vector<Float>();
		res.setSize(A.n);
		for(int j = 0; j <A.n; j++){
			if(j < i){
				res.set(j,new Float(0));				
			}
			else if(j > i){
				res.set(j,new Float(A.matr[j][i]));
			}
		}
		float vi = A.matr[i][i] + signX(A.matr[i][i]) * calcNormB(i);
		res.set(i,new Float(vi));
		return res;
	}
	
	private void findQR(){
		for(int i = 0; i < A.n - 1;i++){
			Vector<Float> v = findUVect(i);
			Matrix H = findHausholder(v);
			Q = Matrix.multiplicationMatrix(Q, H);
			A = Matrix.multiplicationMatrix(H, A);
		}
		R.copyMatrix(R, A);
	}
	
	private boolean isComplex(float sum,int i){
		float tmp = A.matr[i + 1][i]*A.matr[i+1][i];
		return !(tmp <= eps) && ((sum - tmp)<=eps) ;
	}
	
	private boolean endCriterion(){
		boolean res = true;
		for(int i = 0; i < A.n;i++){
			float tmp = 0;
			for(int j = i; j < A.n -1 ;j++){
				tmp += A.matr[j + 1][i] * A.matr[j+1][i];
			}
			if(tmp <= eps){
				vectReal[i] = A.matr[i][i];
			}
			else if(isComplex(tmp,i)){
				findComplex(i);
				i++;
			}
			else{
				res = false;
			}
		}
		return res;
	}
	
	@Override
	public void algorithm() {
		do{
			findQR();
			A = Matrix.multiplicationMatrix(R,Q);
			Q.initSingleMatrix();
		}
		while(!endCriterion());
		printResult();
	}
	
	@Override
	public void init() {
		String nameFile = Main.getFileWithInputData();
		File file = new File("C:\\Users\\User\\Desktop\\inputMatrix5.txt");
		//File file = new File(nameFile);
		try {
			Scanner scan = new Scanner(file);
			A.readMatrFromFile(scan);
			eps = scan.nextFloat();
			R.setN(A.n);
			R.setMatr(new float[A.n][A.n]);
			Q.setN(A.n);
			Q.setMatr(new float[A.n][A.n]);
			Q.initSingleMatrix();
			vectReal = new float[A.n];
			vectComplex = new Complex[A.n];
		} catch (FileNotFoundException e){
			System.out.println("Can not open file:");
			e.printStackTrace();
		}
	}

	@Override
	public void printResult() {
		// TODO Auto-generated method stub
		File result = new File("C:\\Users\\User\\Desktop\\result.txt");
		try {
			if(!result.exists()){
				result.createNewFile();				
			}
			PrintWriter writer = new PrintWriter(result);
			writer.write("Вещественные cобственные значения:\r\n");
			for(int i = 0; i < A.n;i++){
				if(vectReal[i]!=0){
					writer.write(vectReal[i] + "\r\n");				
				}
			}
			writer.write("Комплексные cобственные значения:\r\n");
			for(int i = 0; i < A.n;i++){
				Complex tmp = vectComplex[i];
				if(tmp!=null){
					tmp.printComplex(writer);			
				}
			}
			writer.close();
			OpenResult.launch("C:\\Users\\User\\Desktop\\result.txt");
		}catch(Exception ex){
			System.out.println("Can not write in file:");
			ex.printStackTrace();
		}
	}

}
