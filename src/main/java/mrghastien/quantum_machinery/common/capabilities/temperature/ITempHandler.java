package mrghastien.quantum_machinery.common.capabilities.temperature;

public interface ITempHandler {
	
	double getTemp();
	
	double getMaxTemp();
		
	double heat(double temp, boolean simulate);
	
	double cool(double temp, boolean simulate);
	
	double getNaturalCoolingRate();
}
