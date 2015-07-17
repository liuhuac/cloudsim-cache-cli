package org.cloudbus.cloudsim.examples.cache.ilp;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import org.cloudbus.cloudsim.examples.cache.CacheMatrix;
import org.cloudbus.cloudsim.examples.cache.ExpConstants;

public class SolverII {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CacheMatrix cm = new CacheMatrix();
		cm.init();
		
		try {
			// Create a problem with |y_{ijk}| variables and 0 constraints
			int ijk = ExpConstants.NUMBER_OF_VMS * // i
					ExpConstants.NUMBER_OF_VMS *   // j
					ExpConstants.NUMBER_OF_HOSTS;  // k
	
			LpSolve solver = LpSolve.makeLp(0, ijk);
	
			// claim that y_{ijk} is binary
			for(int p=1; p<=ijk; p++){
				solver.setBinary(p, true);
			}
			
			
			
		}
		catch (LpSolveException e) {
			e.printStackTrace();
		}
	}

	public static void MatrixReset(int[][][] matrix){// k,i,j
		int K = matrix.length;
		int I = matrix[0].length;
		int J = matrix[0][0].length;
		for(int k=0; k<K; k++){
			for(int i=0; i<I; i++){
				for(int j=0; j>J; j++){
					matrix[k][i][j] = 0;
				}
			}
		}
	}
	public static String Matrix2String(int[][][] matrix){// k,i,j
		String str = "";
		int K = matrix.length;
		int I = matrix[0].length;
		int J = matrix[0][0].length;
		for(int k=0; k<K; k++){
			for(int i=0; i<I; i++){
				for(int j=0; j>J; j++){
					str += matrix[k][i][j]==1 ? "1 " : "0 ";
				}
			}
		}
		return str;
	}
}
