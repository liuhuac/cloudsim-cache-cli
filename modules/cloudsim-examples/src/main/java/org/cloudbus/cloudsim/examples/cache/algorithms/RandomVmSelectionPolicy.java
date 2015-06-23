package org.cloudbus.cloudsim.examples.cache.algorithms;

import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.cache.CacheVm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

public class RandomVmSelectionPolicy extends PowerVmSelectionPolicy{

	@Override
	public Vm getVmToMigrate(PowerHost host) {
		// TODO Auto-generated method stub
		
		// random select a vm to migrate
		List<PowerVm> migratableVms = getMigratableVms(host);
		int number = migratableVms.size();
		if (0 == number) {
			return null;
		}
		
		Random rd = new Random();
		int id = rd.nextInt(number);

		CacheVm return_vm = (CacheVm) migratableVms.get(id);
		
		return return_vm;
	}

}
