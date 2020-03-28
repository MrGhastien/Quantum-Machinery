package com.mrghastien.quantum_machinery.tileentities;

import static com.mrghastien.quantum_machinery.init.ModTileEntities.REFINERY_TILE;

import javax.annotation.Nullable;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.containers.RefineryContainer;
import com.mrghastien.quantum_machinery.init.ModRecipes;
import com.mrghastien.quantum_machinery.recipes.refinery.RefineryRecipe;
import com.mrghastien.quantum_machinery.util.helpers.ItemHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RefineryTile extends MachineTile {

	private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createItemHandler);
	private int soundCounter;
	private int toConsume = 150;
	private boolean isRefining = false;
	private ItemStack smelting = ItemStack.EMPTY;

	public RefineryTile() {
		super(REFINERY_TILE.get(), "container.refinery", 10000, 1000, 0);
		this.maxTimer = 120;
	}

	private IItemHandler createItemHandler() {
		return new ItemStackHandler(3) {
			@Override
			protected void onContentsChanged(int slot) {
				RefineryTile.this.markDirty();
			}

			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if (slot == 2) {
					return stack.getItem() == smelting.getItem();
				} else {
					return true;
				}
			}
			
		};
	}

	@Override
	public void tick() {
		if (!world.isRemote) {

			if (this.hasEnergy()) {
				if (workTimer > 0) {
					if (soundCounter >= 20) {
						this.getWorld().playSound(null, pos, SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.BLOCKS,
								0.1f, 0.2f);
						soundCounter = 0;
					}
					if (soundCounter < 20)
						soundCounter++;
					if (this.getStack(2).getItem() == smelting.getItem() || this.getStack(2).isEmpty()) {
						workTimer++;
					}
					energy.ifPresent(e -> {
						((ModEnergyStorage) e).extractEnergy(toConsume);
					});
					if (workTimer >= maxTimer) {
						if (!this.getStack(2).isEmpty()) {
							ItemStack outputItem = new ItemStack(this.getStack(2).getItem(), smelting.getCount());
							handler.ifPresent(h -> h.insertItem(2, outputItem.copy(), false));
						} else {
							handler.ifPresent(h -> h.insertItem(2, smelting.copy(), false));
						}

						smelting = ItemStack.EMPTY;
						workTimer = 0;
						maxTimer = 0;
						toConsume = 0;
						return;
					}
				} else {
					IRecipe<?> rRecipe = this.world.getRecipeManager().getRecipe(ModRecipes.REFINING_TYPE, 
							new Inventory(this.getStack(0), this.getStack(1), this.getStack(2)), this.world).orElse(null);
					if (rRecipe != null && rRecipe instanceof RefineryRecipe && !getStack(0).isEmpty()
							&& !getStack(1).isEmpty()) {
						final ItemStack output = rRecipe.getRecipeOutput();

						if (!output.isEmpty() && this.getStack(2).getCount() < this.getStack(2).getMaxStackSize()) {
							handler.ifPresent(h -> {
								if (this.canSmelt(rRecipe)) {
									smelting = output;
									boolean stack1 = false;
									boolean stack2 = false;
									//First input of the recipe
									for (ItemStack itemStack : rRecipe.getIngredients().get(0).getMatchingStacks()) {
										if (ItemHelper.compareStackWithRecipe(rRecipe, this.getStack(0), 1) || ItemHelper.compareStackWithRecipe(rRecipe, this.getStack(1), 1)) {

											if (ItemHelper.compareItemStacks(this.getStack(0), itemStack)) {
												h.extractItem(0, itemStack.getCount(), false);
												stack1 = true;
												break;
											} else if (ItemHelper.compareItemStacks(this.getStack(1), itemStack)) {
												h.extractItem(1, itemStack.getCount(), false);
												stack1 = true;
												break;
											}
										}
									}
									//Second input of the recipe
									for (ItemStack itemStack : rRecipe.getIngredients().get(1).getMatchingStacks()) {
										if (ItemHelper.compareStackWithRecipe(rRecipe, this.getStack(0), 0) || ItemHelper.compareStackWithRecipe(rRecipe, this.getStack(1), 0)) {
											if (ItemHelper.compareItemStacks(this.getStack(1), itemStack)) {
												h.extractItem(1, itemStack.getCount(), false);
												stack2 = true;
												break;
											} else if (ItemHelper.compareItemStacks(this.getStack(0), itemStack)) {
												h.extractItem(0, itemStack.getCount(), false);
												stack2 = true;
												break;
											}
										}
									}
									if (stack1 && stack2) {
										maxTimer = ((RefineryRecipe) rRecipe).getTicks();
										toConsume = ((RefineryRecipe) rRecipe).getEnergyUsage();
										workTimer++;
										isRefining = true;
									}
								} else {
									this.isRefining = false;
								}
							});
							energy.ifPresent(e -> {
								((ModEnergyStorage) e).extractEnergy(toConsume);
							});
						} 
					} else
						isRefining = false;
				}
			} else isRefining = false;
			BlockState blockState = world.getBlockState(pos);
			if (blockState.get(BlockStateProperties.POWERED) != this.isRefining) {
				world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, this.isRefining), 3);
			}
		}
	}

	protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
		if (recipeIn != null && (!this.getStack(0).isEmpty() || !this.getStack(1).isEmpty())) {
			ItemStack itemstack = recipeIn.getRecipeOutput();
			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = this.getStack(2);
					

					if (itemstack1.isEmpty()) {
						return true;
					} else if (!itemstack1.isItemEqual(itemstack)) {
						return false;
					} else if (itemstack1.getCount() + itemstack.getCount() <= 64
							&& itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
						return true;
					} else {
						return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); 
					}
				}
		} else {
			return false;
		}
	}

	public int getCounter() {
		return this.workTimer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CompoundNBT invTag = compound.getCompound("inv");
		handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
		CompoundNBT energyTag = compound.getCompound("energy");
		energy.ifPresent(e -> ((INBTSerializable<CompoundNBT>) e).deserializeNBT(energyTag));
		if (compound.contains("CustomName", 8)) {
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		handler.ifPresent(h -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("inv", tag);
		});
		energy.ifPresent(e -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) e).serializeNBT();
			compound.put("energy", tag);
		});
		ITextComponent itextcomponent = this.getCustomName();
		if (itextcomponent != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
		}
		return compound;
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new RefineryContainer(id, world, pos, playerInventory, player);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}

	public boolean hasEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0) > 0;
	}

	public LazyOptional<IItemHandler> getHandler() {
		return this.handler;
	}

	public int getEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> ((ModEnergyStorage) e).getEnergyStored()).orElse(0);
	}

	public ItemStack getStack(int index) {
		return this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(h -> h.getStackInSlot(index))
				.orElse(ItemStack.EMPTY);
	}

	public void setCounter(int value) {
		this.workTimer = value;
	}

	public int getMaxCounter() {
		return maxTimer;
	}

	public void setMaxCounter(int maxCounter) {
		this.maxTimer = maxCounter;
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false
				: player.getDistanceSq((double) this.pos.getX() + 0.5d, (double) this.pos.getY() + 0.5d,
						(double) this.pos.getZ() + 0.5d) <= 64d;
	}
}
