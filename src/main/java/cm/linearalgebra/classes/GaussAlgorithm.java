package cm.linearalgebra.classes;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cm.linearalgebra.interfaces.LinearAlgebraAlgorithm;

/** Gauss algorithm of solving linear system of equation.
 * 	Implements by LU decomposition.
 * @author dimakolyandra
 */
public class GaussAlgorithm implements LinearAlgebraAlgorithm{
	
	/** L-matrix of decomposition */
	@Autowired
	@Qualifier("Matrix")
	private Matrix L;

	/** U-matrix of decomposition */
	@Autowired
	@Qualifier("Matrix")
	private Matrix U;
	
	/** Input matrix */
	@Autowired
	@Qualifier("Matrix")
	private Matrix A;
	
	/** Inverse matrix for input */
	@Autowired
	@Qualifier("Matrix")
	private Matrix inverse;
	
	/** Vector of right parts of linear system */
	@Autowired
	@Qualifier("Vect")
	private Vector<Float> vectValues;
	
	/** Vector of solving system */
	@Autowired
	@Qualifier("Vect")
	private Vector<Float> x;
	
	/** Vector permute rows */
	private Permutation[] perm;
	
	/** Number of permutation */
	private int numbPermute;
	
	/** Extended matrix of system */
	private float[][] extendedMatrix;
	
	/** Determinant of matrix */
	private float detA;
	
	/** Extended single matrix*/
	private float[][] extendedSingleMatrix;

	public Matrix getInverse() {
		return inverse;
	}

	public void setInverse(Matrix inverse) {
		inverse = inverse;
	}
	
	public Vector<Float> getVectValues() {
		return vectValues;
	}

	public void setVectValues(Vector<Float> vectValues) {
		this.vectValues = vectValues;
	}
	
	public Matrix getA() {
		return A;
	}

	public void setA(Matrix a) {
		A = a;
	}

	public Matrix getL() {
		return L;
	}

	public void setL(Matrix l) {
		L = l;
	}

	public Matrix getU() {
		return U;
	}

	public void setU(Matrix u) {
		U = u;
	}

	public float getDetA() {
		return detA;
	}

	public void setDetA(float detA) {
		this.detA = detA;
	}
	
	/** Choose leading element and
	 *  if it is need permute strings
	 * @param a Index of string
	 * @param b Index of column
	 * @param numstr Number of strings
	 * @param numcol Number of columns
	 * @param matr Matrix
	 */
	private void chooseLeadingElemAndChangeStr(int a,int b,int numstr,int numcol,float[][] matr){
		int i = a;
		int j =b;
		if(matr[a][b] == 0){
			while(matr[i][j]==0 && i < numstr){
				i++;
			}
			perm[numbPermute].x = a;
			perm[numbPermute].y = i;
			numbPermute++;
			swapStr(a,b,i,matr,numcol);
		}
		else{
			return;
		}
	}
	
	/** Adds columns */
	private void sumColumn(Vector<Float> column,int a,int b,int numcolumn, float[][] matr){
		int k = 0;
		for(int j = b; j < numcolumn; j++){
			matr[a][j] = (float)column.get(k) + matr[a][j];
			k++;
		}
	}
	
	/** Elementary transformations of matrix */
	private void elementaryConversion(int a,int b,int numstr,int numcol,float[][]matr,boolean isInverse){
		for(int i = a; i < numstr - 1; i++){
			float k =(-matr[i + 1][b])/matr[a][b];
			if(!isInverse){
				L.matr[i + 1][b] = -k;	
			}
			Vector<Float> newCol = new Vector<Float>();
			for(int j = b; j < numcol;j++){
				float tmp = matr[a][j] * k;
				newCol.add(new Float(tmp));
			}
			sumColumn(newCol,i+1,b,numcol,matr);
		}
	}
	
	/** Initialize L-matrix */
	private void initL(){
		L.setMatr(new float[A.n][A.n]);
		L.setN(A.n);
		for(int i=0; i < A.n;i++){
			L.matr[i][i] = 1;
		}
	}

