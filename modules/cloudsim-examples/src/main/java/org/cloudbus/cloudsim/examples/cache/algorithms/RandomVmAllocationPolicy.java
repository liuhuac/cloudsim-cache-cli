package org.cloudbus.cloudsim.examples.cache.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.cache.CacheMatrix;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.util.ExecutionTimeMeasurer;


public class RandomVmAllocationPolicy extends PowerVmAllocationPolicyMigrationStaticThreshold{

	public RandomVmAllocationPolicy(List<? extends Host> hostList,
			RandomVmSelectionPolicy vmSelectionPolicy,
			double utilizationThreshold) {
		super(hostList, vmSelectionPolicy, utilizationThreshold);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Find host for vm.
	 * 
	 * @param vm the vm
	 * @param excludedHosts the excluded hosts
	 * @return the power host
	 */
	public PowerHost findHostForVm(Vm vm, Set<? extends Host> excludedHosts) {

		List<PowerHost> candidateHostList = new ArrayList<PowerHost>();

		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (excludedHosts.contains(host)) {
				continue;
			}
			if (host.isSuitableForVm(vm)) {
				if (getUtilizationOfCpuMips(host) != 0 && isHostOverUtilizedAfterAllocation(host, vm)) {
					continue;
				}
				
				if (host.isSuitableForVm(vm)) {
					if (host.getUtilizationOfCpuMips() != 0 && isHostOverUtilizedAfterAllocation(host, vm)) {
						continue;
					}
					candidateHostList.add(host);
				}
			}
		}	
		if(candidateHostList.isEmpty()){
			return null;
		} else {
			Random rd = new Random();
			int rdHostId = rd.nextInt(candidateHostList.size());
			return candidateHostList.get(rdHostId);
		}	
	}
	
	/**
	 * Optimize allocation of the VMs according to current utilization.
	 * 
	 * @param vmList the vm list
	 * 
	 * @return the array list< hash map< string, object>>
	 */
	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
		
		/*
		 * Calculate total pain
		 */
		Log.printLine();
		double total_pain = 0.0;
		for(PowerHostUtilizationHistory host : this.<PowerHostUtilizationHistory> getHostList()){
			double host_pain = 0.0;
			for(Vm vm_i : host.getVmList()){
				for(Vm vm_j : host.getVmList()){
					host_pain += CacheMatrix.get_pain(vm_i.getId(),vm_j.getId());
				}
			}
			Log.printLine("Pain of Host "+host.getId()+" : "+host_pain);
			total_pain += host_pain;
		}
		Log.printLine("==========================");
		Log.printLine("Total pain : "+total_pain);
		Log.printLine("==========================");
		Log.printLine();
		
		
		ExecutionTimeMeasurer.start("optimizeAllocationTotal");

		ExecutionTimeMeasurer.start("optimizeAllocationHostSelection");
		List<PowerHostUtilizationHistory> overUtilizedHosts = getOverUtilizedHosts();
		getExecutionTimeHistoryHostSelection().add(
				ExecutionTimeMeasurer.end("optimizeAllocationHostSelection"));

		printOverUtilizedHosts(overUtilizedHosts);

		saveAllocation();

		ExecutionTimeMeasurer.start("optimizeAllocationVmSelection");
		List<? extends Vm> vmsToMigrate = getVmsToMigrateFromHosts(overUtilizedHosts);
		getExecutionTimeHistoryVmSelection().add(ExecutionTimeMeasurer.end("optimizeAllocationVmSelection"));

		Log.printLine("Reallocation of VMs from the over-utilized hosts:");
		ExecutionTimeMeasurer.start("optimizeAllocationVmReallocation");
		List<Map<String, Object>> migrationMap = getNewVmPlacement(vmsToMigrate, new HashSet<Host>(
				overUtilizedHosts));
		getExecutionTimeHistoryVmReallocation().add(
				ExecutionTimeMeasurer.end("optimizeAllocationVmReallocation"));
		Log.printLine();

		/*
		 * Comment this line to disable turning off underutilized hosts
		 */
		//migrationMap.addAll(getMigrationMapFromUnderUtilizedHosts(overUtilizedHosts));

		restoreAllocation();

		getExecutionTimeHistoryTotal().add(ExecutionTimeMeasurer.end("optimizeAllocationTotal"));

		return migrationMap;
	}

}
