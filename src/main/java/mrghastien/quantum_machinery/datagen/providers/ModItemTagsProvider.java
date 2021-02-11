package mrghastien.quantum_machinery.datagen.providers;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

import java.util.Optional;
import java.util.function.Function;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.init.MetalType;
import mrghastien.quantum_machinery.common.init.ModItems;
import mrghastien.quantum_machinery.common.init.ModTags;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blocks) {
		super(generatorIn, blocks);
	}
	
	@Override
	public String getName() {
		return "Item Tags: " + QuantumMachinery.MODID;
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(ModTags.Items.COAL_DUST).add(ModItems.COAL_DUST.get());
		
		for(MetalType metal : MetalType.values()) {
			metal.getOreTag().ifPresent(t -> copy(t, metal.getOreItemTag().get()));
			metal.getStorageBlockTag().ifPresent(t -> copy(t, metal.getStorageBlockItemTag().get()));
			metal.getIngotTag().ifPresent(t -> metal.getIngot().ifPresent(i -> getOrCreateBuilder(t).add(metal.getIngot().get())));
			metal.getDustTag().ifPresent(t -> getOrCreateBuilder(t).add(metal.getDust().get()));
			metal.getNuggetTag().ifPresent(t -> metal.getNugget().ifPresent(i -> getOrCreateBuilder(t).add(metal.getNugget().get())));
			metal.getShredTag().ifPresent(t -> getOrCreateBuilder(t).add(metal.getShred().get()));
		}
		addMetalTagsToTag(Tags.Items.DUSTS, MetalType::getDustTag, ModTags.Items.COAL_DUST);
		addMetalTagsToTag(Tags.Items.INGOTS, MetalType::getIngotTag);
		addMetalTagsToTag(Tags.Items.NUGGETS, MetalType::getNuggetTag);
		addMetalTagsToTag(ModTags.Items.SHREDS, MetalType::getShredTag);
	}
	
	@SafeVarargs
    private final void addMetalTagsToTag(INamedTag<Item> tag, Function<MetalType, Optional<IOptionalNamedTag<Item>>> tagGetter, INamedTag<Item>... extras) {
		TagsProvider.Builder<Item> builder = getOrCreateBuilder(tag);
        for (MetalType metal : MetalType.values()) {
            tagGetter.apply(metal).ifPresent(builder::addTag);
        }
        for (INamedTag<Item> extraTag : extras) {
            builder.addTag(extraTag);
        }
    }
	
}
