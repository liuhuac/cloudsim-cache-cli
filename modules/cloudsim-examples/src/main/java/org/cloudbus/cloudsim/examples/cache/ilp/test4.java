package org.cloudbus.cloudsim.examples.cache.ilp;

import org.cloudbus.cloudsim.examples.cache.ExpConstants;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class test4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
			
			// index of y_{ijk} is (i+j*n_t+k*n_t*n_t)+1
			// +1 because LpSolve index starts from 1
			String coeff = "";		
			for(int kk=0; kk<ExpConstants.NUMBER_OF_HOSTS; kk++){
				coeff = "";
				for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){	
					for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
						for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){
							if(k == kk){
								coeff += "1 ";
							} else {
								coeff += "0 ";
							} 
						}
					}
				}
				System.out.println(coeff);
			}
			
			
			


		      // add constraints
		      solver.strAddConstraint("60 60", LpSolve.GE, 300);
		      solver.strAddConstraint("12 6", LpSolve.GE, 36);
		      solver.strAddConstraint("10 30", LpSolve.GE, 90);

		      // set objective function
		      solver.strSetObjFn("0.12 0.15");

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
