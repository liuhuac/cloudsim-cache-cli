package org.cloudbus.cloudsim.examples.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;

public class CacheMatrix {
	
	public static int A = 128;
	
	enum Animal {
		TURTLE,
		DEVIL,
		RABBIT,
		SHEEP
	}

	public static List<Animal> VM_TYPE_LIST = null;
	
	public static List<String> VM_NAME_LIST = null;
	
	/*
	 *  Each row of PROFILE_MATRIX is a stack distance profile
	 *  n_t (rows) x A (cols)  
	 *  n_t is the total number of VMs 
	 */
	public static List<List<Integer>> PROFILE_MATRIX = null;
	
	/*
	 *  Each element of PAIN_MATRIX is the pain between VM i and j
	 *  n_t (rows) x n_t (cols)
	 *  n_t is the total number of VMs  
	 */
	public static List<List<Double>> PAIN_MATRIX = null;
	
	public static List<Integer> VM_H_LIST = null;//total hits
	public static List<Integer> VM_Z_LIST = null;
	
	public static List<Double> HOST_PAIN_LIST = null;// host total pain of current vms
	public static List<Double> HOST_MISSRATE_LIST = null;// host total pain of current vms
	
	@SuppressWarnings("unused")
	public void init(){
		
		PROFILE_MATRIX = new ArrayList<List<Integer>>();
		VM_NAME_LIST = new ArrayList<String>();
		
		for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
			if(1==ExpConstants.PROFILE_TYPE) {				
				PROFILE_MATRIX.add(random_read_from_folder(ExpConstants.TRACE_FOLDER));
			} else if(0==ExpConstants.PROFILE_TYPE){
				PROFILE_MATRIX.add(random_profile());
				/*System.out.println(PROFILE_MATRIX.get(0).toString());
				System.exit(0);*/
			}
			
		}
		
		init_pain_matrix();
		
		init_hzm_list();
		
		HOST_PAIN_LIST = new ArrayList<Double>();
		for(int i=0; i<ExpConstants.NUMBER_OF_HOSTS; i++){
			HOST_PAIN_LIST.add(0.0);
		}
		
