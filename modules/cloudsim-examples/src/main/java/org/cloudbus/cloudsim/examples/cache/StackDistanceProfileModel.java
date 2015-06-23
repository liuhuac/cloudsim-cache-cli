package org.cloudbus.cloudsim.examples.cache;

import java.util.Random;

public class StackDistanceProfileModel {

	/*
	 * y = a*e^(-(1/b)*x+(c/200))
	 */
	int a,b,c;
	
	public StackDistanceProfileModel(){
		shuffle();
	}
	
	public void shuffle(){
		Random rand = new Random();
		a = rand.nextInt(ExpConstants.PROFILE_MODEL_A);
		b = rand.nextInt(ExpConstants.PROFILE_MODEL_B_1)+ExpConstants.PROFILE_MODEL_B_2;
		c = rand.nextInt(ExpConstants.PROFILE_MODEL_C);
	}
	
	public double get(int x){
		return a*Math.exp(-(1.0/b)*x+(c/200));
	}
}
