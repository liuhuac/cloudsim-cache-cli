package org.cloudbus.cloudsim.examples.cache.ilp;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import org.cloudbus.cloudsim.examples.cache.CacheMatrix;
import org.cloudbus.cloudsim.examples.cache.ExpConstants;

public class Solver {

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
//			for(int p=1; p<=ijk; p++){
//				solver.setBinary(p, true);
//			}
			
			// index of y_{ijk} is (i+j*n_t+k*n_t*n_t)+1
			// +1 because LpSolve index starts from 1
			
			String coeff = "";		
			for(int kk=0; kk<ExpConstants.NUMBER_OF_HOSTS; kk++){
			//********begin********//
				coeff = "";
				for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){	
					for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){
						for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
							if(k == kk){
								coeff += "1 ";
							} else {
								coeff += "0 ";
							} 
						}
					}
				}
				/*
				 *  add constraints
				 */
				int upper = 2 * ExpConstants.NUMBER_OF_VMS * 4;
				//int lower = upper - ExpConstants.NUMBER_OF_VMS * ExpConstants.NUMBER_OF_VMS;
				
				// \sum_{i=1}^{n_t}\sum_{j=1}^{n_t}y_{ijk} \leq 2n_{t}c_{k}
				solver.strAddConstraint(coeff, LpSolve.LE, upper); 
				
				// 2n_{t}c_{k}-n_{t}^2 \leq \sum_{i=1}^{n_t}\sum_{j=1}^{n_t}y_{ijk}
				//solver.strAddConstraint(coeff, LpSolve.GE, 0);
//System.out.println();
System.out.print(coeff);	
System.out.println("<="+upper);
//System.out.println();
//System.out.println();
			//********end*********//
			} 
			
							
			for(int ii=0; ii<ExpConstants.NUMBER_OF_VMS; ii++){	
				for(int jj=0; jj<ExpConstants.NUMBER_OF_VMS; jj++){
				//********begin********//
					coeff = "";
					for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){	
						for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){
							for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
								if(i==ii && j==jj){
									coeff += "1 ";
								} else {
									coeff += "0 ";
								}
							}
						}
					}
					/*
					 *  add constraints
					 */
					
					// \sum_{k=1}^{n_p}y_{ijk} \leq 2
					solver.strAddConstraint(coeff, LpSolve.LE, 2); 
					
					// 1 \leq \sum_{k=1}^{n_p}y_{ijk}
					solver.strAddConstraint(coeff, LpSolve.GE, 1);
//System.out.println();
System.out.print("1<= ");
System.out.print(coeff);				
System.out.print("<=2");
System.out.println();
				//********end*********//
				}
			}
			
			
			/*
			 * set objective function
			 */
			coeff = "";
			for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){
				for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){	
					for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){			
						coeff += String.valueOf(CacheMatrix.get_pain(i, j)) + " ";
					}
				}
			}

System.out.println(coeff);
System.out.println();
			solver.strSetObjFn(coeff);
solver.setMinim();

			// solve the problem
			solver.solve();
			
			// print solution
			System.out.println("Value of objective function: " + solver.getObjective());
			double[] var = solver.getPtrVariables();
			for (int i = 0; i < var.length; i++) {
			  System.out.println("Value of var[" + i + "] = " + var[i]);
			}
			
			// delete the problem and free memory
			solver.deleteLp();
			
		}
		catch (LpSolveException e) {
			e.printStackTrace();
		}
	}
}
