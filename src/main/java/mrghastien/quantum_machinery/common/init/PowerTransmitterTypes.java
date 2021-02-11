package mrghastien.quantum_machinery.common.init;

public enum PowerTransmitterTypes {

	BASIC(256),
	ASTRONIUM(1024);
	
	private int transferRate;
	
	private PowerTransmitterTypes(int transferRate) {
		
	}
	
	public int getTransferRate() {
		return transferRate;
	}
	
}
