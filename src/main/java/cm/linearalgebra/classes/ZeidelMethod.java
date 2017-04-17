package cm.linearalgebra.classes;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Required;

/** Zeidel method of solving linear system */
public class ZeidelMethod extends SimpleIterationMethod{
	private void calcAandB(Matrix alpha,Matrix B,Matrix C){
		for(int i=0;i < alpha.n;i++){
			for(int j = 0; j<alpha.n;j++){
				if(i==j){
					break;
				}
				B.matr[i][j] = alpha.matr[i][j]; 
			}
		}
		
		C.matr = Matrix.calcDifferenceMatrix(alpha, B).matr;
	}
	@Override
	public void algorithm(){
		Matrix B = new Matrix(A.n);
		Matrix C = new Matrix(A.n);
		Matrix alpha = calcAlphaMatrix();
		Vector<Float> beta = calcBetaVect();
		float accuracy = 2;
		double normAlpha = alpha.calcNormMatrix();
		for(int i = 0; i < A.n; i++){
			float elem = beta.get(i);
			X.add(i,new Float(elem));
		}
		calcAandB(alpha,B,C);
		while(accuracy > eps){
			numbIter++;
			Vector<Float> X_prev = new Vector<Float>(X);
			for(int i = 0; i < A.n ; i++){
				float Xi = beta.get(i);
				for(int j = 0;j < i; j++){
					Xi += B.matr[i][j]*X.get(j);  
				}
				for(int j = i;j < A.n; j++){
					Xi += C.matr[i][j]*X_prev.get(j);   
				}
				X.set(i, new Float(Xi));
			}
			if(normAlpha < 1){ 	
				accuracy = calcAccuracy(normAlpha,X_prev);
			}
			else{
				accuracy = calcAccuracy(normAlpha,X_prev,1);		
			}
		}
		printResult();
	}
}
