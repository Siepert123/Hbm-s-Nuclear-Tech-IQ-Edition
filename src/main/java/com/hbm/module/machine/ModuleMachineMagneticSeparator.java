package com.hbm.module.machine;

import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.recipes.MagneticSeparatorRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipes;

import api.hbm.energymk2.IEnergyHandlerMK2;
import net.minecraft.item.ItemStack;

public class ModuleMachineMagneticSeparator extends ModuleMachineBase {

	public ModuleMachineMagneticSeparator(int index, IEnergyHandlerMK2 battery, ItemStack[] slots) {
		super(index, battery, slots);
		this.inputSlots = new int[1];
		this.outputSlots = new int[6];
		this.inputTanks = new FluidTank[1];
		this.outputTanks = new FluidTank[1];
	}

	@Override
	public GenericRecipes getRecipeSet() {
		return MagneticSeparatorRecipes.INSTANCE;
	}

	public ModuleMachineMagneticSeparator itemInput(int start) { for(int i = 0; i < inputSlots.length; i++) inputSlots[i] = start + i; return this; }
	public ModuleMachineMagneticSeparator itemOutput(int start) { for(int i = 0; i < outputSlots.length; i++) outputSlots[i] = start + i; return this; }
	public ModuleMachineMagneticSeparator fluidInput(FluidTank a) { inputTanks[0] = a; return this; }
	public ModuleMachineMagneticSeparator fluidOutput(FluidTank a) { outputTanks[0] = a; return this; }
}
