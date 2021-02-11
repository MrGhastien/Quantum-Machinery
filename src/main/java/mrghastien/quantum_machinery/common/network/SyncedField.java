package mrghastien.quantum_machinery.common.network;

import java.lang.reflect.Field;

import mrghastien.quantum_machinery.common.capabilities.energy.MachineEnergyStorage;

import net.minecraft.network.PacketBuffer;

public abstract class SyncedField<T> {

	public final Field field;
	public final Object instance;
	protected T lastValue;
	
	public SyncedField(Object instance, Field field) {
		this.field = field;
		field.setAccessible(true);
		this.instance = instance;
	}
	
	public boolean updated() {
		T value = null;
		try {
			value = retrieveValue();
			if(lastValue == null && value != null || lastValue != null && !lastValue.equals(value)) {
				lastValue = value == null ? null : copyValue(value);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected T copyValue(T oldValue) {
		return oldValue;
	}
	
	@SuppressWarnings("unchecked")
	protected T retrieveValue() throws Exception {
		return (T) field.get(instance);
	}
	
	protected void injectValue(T value) throws Exception {
		field.set(instance, value);
	}

	public T getValue() {
		return lastValue;
	}
	
	public void setValue(T value) {
		try {
			injectValue(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class SyncedInt extends SyncedField<Integer> {

		public SyncedInt(Object instance, Field field) {
			super(instance, field);
		}
	}
	
	public static class SyncedFloat extends SyncedField<Float> {

		public SyncedFloat(Object instance, Field field) {
			super(instance, field);
		}
	}
	
	public static class SyncedDouble extends SyncedField<Double> {

		public SyncedDouble(Object instance, Field field) {
			super(instance, field);
		}
	}
	
	public static class SyncedBoolean extends SyncedField<Boolean> {

		public SyncedBoolean(Object instance, Field field) {
			super(instance, field);
		}
	}
	
	public static class SyncedString extends SyncedField<String> {

		public SyncedString(Object instance, Field field) {
			super(instance, field);
		}
	}
	
	public static class SyncedEnergyStorage extends SyncedField<MachineEnergyStorage> {

		public SyncedEnergyStorage(Object instance, Field field) {
			super(instance, field);
		}
		
		@Override
		protected MachineEnergyStorage copyValue(MachineEnergyStorage oldValue) {
			return oldValue.copy();
		}
	}
	
	public static SyncedField<?> create(Object instance, Field field) {
		if(int.class.isAssignableFrom(field.getType())) return new SyncedInt(instance, field);
		if(float.class.isAssignableFrom(field.getType())) return new SyncedFloat(instance, field);
		if(double.class.isAssignableFrom(field.getType())) return new SyncedDouble(instance, field);
		if(boolean.class.isAssignableFrom(field.getType())) return new SyncedBoolean(instance, field);
		if(String.class.isAssignableFrom(field.getType())) return new SyncedString(instance, field);
		if(MachineEnergyStorage.class.isAssignableFrom(field.getType())) return new SyncedEnergyStorage(instance, field);
		return null;
	}
	
	public byte getType() {
		if(this instanceof SyncedInt) return 0;
		else if(this instanceof SyncedFloat) return 1;
		else if(this instanceof SyncedDouble) return 2;
		else if(this instanceof SyncedBoolean) return 3;
		else if(this instanceof SyncedString) return 4;
		else if(this instanceof SyncedEnergyStorage) return 5;
		else
			throw new IllegalArgumentException("Wrong sync type ! " + getValue().getClass().getName());
	}
	
	public static void encode(PacketBuffer buf, Object value, byte type) {
		switch (type) {
			case 0:
				buf.writeInt((int) value);
				break; 
			case 1:
				buf.writeFloat((float) value);
				break;
			case 2:
				buf.writeDouble((double) value);
				break;
			case 3:
				buf.writeBoolean((boolean) value);
				break;
			case 4:
				buf.writeString((String) value);
				break;
			case 5:
				buf.writeInt(((MachineEnergyStorage) value).getMaxEnergyStored());
				buf.writeInt(((MachineEnergyStorage) value).getMaxReceive());
				buf.writeInt(((MachineEnergyStorage) value).getMaxExtract());
				buf.writeInt(((MachineEnergyStorage) value).getEnergyStored());
				buf.writeInt(((MachineEnergyStorage) value).getOutput());
				buf.writeInt(((MachineEnergyStorage) value).getInput());
				break;
			default:
				throw new IllegalArgumentException("Wrong sync type ! " + type);
		}
	}
	
	public static Object decode(PacketBuffer buf, byte type) {
		switch (type) {
			case 0:
				return buf.readInt();
			case 1:
				return buf.readFloat();
			case 2:
				return buf.readDouble();
			case 3:
				return buf.readBoolean();
			case 4:
				return buf.readString();
			case 5:
				int cap = buf.readInt();
				int maxR = buf.readInt();
				int maxE = buf.readInt();
				int e = buf.readInt();
				int lastE = buf.readInt();
				int lastR = buf.readInt();
				return new MachineEnergyStorage(cap, maxR, maxE, e, lastR, lastE);
			default:
				throw new IllegalArgumentException("Wrong sync type ! " + type);
		}
	}
	
 }