	/** Initialize U-matrix */
	private void initU(){
		U.setMatr(new float[A.n][A.n]);
		U.setN(A.n);
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n;j++){
				U.matr[i][j] = extendedMatrix[i][j];				
			}
		}
	}
	

	/** Permute vector of right parts */
	private void permuteVectValues(){
		for(int i = 0; i < numbPermute;i++){
			Permutation p = perm[i];
			float k = vectValues.get(p.x);
			float j = vectValues.get(p.y);
			vectValues.set(p.x,j);
			vectValues.set(p.y,k);
		}	
	}
	

	/** Calculate determinant of input Matrix*/
	private void determinantA(){
		detA = (float)Math.pow(-1, numbPermute);
		for(int i = 0; i < A.n;i++){
			detA = detA * extendedMatrix[i][i];
		}
	}
	
	/** Find inverse matrix */
	private void findInverse(){
		float [][] inv = new float[A.n][A.n];
		int k = 0;
		for(int j=A.n; j < 2*A.n;j++){
			Vector<Float> tmp = new Vector<Float>();
			for(int i = 0; i < A.n; i++){
				tmp.add(new Float(extendedSingleMatrix[i][j]));
			}
			Vector<Float> Xinv = new Vector<Float>();
			solvSystemUX(tmp, Xinv);
			for(int l = 0; l < A.n;l++){
				inv[l][k] = Xinv.get(l);	
			}
			k++;
		}
		inverse.setMatr(inv);
		inverse.setN(A.n);
	}
	
	/** Gauss algorithm */
	private void gauss(boolean isInverse, float[][]matr, int numstr,int numcol){
		for(int i = 0; i < numstr ;i++){
			chooseLeadingElemAndChangeStr(i,i,numstr,numcol,matr);
			elementaryConversion(i,i,numstr,numcol,matr,isInverse);
		}
		if(!isInverse){
			initU();
			permuteL();
			permuteVectValues();
			Vector<Float> z = solvSystemLZ(vectValues);
			solvSystemUX(z,x);
			determinantA();			
		}
		else{
			findInverse();
		}
	}
	
	/** Method that rules Gauss algorithm */
	@Override
	public void algorithm() {
		initL();
		gauss(false,extendedMatrix,A.n,A.n+1);
		gauss(true,extendedSingleMatrix,A.n,2*A.n);
		printResult();
	}
	
	/** Change strings */
	private void swapStr(int a,int b,int i,float[][] matr,int numcol){
		for(int k = b; k < numcol;k++){
			float tmp = matr[i][k];
			matr[i][k] = matr[a][k];
			matr[a][k] = tmp;
		}
	}
	
	/** Change columns*/
	private void swapColumn(int a, int b,float[][] matr){
		for(int k = 0; k < A.n; k++){
			float tmp = matr[k][a];
			matr[k][a] = matr[k][b];
			matr[k][b] = tmp;		
		}
	}
	
	/** Make need permutation in L-matrix */
	private void permuteL(){
		for(int i = 0; i < numbPermute;i++){
			Permutation p = perm[i];
			swapStr(p.x,0,p.y,L.matr,A.n);
			swapColumn(p.x,p.y,L.matr);
		}
	}
	
	/** Solve system UX = z */
	private void solvSystemUX(Vector<Float> z,Vector<Float> X){
		float xn = (float)z.get(z.size()-1) / U.matr[U.n-1][U.n-1]; 
		X.setSize(A.n);
		X.set(A.n - 1,new Float(xn));
		for(int i = U.n-2; i >= 0;i--){
			float tmp = 0;
			for(int j = i + 1; j < U.n;j++){
				tmp += U.matr[i][j] * X.get(j);
			}
			float xi = (z.get(i) - tmp)/U.matr[i][i];
			X.set(i,new Float(xi));
		}
	}
	
	/** Solve system LZ = B */
	private Vector<Float> solvSystemLZ(Vector<Float> vect){
		Vector<Float> result = new Vector<Float>();
		result.add(vect.get(0));
		for(int i = 1; i < L.n;i++){
			float tmp = 0;
			for(int j = 0; j < i;j++){
				tmp += L.matr[i][j] * result.get(j);
			}
			float bi = vect.get(i);
			result.add(new Float(bi - tmp));
		}
		return result;
 	}
	
	/** Print extended matrix */
	private void printExtended(int numstr, int numcol,float[][] matr){
		System.out.println("Extended:");
		for(int i = 0; i < numstr;i++){
			for(int j = 0; j < numcol;j++){
				System.out.print(matr[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	/** Make extended matrix */
	private void makeExtendedMatrix(){
		extendedMatrix = new float[A.n][2*A.n];
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n;j++){
				extendedMatrix[i][j] = A.matr[i][j];
			}
			extendedMatrix[i][A.n] = (float)vectValues.get(i);
		}
	}
	
	private void makeSingleExtendedMatrix(){
		extendedSingleMatrix = new float[A.n][2*A.n];
		for(int i = 0; i < A.n;i++){
			for(int j = 0; j < A.n;j++){
				extendedSingleMatrix[i][j] = A.matr[i][j];
			}
		}
		for(int i = 0; i < A.n;i++){
			for(int j = A.n; j < 2*A.n;j++){
				if(i!=j%A.n){
					extendedSingleMatrix[i][j] = 0;
				}
				else{
					extendedSingleMatrix[i][j] = 1;
				}
			}
		}
	}
	
	/** Init array of permutation */
	public void initPerm(){
		perm = new Permutation[A.n];
		numbPermute = 0;
		for(int i = 0; i < A.n;i++){
			perm[i] = new Permutation();
		}
	}
	
	/** Init object and read input data from file */
	@Override
	public void init() {
		try{
			String nameFile = Main.getFileWithInputData();
			File file = new File(nameFile);
			
			Scanner scan = new Scanner(file);
			A.readMatrFromFile(scan);
			detA=0;
			initPerm();
			for(int i = 0; i < A.n;i++){
				float a = scan.nextFloat();
				vectValues.add(new Float(a));
			}
			makeExtendedMatrix();
			makeSingleExtendedMatrix();
		}catch(Exception ex){
			System.out.println("Can not open file:");
			ex.printStackTrace();
		}
	}
	
	/** Print results into file */
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
			int i = A.n;
			for(float tmp:x){
				writer.write("X" + i + "=" + tmp + " ");
				i--;
			}
			writer.write("Определитель: " + detA + "\r\n" );
			writer.write("Матрица L:\r\n");
			L.printMatr(writer);
			writer.write("Матрица U:\r\n");
			U.printMatr(writer);
			writer.write("Обратная матрица:\r\n");
			inverse.printMatr(writer);
			writer.close();
			OpenResult.launch("C:\\Users\\User\\Desktop\\result.txt");
		}catch(Exception ex){
			System.out.println("Can not write in file:");
			ex.printStackTrace();
		}
	}
	
	/** Class which store permutation */
	class Permutation{
		int x;
		int y;
		Permutation(){
			x = 0;
			y = 0;
		}
	}
}
