package com.mrghastien.quantum_machinery.datagen;

import com.mrghastien.quantum_machinery.datagen.providers.ModBlockStateProvider;
import com.mrghastien.quantum_machinery.datagen.providers.ModItemModelProvider;
import com.mrghastien.quantum_machinery.datagen.providers.ModItemTagsProvider;
import com.mrghastien.quantum_machinery.datagen.providers.ModRecipeProvider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {
	
	@SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if(event.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
            generator.addProvider(new ModItemTagsProvider(generator));
        }
        if(event.includeClient()) {
        	generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
        	generator.addProvider(new ModItemModelProvider(generator, event.getExistingFileHelper()));
        }
    }

}
