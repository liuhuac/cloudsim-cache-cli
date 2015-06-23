package org.cloudbus.cloudsim.examples.cache;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.examples.cache.CacheMatrix.Animal;
import org.cloudbus.cloudsim.power.PowerVm;

public class CacheVm extends PowerVm {

	/**
	 * Instantiates a new power vm.
	 * 
	 * @param id the id
	 * @param userId the user id
	 * @param mips the mips
	 * @param pesNumber the pes number
	 * @param ram the ram
	 * @param bw the bw
	 * @param size the size
	 * @param priority the priority
	 * @param vmm the vmm
	 * @param cloudletScheduler the cloudlet scheduler
	 * @param schedulingInterval the scheduling interval
	 */
	private double missrate = 0.0;
	
	public CacheVm(
			final int id,
			final int userId,
			final double mips,
			final int pesNumber,
			final int ram,
			final long bw,
			final long size,
			final int priority,
			final String vmm,
			final CloudletScheduler cloudletScheduler,
			final double schedulingInterval) {
		super(id, userId, mips, pesNumber, ram, bw, size, priority, vmm, cloudletScheduler, schedulingInterval);
		setSchedulingInterval(schedulingInterval);
		setMissrate();
	}

	public double getMissrate(){
		return this.missrate;
	}
	
	private void setMissrate(){
		if(getZ()==0){
			this.missrate = 0.0;
		} else {
			this.missrate = ((double)(getZ() - getH())) / ((double)getZ());
		}		
	}
	
	public int getH(){
		return CacheMatrix.VM_H_LIST.get(this.getId());
	}
	
	public int getZ(){
		return CacheMatrix.VM_Z_LIST.get(this.getId());
	}
	
	public Animal getVmType(){
		return CacheMatrix.VM_TYPE_LIST.get(this.getId());
	}
	
	public boolean isDveil(){
		return Animal.DEVIL == this.getVmType() ? true : false;
	}
}
