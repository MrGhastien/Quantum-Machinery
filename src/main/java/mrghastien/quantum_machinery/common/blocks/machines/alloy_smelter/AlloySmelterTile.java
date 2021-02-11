package mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static mrghastien.quantum_machinery.common.init.ModTileEntities.ALLOY_SMELTER;

import mrghastien.quantum_machinery.api.common.crafting.ItemStackIngredient;
import mrghastien.quantum_machinery.api.common.crafting.ModRecipeType;
import mrghastien.quantum_machinery.common.blocks.machines.MachineTile;
import mrghastien.quantum_machinery.common.capabilities.items.ModItemStackHandler;
import mrghastien.quantum_machinery.common.recipes.AlloySmeltingRecipe;
import mrghastien.quantum_machinery.util.ItemHelper;

public class AlloySmelterTile extends MachineTile<AlloySmeltingRecipe> {

	private ModItemStackHandler inputHandler = new ModItemStackHandler(this, 4) {
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.isEmpty() || ModRecipeType.ALLOY_SMELTING.stream(world).anyMatch(r -> r.getInputs().stream().anyMatch(ing -> ing.test(stack)));
		};
	};
	private ModItemStackHandler outputHandler = new ModItemStackHandler(this);
	private CombinedInvWrapper invWrapper = new CombinedInvWrapper(inputHandler, outputHandler);
	
	public AlloySmelterTile() {
		super(ALLOY_SMELTER.get(), 10000, 1000);
	}

	public int getCounter() {
		return this.progress;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		inputHandler.deserializeNBT(compound.getCompound("Input"));
		outputHandler.deserializeNBT(compound.getCompound("Output"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Input", inputHandler.serializeNBT());
		compound.put("Output", outputHandler.serializeNBT());
		return compound;
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new AlloySmelterContainer(id, world, pos, playerInventory);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == Direction.UP)
				return inputHandler.getLazy().cast();
			if(side == Direction.DOWN)
				return outputHandler.getLazy().cast();
			
			return LazyOptional.of(() -> invWrapper).cast();
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return energy.getLazy().cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	protected int getEnergyUsage() {
		return 64;
	}

	@Override
	protected ModItemStackHandler getInputInventory() {
		return inputHandler;
	}

	@Override
	protected ModItemStackHandler getOutputInventory() {
		return outputHandler;
	} 

	@Override
	protected Collection<AlloySmeltingRecipe> getRecipes() {
		return ModRecipeType.ALLOY_SMELTING.getRecipes(world).values();
	}
	
	@Override
	protected AlloySmeltingRecipe getApplicableRecipe() {
		IItemHandler inv = getInputInventory();
		for(AlloySmeltingRecipe recipe : getRecipes()) {
			if(recipe.matches(ItemHelper.getStacksFromHandler(inv).toArray(new ItemStack[0]))) {
				return recipe;
			}
		}
		return null;
	}

	@Override
	protected int getProcessingTime() {
		return currentRecipe.getTicks();
	}

	@Override
	protected Collection<ItemStack> getResults() {
		return Collections.singletonList(currentRecipe.getResult());
	}

	@Override
	protected Collection<Ingredient> getIngredients() {
		Collection<Ingredient> ing = new ArrayList<>();
		for (ItemStackIngredient ingredient : currentRecipe.getInputs()) {
			ing.add(ingredient);
		}
		return ing;
	}
}
