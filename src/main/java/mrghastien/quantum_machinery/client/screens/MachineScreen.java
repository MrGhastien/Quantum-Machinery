package mrghastien.quantum_machinery.client.screens;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.matrix.MatrixStack;

import mrghastien.quantum_machinery.api.client.EnergyBar;
import mrghastien.quantum_machinery.common.blocks.BaseContainer;
import mrghastien.quantum_machinery.common.blocks.BaseTile;
import mrghastien.quantum_machinery.util.MathHelper;

@OnlyIn(Dist.CLIENT)
public abstract class MachineScreen<T extends BaseContainer<U>, U extends BaseTile> extends ContainerScreen<T> {

	protected U tileEntity;

	public MachineScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, U tileEntity) {
		super(screenContainer, inv, titleIn);
		this.tileEntity = tileEntity;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		for (int i = 0; i < container.energyBars.size(); i++) {
			EnergyBar bar = container.energyBars.get(i);
			bar.render(this);
			if(isPointInRegion(bar.xPos, bar.yPos, bar.width, bar.height, mouseX, mouseY))
				func_243308_b(matrixStack, bar.getTooltip(tileEntity), mouseX, mouseY); //Not mapped yet (renderTooltip())
		}
	}

	protected int getEnergyScaled(int pixels) {
    	int i = getEnergyStored();
    	int c = getMaxEnergyStored();
    	return MathHelper.scale(i, c, pixels);
    }
	
	protected int getEnergyStored() {
		return tileEntity.getEnergyStorage().getEnergyStored();
	}
	
	protected int getMaxEnergyStored() {
		return tileEntity.getEnergyStorage().getMaxEnergyStored();
	}
}
