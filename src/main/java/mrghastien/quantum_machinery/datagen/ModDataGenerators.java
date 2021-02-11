package mrghastien.quantum_machinery.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import mrghastien.quantum_machinery.datagen.providers.ModBlockStateProvider;
import mrghastien.quantum_machinery.datagen.providers.ModBlockTagsProvider;
import mrghastien.quantum_machinery.datagen.providers.ModItemModelProvider;
import mrghastien.quantum_machinery.datagen.providers.ModItemTagsProvider;
import mrghastien.quantum_machinery.datagen.providers.ModRecipeProvider;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {
	
	@SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if(event.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
            ModBlockTagsProvider blocks = new ModBlockTagsProvider(generator);
            generator.addProvider(new ModItemTagsProvider(generator, blocks));
            generator.addProvider(blocks);
        }
        if(event.includeClient()) {
        	generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
        	generator.addProvider(new ModItemModelProvider(generator, event.getExistingFileHelper()));
        }
    }

}
