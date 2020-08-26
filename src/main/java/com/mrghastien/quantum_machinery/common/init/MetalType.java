package com.mrghastien.quantum_machinery.common.init;

import static com.mrghastien.quantum_machinery.QuantumMachinery.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.mrghastien.quantum_machinery.common.blocks.MetalBlock;
import com.mrghastien.quantum_machinery.common.blocks.ModOreBlock;
import com.mrghastien.quantum_machinery.util.ItemHelper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public enum MetalType {
	IRON(build("iron").withDust().withIngotTag().withNuggetTag()),
	GOLD(build("gold").withDust().withIngotTag().withNuggetTag()),
	
	TITANIUM(buildMineral("titanium")),
	
	STEEL(buildAlloy("steel"));
	
	
	private final String oreName;
	private Supplier<Block> ore;
	private Supplier<Block> storageBlock;
	private Supplier<Item> ingot;
	private Supplier<Item> nugget;
	private Supplier<Item> dust;
	private Supplier<Item> shred;
	private final Tag<Block> oreTag;
	private final Tag<Block> storageBlockTag;
	private final Tag<Item> oreItemTag;
	private final Tag<Item> storageBlockItemTag;
	private final Tag<Item> ingotTag;
	private final Tag<Item> nuggetTag;
	private final Tag<Item> dustTag;
	private final Tag<Item> shredTag;
	
	private RegistryObject<Item> ingotRegistry;
	private RegistryObject<Item> dustRegistry;
	private RegistryObject<Item> nuggetRegistry;
	private RegistryObject<Item> shredRegistry;
	private RegistryObject<Block> oreRegistry;
	private RegistryObject<Block> storageBlockRegistry;
	
	private MetalType(Builder builder) {
		this(builder, builder.name + "_ore");
	}
	
	private MetalType(Builder builder, String oreName) {
		if(!builder.name.equals(this.getName())) {
			throw new IllegalArgumentException("Builder name should be the same as the enum field " + this.getName());
		}
		
		this.oreName = oreName;		
		this.ore = builder.ore;
		this.oreTag = builder.oreTag;
		this.storageBlock = builder.storageBlock;
		this.storageBlockTag = builder.storageBlockTag;
        this.oreItemTag = this.oreTag != null ? new ItemTags.Wrapper(this.oreTag.getId()) : null;
        this.storageBlockItemTag = this.storageBlockTag != null ? new ItemTags.Wrapper(this.storageBlockTag.getId()) : null;
		this.ingot = builder.ingot;
		this.ingotTag = builder.ingotTag;
		this.dust = builder.dust;
		this.dustTag = builder.dustTag;
		this.nugget = builder.nugget;
		this.nuggetTag = builder.nuggetTag;
		this.shred = builder.shred;
		this.shredTag = builder.shredTag;
	}
	
	public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
	
	public Optional<Block> getOre() {
        return ore != null ? Optional.of(ore.get()) : Optional.empty();
    }

    public Optional<Block> getStorageBlock() {
        return storageBlock != null ? Optional.of(storageBlock.get()) : Optional.empty();
    }

    public Optional<Item> getDust() {
        return dust != null ? Optional.of(dust.get()) : Optional.empty();
    }

    public Optional<Item> getIngot() {
        return ingot != null ? Optional.of(ingot.get()) : Optional.empty();
    }

    public Optional<Item> getNugget() {
        return nugget != null ? Optional.of(nugget.get()) : Optional.empty();
    }
    
    public Optional<Item> getShred() {
        return shred != null ? Optional.of(shred.get()) : Optional.empty();
    }

	public Optional<Tag<Block>> getOreTag() {
        return oreTag != null ? Optional.of(oreTag) : Optional.empty();
    }

    public Optional<Tag<Block>> getStorageBlockTag() {
        return storageBlockTag != null ? Optional.of(storageBlockTag) : Optional.empty();
    }

    public Optional<Tag<Item>> getOreItemTag() {
        return oreItemTag != null ? Optional.of(oreItemTag) : Optional.empty();
    }

    public Optional<Tag<Item>> getStorageBlockItemTag() {
        return storageBlockItemTag != null ? Optional.of(storageBlockItemTag) : Optional.empty();
    }

    public Optional<Tag<Item>> getDustTag() {
        return dustTag != null ? Optional.of(dustTag) : Optional.empty();
    }

    public Optional<Tag<Item>> getIngotTag() {
        return ingotTag != null ? Optional.of(ingotTag) : Optional.empty();
    }

    public Optional<Tag<Item>> getNuggetTag() {
        return nuggetTag != null ? Optional.of(nuggetTag) : Optional.empty();
    }
    
    public Optional<Tag<Item>> getShredTag() {
        return shredTag != null ? Optional.of(shredTag) : Optional.empty();
    }
    
    public Stream<Item> getSmeltables(boolean includeIngots) {
    	List<Item> collection = new ArrayList<>();
    	if(includeIngots)
    		getIngotTag().ifPresent(t -> collection.addAll(t.getAllElements()));
    	getDustTag().ifPresent(t -> collection.addAll(t.getAllElements()));
    	getIngotTag().ifPresent(t -> collection.addAll(t.getAllElements()));
    	return collection.stream();
    }
    
    private static Builder build(String name) {
    	return new Builder(name);
    }
    
    private static Builder buildAlloy(String name) {
    	return build(name).withBlock().withDust().withIngot().withNugget();
    }
    
    private static Builder buildMineral(String name) {
    	return build(name).withOre().withBlock().withDust().withIngot().withNugget().withShred();
    }
    
    public static void registerBlocks() {
    	for(MetalType metal : values()) {
    		if(metal.storageBlock != null) {
    			metal.storageBlockRegistry = ModBlocks.BLOCKS.register(metal.getName() + "_block", metal.storageBlock);
    			metal.storageBlock = metal.storageBlockRegistry;
    		}
    		if(metal.ore != null) {
    			metal.oreRegistry = ModBlocks.BLOCKS.register(metal.oreName, metal.ore);
    			metal.ore = metal.oreRegistry;
    		}
    	}
    }
    
    public static void registerItems() {
    	for(MetalType metal : values()) {
    		if(metal.ingot != null) {
    			metal.ingotRegistry = ModItems.ITEMS.register(metal.getName() + "_ingot", metal.ingot);
    			metal.ingot = metal.ingotRegistry;
    		}
    		if(metal.dust != null) {
    			metal.dustRegistry = ModItems.ITEMS.register(metal.getName() + "_dust", metal.dust);
    			metal.dust = metal.dustRegistry;
    		}
    		if(metal.nugget != null) {
    			metal.nuggetRegistry = ModItems.ITEMS.register(metal.getName() + "_nugget", metal.nugget);
    			metal.nugget = metal.nuggetRegistry;
    		}
    		if(metal.shred != null) {
    			metal.shredRegistry = ModItems.ITEMS.register(metal.oreName + "_shred", metal.shred);
    			metal.shred = metal.shredRegistry;
    		}
    	}
    }
	
	private static class Builder {
		
		final String name;
		Supplier<Block> ore;
		Supplier<Block> storageBlock;
		Supplier<Item> ingot;
		Supplier<Item> nugget;
		Supplier<Item> dust;
		Supplier<Item> shred;
		Tag<Block> oreTag;
		Tag<Block> storageBlockTag;
		Tag<Item> ingotTag;
		Tag<Item> nuggetTag;
		Tag<Item> dustTag;
		Tag<Item> shredTag;
		
		public Builder(String name) {
			this.name = name;
		}
		
		Builder withOre() {
			this.ore = ModOreBlock::new;
			this.oreTag = createBlockTag("ores/" + name);
			return this;
		}
		
		Builder withBlock() {
			this.storageBlock = MetalBlock::new;
			this.storageBlockTag = createBlockTag("storage_blocks/" + name);
			return this;
		}
		
		Builder withIngot() {
			this.ingot = () -> new Item(ItemHelper.defaultProperties());
			this.ingotTag = createItemTag("ingots/" + name);
			return this;
		}
		
		Builder withDust() {
			this.dust = () -> new Item(ItemHelper.defaultProperties());
			this.dustTag = createItemTag("dusts/" + name);
			return this;
		}
		
		Builder withNugget() {
			this.nugget = () -> new Item(ItemHelper.defaultProperties());
			this.nuggetTag = createItemTag("nuggets/" + name);
			return this;
		}
		
		Builder withShred() {
			this.shred = () -> new Item(ItemHelper.defaultProperties());
			//Using tag "chunk" instead of "shread", to ensure mod compatibility.
			this.shredTag = createItemTag(location("chunks/" + name));
			return this;
		}
		
		Builder withNuggetTag() {
			this.nuggetTag = createItemTag("nuggets/" + name);
			return this;
		}
		
		Builder withIngotTag() {
			this.ingotTag = createItemTag("ingots/" + name);
			return this;
		}
		
		private static Tag<Block> createBlockTag(String path) {
            return new BlockTags.Wrapper(new ResourceLocation("forge", path));
        }

        private static Tag<Item> createItemTag(String path) {
            return new ItemTags.Wrapper(new ResourceLocation("forge", path));
        }

        private static Tag<Item> createItemTag(ResourceLocation tag) {
            return new ItemTags.Wrapper(tag);
        }
	}
	
}
