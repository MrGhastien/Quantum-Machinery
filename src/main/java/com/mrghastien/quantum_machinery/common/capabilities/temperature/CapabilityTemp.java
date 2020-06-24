package com.mrghastien.quantum_machinery.common.capabilities.temperature;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityTemp {

	@CapabilityInject(ITempHandler.class)
    public static Capability<ITempHandler> HEAT = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ITempHandler.class, new IStorage<ITempHandler>()
        {
            @Override
            public INBT writeNBT(Capability<ITempHandler> capability, ITempHandler instance, Direction side)
            {
                return DoubleNBT.valueOf(instance.getTemp());
            }

            @Override
            public void readNBT(Capability<ITempHandler> capability, ITempHandler instance, Direction side, INBT nbt)
            {
                if (!(instance instanceof TempHandler))
                    throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                ((TempHandler)instance).temp = ((DoubleNBT)nbt).getDouble();
            }
        },
        () -> new TempHandler(1000.0d));
    }
    
}
