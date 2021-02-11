package mrghastien.quantum_machinery.api.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import mrghastien.quantum_machinery.client.screens.MachineScreen;
import mrghastien.quantum_machinery.common.blocks.BaseTile;
import mrghastien.quantum_machinery.common.blocks.generators.GeneratorTile;
import mrghastien.quantum_machinery.common.blocks.machines.MachineTile;
import mrghastien.quantum_machinery.common.capabilities.energy.MachineEnergyStorage;
import mrghastien.quantum_machinery.util.MathHelper;
import mrghastien.quantum_machinery.util.RenderingUtils;
import mrghastien.quantum_machinery.util.Units;
import mrghastien.quantum_machinery.util.RenderingUtils.GradientMode;

public class EnergyBar {

	private final Supplier<MachineEnergyStorage> storage;
	public int index;
	public final int xPos;
	public final int yPos;
	public final int width;
	public final int height;
	public final Type type;
	
	public EnergyBar(Supplier<MachineEnergyStorage> storage, int xPos, int yPos, int width, int height) {
		this(storage, xPos, yPos, width, height, Type.STORAGE);
	}
	
	public EnergyBar(Supplier<MachineEnergyStorage> storage, int xPos, int yPos, int width, int height, Type type) {
		this.storage = storage;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.type = type;
	}
	
	public List<ITextComponent> getTooltip(BaseTile tile) {
		List<ITextComponent> tooltips = new ArrayList<>();
		MachineEnergyStorage energy = getStorage();
		int transfer = energy.getInput() - energy.getOutput();
		tooltips.add(new StringTextComponent("Energy: " + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored() + Units.ENERGY));
		switch (type) {
			case STORAGE:
				tooltips.add(new StringTextComponent((energy.getInput() > 0 ? TextFormatting.GREEN : TextFormatting.DARK_GRAY) + "Input : "
						+ energy.getInput() + Units.ENERGY + "/t"));
				tooltips.add(new StringTextComponent((energy.getOutput() > 0 ? TextFormatting.RED : TextFormatting.DARK_GRAY) + "Output : "
						+ energy.getOutput() + Units.ENERGY + "/t"));
				tooltips.add(new StringTextComponent((transfer > 0 ? TextFormatting.GREEN
						: transfer < 0 ? TextFormatting.RED : TextFormatting.DARK_GRAY) + "Transfer : "
						+ (energy.getInput() - energy.getOutput()) + Units.ENERGY + "/t"));
				break;

			case GENERATOR:
				if (tile instanceof GeneratorTile) {
					GeneratorTile gen = (GeneratorTile) tile;
					TextFormatting color = gen.isBurning() ? TextFormatting.GREEN : TextFormatting.DARK_GRAY;
					tooltips.add(new StringTextComponent(color + "Production : " + gen.getCurrentEnergyProduction() + Units.ENERGY + "/t"));
					tooltips.add(new StringTextComponent((energy.getOutput() > 0 ? TextFormatting.RED : TextFormatting.DARK_GRAY) + "Output : "
							+ energy.getOutput() + Units.ENERGY + "/t"));
				}

			case CONSUMER:
				if (tile instanceof MachineTile<?>) {
					MachineTile<?> machine = (MachineTile<?>) tile;
					TextFormatting color = machine.isActive() ? TextFormatting.RED : TextFormatting.DARK_GRAY;
					tooltips.add(new StringTextComponent((energy.getInput() > 0 ? TextFormatting.GREEN : TextFormatting.DARK_GRAY) + "Input : "
							+ energy.getInput() + Units.ENERGY + "/t"));
					tooltips.add(new StringTextComponent(color + "Consumption : " + machine.getCurrentEnergyUsage() + Units.ENERGY + "/t"));
				}
				break;
		}
		return tooltips;
	}
	
	public void render(MachineScreen<?, ?> screen) {
		int guiLeft = screen.getGuiLeft();
		int guiTop = screen.getGuiTop();
		int blitOffset = screen.getBlitOffset();
		Color central = new Color(206, 0, 0);
		Color edge = new Color(149, 0, 0);
		Color darkCentral = new Color(129, 0, 0);
		Color darkEdge = new Color(106, 0, 0);
		int energy = MathHelper.scale(getStorage().getEnergyStored(), getStorage().getMaxEnergyStored(), height);
		//On commence tout en bas de la barre
		int yPos1 = guiTop + yPos + height - 1;
		boolean dark = false;
		int count = 0;
		while (yPos1 >= guiTop + yPos + height - energy) {
			if (count > 1) {
				count = 0;
				if (dark) dark = false;
				else dark = true;
			}
			//Pour remédier au problème d'écart quand la largeur est impaire
			int oddWidth = width % 2 != 0 ? width / 2 + 1 : width / 2;
			//On fait deux dégradés : un de chaque côté
			if (dark) {
				RenderingUtils.fillGradient(guiLeft + xPos, yPos1, width / 2, 1, blitOffset, darkCentral, darkEdge,
						GradientMode.HORIZONTAL);
				RenderingUtils.fillGradient(guiLeft + xPos + width / 2, yPos1, oddWidth, 1, blitOffset, darkEdge,
						darkCentral, GradientMode.HORIZONTAL);
			} else {
				RenderingUtils.fillGradient(guiLeft + xPos, yPos1, width / 2, 1, blitOffset, central, edge,
						GradientMode.HORIZONTAL);
				RenderingUtils.fillGradient(guiLeft + xPos + width / 2, yPos1, oddWidth, 1, blitOffset, edge, central,
						GradientMode.HORIZONTAL);
			}
			//On remonte de 1 à chaque itération
			yPos1--;
			count++;
		}
	}
	
	public MachineEnergyStorage getStorage() {
		return storage.get();
	}
	
	public enum Type {
		STORAGE, GENERATOR, CONSUMER;
	}
}
