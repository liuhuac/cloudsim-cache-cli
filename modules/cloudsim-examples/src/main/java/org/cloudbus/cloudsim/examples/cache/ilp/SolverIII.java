package org.cloudbus.cloudsim.examples.cache.ilp;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import org.cloudbus.cloudsim.examples.cache.CacheMatrix;
import org.cloudbus.cloudsim.examples.cache.CliParser;
import org.cloudbus.cloudsim.examples.cache.ExpConstants;

public class SolverIII {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		CliParser cli = new CliParser();
//		cli.parse(args);
		
		CacheMatrix cm = new CacheMatrix();
		cm.init();
		
		SolverIII helper = new SolverIII();
		
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
			
			/*Note that for add_constraint (and add_constraintex when colno is NULL) 
			element 0 of the array is not considered (i.e. ignored). Column 1 is 
			element 1, column 2 is element 2, ...*/
			
			double[] coef = new double[ijk+1]; // see Note
			helper.CoefReset(coef, 0, ijk-1);
			
			// y_{ijk}==y_{jik}
			for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){
				for(int i=0; i<ExpConstants.NUMBER_OF_VMS-1; i++){
					for(int j=i+1; j<ExpConstants.NUMBER_OF_VMS; j++){
						helper.CoefSet(coef, i, j, k, 1);
						helper.CoefSet(coef, j, i, k, -1);
						solver.addConstraint(coef, LpSolve.EQ, 0);
						//helper.Print(coef, false);
						helper.CoefSet(coef, i, j, k, 0);
						helper.CoefSet(coef, j, i, k, 0);
					}
				}
			}
			
			// y_{ijk}\leq y_{iik}+y_{jjk} and y_{ijk}\geq y_{iik}+y_{jjk}-1
			for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){
				for(int i=0; i<ExpConstants.NUMBER_OF_VMS-1; i++){
					for(int j=i+1; j<ExpConstants.NUMBER_OF_VMS; j++){
						helper.CoefSet(coef, i, j, k, 1);						
						helper.CoefSet(coef, i, i, k, -1);
						helper.CoefSet(coef, j, j, k, -1);
						solver.addConstraint(coef, LpSolve.LE, 0);
						solver.addConstraint(coef, LpSolve.GE, -1);
						helper.CoefSet(coef, i, j, k, 0);						
						helper.CoefSet(coef, i, i, k, 0);
						helper.CoefSet(coef, j, j, k, 0);
					}
				}
			}
			
			int size = ExpConstants.NUMBER_OF_VMS * ExpConstants.NUMBER_OF_VMS;
			double upper = 16;
			// \sum_{i=1}^{n_t}\sum_{j=1}^{n_t}y_{ijk} \leq c_{k}^2
			/*for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){	
				helper.CoefSetRange(coef, k*size, (k+1)*size-1, 1);//set
				solver.addConstraint(coef, LpSolve.LE, upper);
				//helper.Print(coef, false);
				helper.CoefSetRange(coef, k*size, (k+1)*size-1, 0);//reset
			}*/
			
			upper = 1;
			// \sum_{k=1}^{n_p}y_{ijk} \leq 1
			for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
				for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){
					for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){//set
						helper.CoefSet(coef, i, j, k, 1);
					}
					solver.addConstraint(coef, LpSolve.LE, upper);
					//helper.Print(coef, false);
					for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){//reset
						helper.CoefSet(coef, i, j, k, 0);
					}
				}
			}
			
			/*int ceil = (int)Math.ceil(1.0*ExpConstants.NUMBER_OF_VMS/ExpConstants.NUMBER_OF_HOSTS);
			int floor = (int)Math.floor(1.0*ExpConstants.NUMBER_OF_VMS/ExpConstants.NUMBER_OF_HOSTS);
			int remain = ExpConstants.NUMBER_OF_VMS % ExpConstants.NUMBER_OF_HOSTS;
			double lower = ceil*ceil*remain+floor*floor*(ExpConstants.NUMBER_OF_HOSTS-remain);
			// \sum_{k=1}^{N_{p}}\sum_{i=1}^{N_{v}}\sum_{j=1}^{N_{v}}y_{ijk}\geq
			helper.CoefSetRange(coef, 0, ijk-1, 1);//set
			solver.addConstraint(coef, LpSolve.GE, lower);
			//helper.Print(coef, false);
			helper.CoefReset(coef, 0, ijk-1);//reset*/			

			
			for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
				for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){
					helper.CoefSet(coef, i, i, k, 1);
				}
			}
			solver.addConstraint(coef, LpSolve.GE, ExpConstants.NUMBER_OF_VMS);
			helper.CoefReset(coef, 0, ijk-1);//reset
			
			
			/*
			 * set objective function
			 */
			for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
				for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){
					for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){
						helper.CoefSet(coef, i, j, k, CacheMatrix.get_pain(i, j));
					}
				}
			}
			solver.setObjFn(coef);
			solver.setMinim();
			
			// solve the problem
			solver.solve();
			
			System.out.println("Value of objective function: " + solver.getObjective());
			
			double[] var = solver.getPtrVariables();
			/*for (int i = 0; i < var.length; i++) {
			  System.out.println("Value of var[" + i + "] = " + var[i]);
			}*/
			helper.Print(var, true);
			
			//solver.printConstraints(1);
			
			// delete the problem and free memory
			System.out.println();
			solver.deleteLp();
			
		}
		catch (LpSolveException e) {
			e.printStackTrace();
		}
	}

	public void CoefReset(double[] coef, int left, int right){
		// all plus one, see Note
		left++;
		right++;
		for(int i=left; i<=right; i++){
			coef[i] = 0;
		}
	}
	
	public void CoefSet(double[] coef, int i, int j, int k, double val){
		coef[k*ExpConstants.NUMBER_OF_VMS*ExpConstants.NUMBER_OF_VMS+i*ExpConstants.NUMBER_OF_VMS+j+1] = val;// plus one
	}
	
	public void CoefSetRange(double[] coef, int left, int right, double val){
		// all plus one, see Note
		left++;
		right++;
		for(int i=left; i<=right; i++){
			coef[i] = val;
		}
	}
	
	public void Print(double[] var, boolean isPrintResult){
		int shift = isPrintResult ? 0 : 1;
		for(int k=0; k<ExpConstants.NUMBER_OF_HOSTS; k++){
			for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
				for(int j=0; j<ExpConstants.NUMBER_OF_VMS; j++){
					System.out.print((int)var[k*ExpConstants.NUMBER_OF_VMS*ExpConstants.NUMBER_OF_VMS+i*ExpConstants.NUMBER_OF_VMS+j+shift]);
					System.out.print(" ");
				}
				System.out.print("\n");
			}
			System.out.print("\n");
		}
	}
	
}
