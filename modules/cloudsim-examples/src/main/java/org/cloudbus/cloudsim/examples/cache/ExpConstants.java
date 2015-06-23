package org.cloudbus.cloudsim.examples.cache;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantDL380G5Xeon5400;
//import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
//import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;

/**
 * If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 *
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 *
 * @author Anton Beloglazov
 * @since Jan 6, 2012
 */
public class ExpConstants {
	
	public static int NUMBER_OF_VMS = 8;
	public static int NUMBER_OF_HOSTS = 4;
	
	// stack distance profile trace folder
	public static String TRACE_FOLDER = "trace";
	
	// 0 for random profile; 1 for trace
	public static int PROFILE_TYPE = 0;
	
	public static String THRESHOLD = "1";

	/*
	 * parameters for generating vm stack distance profiles
	 * used in StackDistanceProfileModel.java
	 * y = a*e^(-(1/b)*x+(c/200))
	 */
	public static int PROFILE_MODEL_A = 200; // a = [0,A]
	public static int PROFILE_MODEL_B_1 = 20; // [0,B1]
	public static int PROFILE_MODEL_B_2 = 30; // b = B2 + [0,B1]
	public static int PROFILE_MODEL_C = 200; // c = [0,C]
	// end of profile parameters
	
	public final static long CLOUDLET_UTILIZATION_SEED = 1;
	

	public final static boolean ENABLE_OUTPUT = true;
	public final static boolean OUTPUT_CSV    = false;
	
	public static boolean OUTPUT_MAP    = false;

	public final static double SCHEDULING_INTERVAL = 300;
	public static double SIMULATION_LIMIT = 301;

	public final static int CLOUDLET_LENGTH	= 2500 * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_PES	= 1;

	public static String CURRENT_EXP_NAME; // variable for storing exp name, used by PrintVm2PmMapping
	
	/*
	 * VM instance types:
	 *   High-Memory Extra Large Instance: 3.25 EC2 Compute Units, 8.55 GB // too much MIPS
	 *   High-CPU Medium Instance: 2.5 EC2 Compute Units, 0.85 GB
	 *   Extra Large Instance: 2 EC2 Compute Units, 3.75 GB
	 *   Small Instance: 1 EC2 Compute Unit, 1.7 GB
	 *   Micro Instance: 0.5 EC2 Compute Unit, 0.633 GB
	 *   We decrease the memory size two times to enable oversubscription
	 *
	 */
	public final static int VM_TYPES	= 4;
	public final static int[] VM_MIPS	= { 2500, 2000, 1000, 500 };
	public final static int[] VM_PES	= { 1, 1, 1, 1 };
	public final static int[] VM_RAM	= { 870,  1740, 1740, 613 };
	public final static int VM_BW		= 100000; // 100 Mbit/s
	public final static int VM_SIZE		= 2500; // 2.5 GB

	/*
	 * Host types:
	 *   HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB)
	 *   HP ProLiant ML110 G5 (1 x [Xeon 3075 2660 MHz, 2 cores], 4GB)
	 *   We increase the memory size to enable over-subscription (x4)
	 */
	/*public final static int HOST_TYPES	 = 2;
	public final static int[] HOST_MIPS	 = { 1860, 2660 };
	public final static int[] HOST_PES	 = { 2, 2 };
	public final static int[] HOST_RAM	 = { 4096, 4096 };
	public final static int HOST_BW		 = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000; // 1 GB
	 */
	
	
	/*
	 * Host types:
	 *   HP ProLiant DL380 G5 (1 x [Xeon 3075 3160 MHz, 4 cores], 4GB)
	 *   We increase the memory size to enable over-subscription (x4)
	 */
	public final static int HOST_TYPES	 = 1;
	public final static int[] HOST_MIPS	 = { 3160 };
	public final static int[] HOST_PES	 = { 4 };
	public final static int[] HOST_RAM	 = { 4096 };
	public final static int HOST_BW		 = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000; // 1 GB

	public final static PowerModel[] HOST_POWER = {
		new PowerModelSpecPowerHpProLiantDL380G5Xeon5400()
		//new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		//new PowerModelSpecPowerHpProLiantMl110G5Xeon3075()
	};

}
