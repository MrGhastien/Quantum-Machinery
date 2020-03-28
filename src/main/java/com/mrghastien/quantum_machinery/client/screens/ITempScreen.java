package com.mrghastien.quantum_machinery.client.screens;

public interface ITempScreen {
	
	void syncTemp(double temp, double maxTemp);
	
	double getClientTemp();
	
	double getClientMaxTemp();

}
