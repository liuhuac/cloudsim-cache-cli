package org.cloudbus.cloudsim.examples.cache.algorithms;

import org.cloudbus.cloudsim.examples.cache.ExpConstants;
import org.cloudbus.cloudsim.examples.cache.ExpRunner;


public class HeuristicAlgorithm {

	/**
	 * @param args
	 */
	public static double [] hostPainList;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean enableOutput = true;
		boolean outputToFile = true;
		String inputFolder = "workloads";
		String outputFolder = "results";
		String workload = "random"; // Random workload
		String vmAllocationPolicy = "heuristic"; // 
		String vmSelectionPolicy = "heuristic";
		String parameter = ExpConstants.THRESHOLD;

		new ExpRunner(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}

}
