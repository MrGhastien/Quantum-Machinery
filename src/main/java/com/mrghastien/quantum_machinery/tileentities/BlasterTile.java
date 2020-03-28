package com.mrghastien.quantum_machinery.tileentities;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.capabilities.temperature.CapabilityTemp;
import com.mrghastien.quantum_machinery.capabilities.temperature.ITempHandler;
import com.mrghastien.quantum_machinery.capabilities.temperature.TempHandler;
import com.mrghastien.quantum_machinery.containers.BlasterContainer;
import com.mrghastien.quantum_machinery.init.ModTileEntities;
import com.mrghastien.quantum_machinery.items.BatteryStackHandler;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BlasterTile extends MachineTile {

	private LazyOptional<ItemStackHandler> handler = LazyOptional.of(this::createHandler);
	private LazyOptional<BatteryStackHandler> batteryHandler = LazyOptional.of(this::createBatteryHandler);
	private LazyOptional<ITempHandler> temp = LazyOptional.of(this::createTemp);

	private int soundCounter;
	private final int MAX_PROD = 500;
	private final double MAX_TEMP = 1000.0d;
	private boolean isProducing;

	public BlasterTile() {
		super(ModTileEntities.BLASTER_TILE.get(), "container.blaster.name", 50000, 0, 1024);
		this.maxTimer = 120;
	}

	public ItemStack getStack(int index) {
		return this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(h -> h.getStackInSlot(index))
				.orElse(ItemStack.EMPTY);
	}
	
	public ItemStack getBatteryStack(int index) {
		return this.batteryHandler.map(h -> h.getStackInSlot(index))
				.orElse(ItemStack.EMPTY);
	}
	
	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}

		if (soundCounter > 0) {
			soundCounter--;
			markDirty();
		}
		if (soundCounter <= 0 && world.getBlockState(pos).get(BlockStateProperties.POWERED)) {
			this.getWorld().playSound(null, pos, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS,
					0.5f, 0.2f);
			soundCounter = 20;
		}

		if (getProd() != 0) {
			int t = (int) (((float) MAX_PROD / (float) getProd()) * 120f);
			maxTimer = t;
		} else {
			this.isProducing = false;
		}

		if (workTimer == 0 || workTimer >= maxTimer) {
			handler.ifPresent(h -> {
				energy.ifPresent(e -> {
					if (canProduce()) {
						h.extractItem(0, 1, false);
						this.isProducing = true;
						if (workTimer >= maxTimer) {
							workTimer = 0;
						} else if (workTimer == 0) {
							workTimer++;
						}
						// ((ModEnergyStorage) e).generateEnergy(getProd());
						markDirty();
					} else {
						this.isProducing = false;
					}
				});
			});
		}

		if (workTimer < maxTimer && canProduce() && getProd() > 0) {
			workTimer++;
			temp.ifPresent(t -> {
				((TempHandler) t).heat(getProd(), false);
			});
			energy.ifPresent(e -> ((ModEnergyStorage) e).generateEnergy(getProd(), false));
			markDirty();
		} else {
			isProducing = false;
		}

		temp.ifPresent(t -> {
			if (!world.getBlockState(this.getPos()).get(BlockStateProperties.POWERED)) {
				((TempHandler) t).cool(0, false);
			}
		});

		BlockState blockState = world.getBlockState(pos);
		if (blockState.get(BlockStateProperties.POWERED) != isProducing) {
			world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, isProducing), 3);
		}
		batteryHandler.ifPresent(h -> h.fillBatteries(energy));
		sendOutPower(energy);
	}

	private boolean canProduce() {
		return !energy.map(e -> ((ModEnergyStorage) e).isFull()).orElse(true)
				&& (handler.map(h -> h.getStackInSlot(0)).orElse(ItemStack.EMPTY).getItem() == Items.COAL)
				|| workTimer > 0;
	}

	private ITempHandler createTemp() {
		return new TempHandler(MAX_TEMP);
	}

	private int getProd() {
		return Math.min(energy.map(e -> ((ModEnergyStorage) e).getAvailableSpace()).orElse(0), MAX_PROD);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound) {
		CompoundNBT invTag = compound.getCompound("inv");
		handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
		CompoundNBT batteryTag = compound.getCompound("batteries");
		batteryHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(batteryTag));
		CompoundNBT heatTag = compound.getCompound("heat");
		temp.ifPresent(t -> ((INBTSerializable<CompoundNBT>) t).deserializeNBT(heatTag));
		CompoundNBT energyTag = compound.getCompound("energy");
		energy.ifPresent(e -> ((INBTSerializable<CompoundNBT>) e).deserializeNBT(energyTag));
		this.workTimer = compound.getInt("counter");
		this.isProducing = compound.getBoolean("producing");
		if (compound.contains("CustomName", 8)) {
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
		}
		super.read(compound);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		handler.ifPresent(h -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("inv", tag);
		});
		batteryHandler.ifPresent(h -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("batteries", tag);
		});
		temp.ifPresent(t -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) t).serializeNBT();
			compound.put("heat", tag);
		});
		energy.ifPresent(e -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) e).serializeNBT();
			compound.put("energy", tag);
		});
		compound.putInt("counter", this.workTimer);
		compound.putBoolean("producing", isProducing);
		ITextComponent itextcomponent = this.getCustomName();
		if (itextcomponent != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
		}
		return compound;
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(1) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if (slot == 0) {
					return stack.getItem() == Items.COAL;
				}
				return false;
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if (slot == 0) {
					if (stack.getItem() != Items.COAL)
						return stack;
				}

				return super.insertItem(slot, stack, simulate);
			}

			@Override
			protected void onContentsChanged(int slot) {
				BlasterTile.this.markDirty();
			}

		};
	}

	private BatteryStackHandler createBatteryHandler() {
		return new BatteryStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				BlasterTile.this.markDirty();
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == null) {
				return LazyOptional.of(() -> (T) new CombinedInvWrapper(handler.map(e -> e).orElse(null), batteryHandler.map(e -> e).orElse(null)));
			} else if(side == Direction.UP) {
				return handler.cast();
			} else if(side == Direction.SOUTH) {
				return batteryHandler.cast();
			}
			return handler.cast();
		}
		if (cap == CapabilityTemp.HEAT) {
			return temp.cast();
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerinventory, PlayerEntity player) {
		return new BlasterContainer(id, world, pos, playerinventory, player);
	}

	public LazyOptional<ItemStackHandler> getHandler() {
		return handler;
	}

	public double getMaxTemp() {
		return this.MAX_TEMP;
	}

	public int getCounter() {
		return workTimer;
	}

	public void setCounter(int counter) {
		this.workTimer = counter;
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false
				: player.getDistanceSq((double) this.pos.getX() + 0.5d, (double) this.pos.getY() + 0.5d,
						(double) this.pos.getZ() + 0.5d) <= 64d;
	}
}
