package mrghastien.quantum_machinery.common.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mrghastien.quantum_machinery.api.client.EnergyBar;
import mrghastien.quantum_machinery.common.network.GuiSyncPacket;
import mrghastien.quantum_machinery.common.network.ModNetworking;
import mrghastien.quantum_machinery.common.network.SyncedField;
import mrghastien.quantum_machinery.util.ClassUtils;

public abstract class BaseContainer<T extends BaseTile> extends Container {

	public final T tileEntity;
	protected IItemHandler playerInventory;
	protected BlockPos pos;
	protected World world;
	protected int size;
	private final List<SyncedField<?>> syncedFields = new ArrayList<>();
	public final List<EnergyBar> energyBars = new ArrayList<>();
	
	protected BaseContainer(ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerinventory, T tileEntity, int size) {
		super(type, id);
		this.tileEntity = tileEntity;
		this.playerInventory = new InvWrapper(playerinventory);
		this.pos = pos;
		this.world = world;
		this.size = size;
		syncedFields.addAll(ClassUtils.getSyncableFields(tileEntity));
		for (int i = 0; i < syncedFields.size(); i++) {
			sendToListeners(new GuiSyncPacket(syncedFields.get(i), i));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateField(Object value, int id) {
		SyncedField<Object> field = (SyncedField<Object>) syncedFields.get(id);
		field.setValue(value);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < syncedFields.size(); i++) {
			if(syncedFields.get(i).updated()) {
				sendToListeners(new GuiSyncPacket(syncedFields.get(i), i));
			}
		}
		//tileEntity.getEnergyStorage().update();
	}
	
	@SuppressWarnings("unchecked")
	private List<IContainerListener> getListeners() throws Exception {
		Field f = null;
		try {
			f = Container.class.getDeclaredField("listeners");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		f.setAccessible(true);
		List<IContainerListener> listeners = null;
		try {
			listeners = (List<IContainerListener>) f.get(this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception();
		}
		return listeners;
	}
	
	private void sendToListeners(Object message) {
		List<IContainerListener> listeners = null;
		try {
			listeners = getListeners();
			if (listeners != null) {
				for (IContainerListener listener : listeners) {
					if (listener instanceof ServerPlayerEntity)
						ModNetworking.sendToPlayer(message, (ServerPlayerEntity) listener);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EnergyBar addEnergyBar(EnergyBar bar) {
		bar.index = energyBars.size();
		energyBars.add(bar);
		return bar;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return tileEntity.isUsableByPlayer(playerIn);
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
	}
	
	protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
        	addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }
	
	protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
		for (int j = 0 ; j < verAmount ; j++) {
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
		// Player inventory
		addSlotBox(playerInventory, 9, leftCol, topRow, 9,  18, 3, 18);

		// Hotbar
		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}
    
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < this.size) {
				if (!this.mergeItemStack(itemstack1, this.size, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, this.size, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
	    
	public BlockPos getPos() {
		return pos;
	}
}
