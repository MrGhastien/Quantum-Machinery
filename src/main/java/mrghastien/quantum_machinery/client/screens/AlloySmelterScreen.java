package mrghastien.quantum_machinery.client.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterContainer;
import mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterTile;
import mrghastien.quantum_machinery.util.MathHelper;

@OnlyIn(Dist.CLIENT)
public class AlloySmelterScreen extends MachineScreen<AlloySmelterContainer, AlloySmelterTile>{

	private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/alloy_smelter_gui.png");

	public AlloySmelterScreen(AlloySmelterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, AlloySmelterTile tileEntity) {
		super(screenContainer, inv, titleIn, tileEntity);
		this.ySize = 184;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		//this.renderToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		ITextComponent s = tileEntity.getDisplayName();
		this.font.drawString(matrixStack, s.getString(),
				this.xSize / 2 - this.font.getStringWidth(s.getString()) / 2, 6.0F, 4210752);
		this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0F,
				this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

		int c = MathHelper.scale(tileEntity.getProgress(), tileEntity.getCurrentProcessingTime(), 14);
		this.blit(matrixStack, this.guiLeft + 124, this.guiTop + 82 - c, 176, 13 - c, 14, c + 1);
	}

}