		HOST_MISSRATE_LIST = new ArrayList<Double>();
		for(int i=0; i<ExpConstants.NUMBER_OF_HOSTS; i++){
			HOST_MISSRATE_LIST.add(0.0);
		}
		
	}

	private void init_hzm_list() {
		// TODO Auto-generated method stub
		VM_H_LIST = new ArrayList<Integer>();
		VM_Z_LIST = new ArrayList<Integer>();
		
		for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
			int h = 0;
			int z = 0;
			int k = 0;
			for(; k<A-1; k++){
				h += PROFILE_MATRIX.get(i).get(k).intValue();
			}
			z = h + PROFILE_MATRIX.get(i).get(k).intValue();
			
			VM_H_LIST.add(h);
			VM_Z_LIST.add(z);
		}
	}
	
	public void init_animal_classification() {
		// TODO Auto-generated method stub
		VM_TYPE_LIST = new ArrayList<Animal>();

		for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
			double h = (double)VM_H_LIST.get(i); // total hits
			double z = (double)VM_Z_LIST.get(i); // total accesses
			double m = z - h; // total misses
			double mr = m / z; // miss rate
			
			// calculate WaysNeeded k%
			int k95;
			if(0 == z){// NOT zero access
				k95 = 0;
			} else {
				double sum = 0;
				for(k95=0; k95<A; k95++){
					sum += PROFILE_MATRIX.get(i).get(k95).doubleValue();
					if(sum/z >= 0.95){
						
					}
				}
			}
			 
			/*
			 * Animal Classification Algorithm
			 */
			if(h < 1000){
				VM_TYPE_LIST.add(Animal.TURTLE);
			} else if(mr > 0.1 || m > 4000){
				VM_TYPE_LIST.add(Animal.DEVIL);
			} else if(k95 > A/2){
				VM_TYPE_LIST.add(Animal.RABBIT);
			} else {
				VM_TYPE_LIST.add(Animal.SHEEP);
			}

		}
	}

	private void init_pain_matrix() {
		// TODO Auto-generated method stub
		PAIN_MATRIX = new ArrayList<List<Double>>();
		
		for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
			List<Double> row = new ArrayList<Double>();
			PAIN_MATRIX.add(row);
		}
		
		for(int i=0; i<ExpConstants.NUMBER_OF_VMS; i++){
			for(int j=i; j<ExpConstants.NUMBER_OF_VMS; j++){
				
				if(i == j){
					PAIN_MATRIX.get(i).add((double) 0);
					continue;
				}
				
				double pain_i = 0, pain_j = 0;
				double s_i = 0, s_j = 0;
				double z_i = 0, z_j = 0;
				double p_i, p_j;
				
				for(int k=0; k<A; k++){
					int data_i = PROFILE_MATRIX.get(i).get(k).intValue();
					int data_j = PROFILE_MATRIX.get(j).get(k).intValue();										
					
					if(i != A){
						if(0==data_i && 0==data_j){
							p_i = p_j = 0;
						} else {
							p_i = ((double) data_i) / (data_i + data_j);
							p_j = 1 - p_i;
						}
												
						s_i += p_j * data_i;
						s_j += p_i * data_j;
					}
														
					z_i += data_i;
					z_j += data_j;
				}
				
				pain_i = s_i * z_j;
				pain_j = s_j * z_i;
				
				PAIN_MATRIX.get(i).add((double)(pain_i+pain_j));
				PAIN_MATRIX.get(j).add((double)(pain_i+pain_j));
			}
		}
		
	}

	@SuppressWarnings("unused")
	private List<Integer> random_profile() {
		// TODO Auto-generated method stub
		List<Integer> profile = new ArrayList<Integer>();
		StackDistanceProfileModel sdpm = new StackDistanceProfileModel();
		int i,sum=0;
		for(i=0; i<A-1; i++){
			profile.add((int)sdpm.get(i));
		}
		for(; i<A+50; i++){
			sum += (int)sdpm.get(i);
		}
		profile.add(sum);
		return profile;
	}

	@SuppressWarnings("unused")
	private List<Integer> random_read_from_folder(String folderName) {		
		Path path = Paths.get(folderName);
		File dir = path.toFile();
		String[] fileNames = dir.list();
		File[] files = dir.listFiles();
		Random rd = new Random();
		int index = rd.nextInt(fileNames.length);
		String rFileName = fileNames[index];
		//String fileName = folderName + "\\" + rFileName;
		
		VM_NAME_LIST.add(rFileName);
		
		int scale = 100000;

		List<Integer> profile = new ArrayList<Integer>();
		File file = files[index];
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			scan = null;
			e.printStackTrace();
		}					//fix the folder path
		for(int i = 0; i < A; i++) {
			String str = scan.nextLine();
			str = str.substring(str.indexOf(":")+2);
			profile.add(Integer.parseInt(str)/scale);
		}
		int sum = 0;
		while(scan.hasNext()){
			String str = scan.nextLine();
			str = str.substring(str.indexOf(":")+2);
			sum += Integer.parseInt(str)/scale;
		}
		profile.add(sum);
		scan.close();
		return profile;
	}
	
	@SuppressWarnings("unused")
	private List<Integer> read_profile(String fileName) {
		List<Integer> profile = new ArrayList<Integer>();
		File file = new File(fileName);
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			scan = null;
			e.printStackTrace();
		}					//fix the folder path
		for(int i = 0; i <= A; i++) {
			String str = scan.nextLine();
			str = str.substring(str.indexOf(":")+2);
			profile.add(Integer.parseInt(str));
		}
		scan.close();
		return profile;
	}
	
	public void save_random_profile(String filename){
		// TODO Save the random generated profile matrix to file
	}
	
	/*
	 * get the pain of co-locating vm i and vm j 
	 */
	public static double get_pain(int i, int j){
		return PAIN_MATRIX.get(i).get(j).doubleValue();
	}
	
	/*
	 * get the augment pain amount resulted 
	 * from placing vm i to host
	 */
	public static double get_pain_with_host(int i, Host host){
		double sumPain = 0.0;
		List<Vm> vmlist = host.getVmList();
		for(Vm v : vmlist){
			int j = v.getId();
			if(i == j){
				return 0.0;//vm alredy in the host
			}
			sumPain += get_pain(i, j);
		}
		return sumPain;
	}
	
	/*
	 * get the total missrate of host
	 */
	public static double get_host_missrate(int hostId){
		return HOST_MISSRATE_LIST.get(hostId).doubleValue();
	}
	
	
	/*
	 * get the vm type 
	 */
	public static Animal get_vm_type(int i){
		return VM_TYPE_LIST.get(i);
	}
	
	
	/*
	 * update host cache pain information
	 */
	public static void update_host_pain_add_vm(Vm vm, Host host){
		List<Vm> vmlist = host.getVmList();
		if(vmlist.size() > 1){// there is other vms besides the new vm
			double sum_pain = 0.0;
			for(Vm v : vmlist){
				int e_id = v.getId(); // existing vm id
				int v_id = vm.getId(); // new vm id
				if( e_id == v_id){
					continue;
				} else {
					sum_pain += vm.getPainWithVm(e_id);
				}
			}
			CacheMatrix.HOST_PAIN_LIST.set(host.getId(), sum_pain);
		}
	}
	
	
	/*
	 * update host cache pain information
	 */
	public static void update_host_pain_remove_vm(Vm vm, Host host){
		List<Vm> vmlist = host.getVmList();
		if(vmlist.size() > 1){// there is other vms besides the new vm
			double sum_pain = 0.0;
			for(Vm v : vmlist){
				int e_id = v.getId(); // existing vm id
				int v_id = vm.getId(); // new vm id
				if( e_id == v_id){
					continue;
				} else {
					sum_pain += vm.getPainWithVm(e_id);
				}
			}
			int host_id = host.getId();
			double current = CacheMatrix.HOST_PAIN_LIST.get(host_id);
			CacheMatrix.HOST_PAIN_LIST.set(host_id, current - sum_pain);
		}
	}
	
	/*
	 * update host cache missrate information
	 */
	public static void update_host_missrate_add_vm(Vm vm, Host host){
			double vmMissrate = ((CacheVm)vm).getMissrate();
			double current = CacheMatrix.HOST_MISSRATE_LIST.get(host.getId());
			CacheMatrix.HOST_MISSRATE_LIST.set(host.getId(), current+vmMissrate);
	}
	
	
	/*
	 * update host cache missrate information
	 */
	public static void update_host_missrate_remove_vm(Vm vm, Host host){
		List<Vm> vmlist = host.getVmList();
		int host_id = host.getId();
		if(vmlist.size() == 0){// host is empty
			CacheMatrix.HOST_MISSRATE_LIST.set(host_id, 0.0);;
		} else {
			double vmMissrate = ((CacheVm)vm).getMissrate();
			double current = CacheMatrix.HOST_MISSRATE_LIST.get(host_id);
			CacheMatrix.HOST_MISSRATE_LIST.set(host.getId(), current-vmMissrate);
		}
	}
}
