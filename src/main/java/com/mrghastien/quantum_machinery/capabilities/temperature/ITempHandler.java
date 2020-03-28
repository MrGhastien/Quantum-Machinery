package com.mrghastien.quantum_machinery.capabilities.temperature;

public interface ITempHandler {
	
	
	double getTemp();
	
	double getMaxTemp();
		
	double heat(double temp, boolean simulate);
	
	double cool(double temp, boolean simulate);
	
	double getNaturalCoolingRate();
}
