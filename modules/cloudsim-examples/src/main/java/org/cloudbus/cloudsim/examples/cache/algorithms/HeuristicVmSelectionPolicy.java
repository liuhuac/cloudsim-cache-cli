package org.cloudbus.cloudsim.examples.cache.algorithms;

import java.util.List;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.cache.CacheMatrix;
import org.cloudbus.cloudsim.examples.cache.CacheVm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;

import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

public class HeuristicVmSelectionPolicy extends PowerVmSelectionPolicy{

	@Override
	public Vm getVmToMigrate(PowerHost host) {
		// TODO Auto-generated method stub
		List<PowerVm> migratableVms = getMigratableVms(host);
		if (migratableVms.isEmpty()) {
			return null;
		} else if(1 == migratableVms.size()){
			return migratableVms.get(0);
		} else if(2 == migratableVms.size()){
			CacheVm vm_1 =  (CacheVm)migratableVms.get(0);
			CacheVm vm_2 =  (CacheVm)migratableVms.get(1);
			// vm with higher hits are more sensitive to cache contention
			return vm_1.getH() > vm_2.getH() ? vm_1 : vm_2;
		}
		
		double maxPain = 0.0;
		CacheVm vm_i = null;
		CacheVm vm_j = null;
		for(Vm i : migratableVms){
			for(Vm j : migratableVms){
				if(i.getPainWithVm(j.getId()) > maxPain){
					maxPain = i.getPainWithVm(j.getId());
					vm_i = (CacheVm) i;
					vm_j = (CacheVm) j;
				}
			}
		}

		// vm with higher hits are more sensitive to cache contention
		if(null==vm_i||null==vm_j) return null;
		CacheVm return_vm = vm_i.getH() > vm_j.getH() ? vm_i : vm_j;
		
		/*
		 * update host cache pain information
		 * in CacheMatrix.HOST_PAIN_LIST
		 */
		CacheMatrix.update_host_pain_remove_vm(return_vm, host);
		/*
		 * end update host cache pain information
		 */
		return return_vm;
	}

}
