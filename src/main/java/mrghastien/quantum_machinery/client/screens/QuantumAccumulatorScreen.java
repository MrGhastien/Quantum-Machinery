package mrghastien.quantum_machinery.client.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.blocks.machines.accumulator.QuantumAccumulatorContainer;
import mrghastien.quantum_machinery.common.blocks.machines.accumulator.QuantumAccumulatorTile;

@OnlyIn(Dist.CLIENT)
public class QuantumAccumulatorScreen extends MachineScreen<QuantumAccumulatorContainer, QuantumAccumulatorTile> {

	private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/quantum_accumulator_gui.png");

	public QuantumAccumulatorScreen(QuantumAccumulatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, QuantumAccumulatorTile tileEntity) {
		super(screenContainer, inv, titleIn, tileEntity);
		this.ySize = 232;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		//this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		ITextComponent tileName = tileEntity.getDisplayName();
		this.font.drawString(matrixStack, tileName.getString(), (this.xSize / 2 - this.font.getStringWidth(tileName.getString()) / 2) - 5, 6, 4210752);
		this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0F, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI);
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		int e = this.getEnergyScaled(114);
		this.blit(matrixStack, this.guiLeft + 80, this.guiTop + 18, 176, 0, 16, 114 - e);
	}
}
