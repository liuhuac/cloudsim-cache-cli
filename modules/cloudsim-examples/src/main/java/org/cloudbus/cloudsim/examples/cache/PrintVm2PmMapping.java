package org.cloudbus.cloudsim.examples.cache;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class PrintVm2PmMapping {

	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static void print() throws Exception {
		// TODO Auto-generated method stub
		String fileName = "results/log/"+ExpConstants.CURRENT_EXP_NAME + ".txt";
		String ofileName = "results/mapping_"+ExpConstants.CURRENT_EXP_NAME + ".txt";
		
		FileInputStream fstream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		String strLine;

		String map = "";
		//Read File Line By Line
		int i = 0;
		while ((strLine = br.readLine()) != null)   {
			if(strLine.contains("0.1: Broker:")){
				i++;
				String[] parts = strLine.split("\\s+");
				String vmid = parts[3].substring(1);
				String pmid = parts[11].substring(1);
				if(0==ExpConstants.PROFILE_TYPE){
					map = map + vmid + " " + pmid + "\n";
				} else {
					String fn = CacheMatrix.VM_NAME_LIST.get(Integer.valueOf(vmid));
					fn = fn.substring(0, 2) + "." + fn.substring(2, 3) + "." + fn.substring(3,4);
					map = map + fn + " " + pmid + "\n";
				}				
			}
			if(i==ExpConstants.NUMBER_OF_VMS){
				break;
			}
		}

		ExpHelper.writeDataRow(map, ofileName);
		
		//Close the input stream
		br.close();
	}

}
