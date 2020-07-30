package com.mrghastien.quantum_machinery.common.capabilities.temperature;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TempHandler implements ITempHandler , INBTSerializable<CompoundNBT>{

	protected double temp;
	protected double maxTemp;
	
	private LazyOptional<TempHandler> lazy;

	public TempHandler(double maxTemp) {
		this(maxTemp, 0);
	}
	
	public TempHandler(double maxTemp, double temp) {
		this.maxTemp = maxTemp;
		this.temp = temp;
		this.lazy = LazyOptional.of(() -> this);
	}
	
	@Override
	public double getTemp() {
		return temp;
	}
	
	public void setTemp(double temp) {
		this.temp = temp;
	}
	
	@Override
	public double getMaxTemp() {
		return maxTemp;
	}

	@Override
	public double heat(double temp, boolean simulate) {
		
		double heat = temp - getNaturalCoolingRate();
		if(heat <= 0) {
			return 0;
		}
		if(getTemp() + heat > getMaxTemp()) {
        	heat -= getTemp() + heat - getMaxTemp();
		}
		if(!simulate)
			this.temp += heat;
		return heat;
	}

	@Override
	public double cool(double temp, boolean simulate) {
		
		double heat = temp + getNaturalCoolingRate();
		if(heat <= 0) {
			return 0;
		}
		if(getTemp() - heat < 0) {
        	heat -= heat - getTemp();
		}
		if(!simulate)
			this.temp -= heat;
		return heat;
	}
	
	@Override
	public double getNaturalCoolingRate() {
		return temp / 30d;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putDouble("temp", getTemp());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setTemp(nbt.getDouble("temp"));
	}
	
	public LazyOptional<TempHandler> getLazy() {
		return lazy;
	}

}
