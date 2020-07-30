package com.mrghastien.quantum_machinery.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;

import java.util.Optional;
import java.util.function.Function;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.init.MetalType;
import com.mrghastien.quantum_machinery.common.init.ModItems;
import com.mrghastien.quantum_machinery.common.init.ModTags;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}
	
	@Override
	public String getName() {
		return "Item Tags: " + QuantumMachinery.MODID;
	}

	@Override
	protected void registerTags() {
		getBuilder(ModTags.Items.COAL_DUST).add(ModItems.COAL_DUST.get());
		
		for(MetalType metal : MetalType.values()) {
			metal.getOreTag().ifPresent(t -> copy(t, metal.getOreItemTag().get()));
			metal.getStorageBlockTag().ifPresent(t -> copy(t, metal.getStorageBlockItemTag().get()));
			metal.getIngotTag().ifPresent(t -> metal.getIngot().ifPresent(i -> getBuilder(t).add(metal.getIngot().get())));
			metal.getDustTag().ifPresent(t -> getBuilder(t).add(metal.getDust().get()));
			metal.getNuggetTag().ifPresent(t -> metal.getNugget().ifPresent(i -> getBuilder(t).add(metal.getNugget().get())));
			metal.getShredTag().ifPresent(t -> getBuilder(t).add(metal.getShred().get()));
		}
		addMetalTagsToTag(Tags.Items.DUSTS, MetalType::getDustTag, ModTags.Items.COAL_DUST);
		addMetalTagsToTag(Tags.Items.INGOTS, MetalType::getIngotTag);
		addMetalTagsToTag(Tags.Items.NUGGETS, MetalType::getNuggetTag);
		addMetalTagsToTag(ModTags.Items.SHREDS, MetalType::getShredTag);
	}
	
	@SafeVarargs
    private final void addMetalTagsToTag(Tag<Item> tag, Function<MetalType, Optional<Tag<Item>>> tagGetter, Tag<Item>... extras) {
        Tag.Builder<Item> builder = getBuilder(tag);
        for (MetalType metal : MetalType.values()) {
            tagGetter.apply(metal).ifPresent(builder::add);
        }
        for (Tag<Item> extraTag : extras) {
            builder.add(extraTag);
        }
    }
	
}
