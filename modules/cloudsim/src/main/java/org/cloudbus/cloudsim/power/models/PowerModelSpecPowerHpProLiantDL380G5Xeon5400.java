package org.cloudbus.cloudsim.power.models;

public class PowerModelSpecPowerHpProLiantDL380G5Xeon5400 extends PowerModelSpecPower {

	/** The power. Need to calibrate*/
	private final double[] power = { 93.7, 97, 101, 105, 110, 116, 121, 125, 129, 133, 135 };

	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int)
	 */
	@Override
	protected double getPowerData(int index) {
		return power[index];
	}

}
