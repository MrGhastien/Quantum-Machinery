package mrghastien.quantum_machinery.common.multiblocks;

import java.util.ArrayList;

public class MultiBlockHandler {
	
	static ArrayList<IMultiBlock> multiblocks = new ArrayList<>();
	
	public static void registerMultiblock(IMultiBlock multiblock) {
		multiblocks.add(multiblock);
	}
	
	public static ArrayList<IMultiBlock> getMultiblocks() {
		return multiblocks;
	}
	
	public static void setup() {
		multiblocks.forEach(m -> m.generateStructure());
	}
	
}
