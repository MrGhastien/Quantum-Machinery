package mrghastien.quantum_machinery.client.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.blocks.machines.furnace.ElectricFurnaceContainer;
import mrghastien.quantum_machinery.common.blocks.machines.furnace.ElectricFurnaceTile;
import mrghastien.quantum_machinery.util.MathHelper;

@OnlyIn(Dist.CLIENT)
public class ElectricFurnaceScreen extends MachineScreen<ElectricFurnaceContainer, ElectricFurnaceTile> {
	
	private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/electric_furnace_gui.png");
	
	public ElectricFurnaceScreen(ElectricFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, ElectricFurnaceTile tileEntity) {
		super(screenContainer, inv, titleIn, tileEntity);
		this.ySize = 176;
	}


	@Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        //this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
    	ITextComponent s = tileEntity.getDisplayName();
    	this.font.drawString(matrixStack, s.getString(), this.xSize / 2 - this.font.getStringWidth(s.getString()) / 2, 6.0F, 4210752);
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0F, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    	RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
        
        if (mouseX > guiLeft + 16 && mouseX < guiLeft + 32 && mouseY > guiTop + 40 && mouseY < guiTop + 56) {
        	this.blit(matrixStack, guiLeft + 16, guiTop + 40, 179, 25, 16, 16);
        } else {
        	this.blit(matrixStack, guiLeft + 16, guiTop + 40, 195, 25, 16, 16);
        }
        
        int c = MathHelper.scale(tileEntity.getProgress(), tileEntity.getCurrentProcessingTime(), 26);
        this.blit(matrixStack, this.guiLeft + 86, this.guiTop + 38, 179, 0, 0 + c, 17);
        
        int e = this.getEnergyScaled(61);
        this.blit(matrixStack, this.guiLeft + 43, this.guiTop + 17 + 61 - e, 176, 0, 3, 0 + e);
    }

}
