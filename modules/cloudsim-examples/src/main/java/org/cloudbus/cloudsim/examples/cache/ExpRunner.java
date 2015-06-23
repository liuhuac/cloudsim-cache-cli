package org.cloudbus.cloudsim.examples.cache;

import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.cache.algorithms.ClassificationVmAllocationPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.ClassificationVmSelectionPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.HeuristicVmAllocationPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.HeuristicVmSelectionPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.MissrateVmAllocationPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.MissrateVmSelectionPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.OptimalVmAllocationPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.OptimalVmSelectionPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.RandomVmAllocationPolicy;
import org.cloudbus.cloudsim.examples.cache.algorithms.RandomVmSelectionPolicy;
import org.cloudbus.cloudsim.examples.power.RunnerAbstract;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;


public class ExpRunner extends RunnerAbstract {
	
	public ExpRunner(
			boolean enableOutput,
			boolean outputToFile,
			String inputFolder,
			String outputFolder,
			String workload,
			String vmAllocationPolicy,
			String vmSelectionPolicy,
			String parameter) {
		super(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.examples.power.RunnerAbstract#init(java.lang.String)
	 */
	@Override
	protected void init(String inputFolder) {
		try {
			CloudSim.init(1, Calendar.getInstance(), false);
			
			/*
			 * Either initialize the CacheMatrix here
			 * or initialize it in Simulation.java to
			 * ensure using the same CacheMatrix for
			 * different allocation algorithms.
			 */
			/*CacheMatrix cm = new CacheMatrix();
			cm.init();*/

			broker = ExpHelper.createBroker();
			int brokerId = broker.getId();

			cloudletList = ExpHelper.createCloudletList(brokerId, ExpConstants.NUMBER_OF_VMS);
			vmList = ExpHelper.createVmList(brokerId, cloudletList.size());
			hostList = ExpHelper.createHostList(ExpConstants.NUMBER_OF_HOSTS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
			System.exit(0);
		}
	}
	
	/**
	 * Gets the vm allocation policy.
	 * 
	 * @param vmAllocationPolicyName the vm allocation policy name
	 * @param vmSelectionPolicyName the vm selection policy name
	 * @param parameterName the parameter name
	 * @return the vm allocation policy
	 */
	protected VmAllocationPolicy getVmAllocationPolicy(
			String vmAllocationPolicyName,
			String vmSelectionPolicyName,
			String parameterName) {
		VmAllocationPolicy vmAllocationPolicy = null;
		PowerVmSelectionPolicy vmSelectionPolicy = null;
		if (!vmSelectionPolicyName.isEmpty()) {
			vmSelectionPolicy = getVmSelectionPolicy(vmSelectionPolicyName);
		}
		double parameter = 0;
		if (!parameterName.isEmpty()) {
			parameter = Double.valueOf(parameterName);
		}
		if (vmAllocationPolicyName.equals("heuristic")) {
			vmAllocationPolicy = new HeuristicVmAllocationPolicy(
					hostList,
					(HeuristicVmSelectionPolicy) vmSelectionPolicy,
					parameter);
		} else if (vmAllocationPolicyName.equals("random")) {
			vmAllocationPolicy = new RandomVmAllocationPolicy(
					hostList,
					(RandomVmSelectionPolicy) vmSelectionPolicy,
					parameter);
		} else if (vmAllocationPolicyName.equals("optimal")) {
			vmAllocationPolicy = new OptimalVmAllocationPolicy(
					hostList,
					(OptimalVmSelectionPolicy) vmSelectionPolicy,
					parameter);
		} else if (vmAllocationPolicyName.equals("classification")) {
			vmAllocationPolicy = new ClassificationVmAllocationPolicy(
					hostList,
					(ClassificationVmSelectionPolicy) vmSelectionPolicy,
					parameter);
		} else if (vmAllocationPolicyName.equals("missrate")) {
			vmAllocationPolicy = new MissrateVmAllocationPolicy(
					hostList,
					(MissrateVmSelectionPolicy) vmSelectionPolicy,
					parameter);
		} else {
			System.out.println("Unknown VM allocation policy: " + vmAllocationPolicyName);
			System.exit(0);
		}
		return vmAllocationPolicy;
	}
	
	/**
	 * Gets the vm selection policy.
	 * 
	 * @param vmSelectionPolicyName the vm selection policy name
	 * @return the vm selection policy
	 */
	protected PowerVmSelectionPolicy getVmSelectionPolicy(String vmSelectionPolicyName) {
		PowerVmSelectionPolicy vmSelectionPolicy = null;
		if (vmSelectionPolicyName.equals("heuristic")) {
			vmSelectionPolicy = new HeuristicVmSelectionPolicy();
		} else if (vmSelectionPolicyName.equals("random")) {
			vmSelectionPolicy = new RandomVmSelectionPolicy();
		} else if (vmSelectionPolicyName.equals("optimal")) {
			vmSelectionPolicy = new OptimalVmSelectionPolicy();
		} else if (vmSelectionPolicyName.equals("classification")) {
			vmSelectionPolicy = new ClassificationVmSelectionPolicy();
		} else if (vmSelectionPolicyName.equals("missrate")) {
			vmSelectionPolicy = new MissrateVmSelectionPolicy();
		} else {
			System.out.println("Unknown VM selection policy: " + vmSelectionPolicyName);
			System.exit(0);
		}
		return vmSelectionPolicy;
	}
	
	
	/**
	 * Starts the simulation.
	 * 
	 * @param experimentName the experiment name
	 * @param outputFolder the output folder
	 * @param vmAllocationPolicy the vm allocation policy
	 */
	protected void start(String experimentName, String outputFolder, VmAllocationPolicy vmAllocationPolicy) {
		System.out.println("Starting " + experimentName);
		ExpConstants.CURRENT_EXP_NAME = experimentName;
		try {
			PowerDatacenter datacenter = (PowerDatacenter) ExpHelper.createDatacenter(
					"Datacenter",
					PowerDatacenter.class,
					hostList,
					vmAllocationPolicy);

			datacenter.setDisableMigrations(false);

			broker.submitVmList(vmList);
			broker.submitCloudletList(cloudletList);

			CloudSim.terminateSimulation(ExpConstants.SIMULATION_LIMIT);
			double lastClock = CloudSim.startSimulation();

			List<Cloudlet> newList = broker.getCloudletReceivedList();
			Log.printLine("Received " + newList.size() + " cloudlets");

			CloudSim.stopSimulation();

			ExpHelper.printResults(
					datacenter,
					vmList,
					lastClock,
					experimentName,
					ExpConstants.OUTPUT_CSV,
					outputFolder);

		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
			System.exit(0);
		}

		Log.printLine("Finished " + experimentName);
	}

}
