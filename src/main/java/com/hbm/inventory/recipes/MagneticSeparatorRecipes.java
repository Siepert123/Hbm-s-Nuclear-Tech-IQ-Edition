package com.hbm.inventory.recipes;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ModItems;

import net.minecraft.item.ItemStack;

public class MagneticSeparatorRecipes extends GenericRecipes<GenericRecipe> {

	public static final MagneticSeparatorRecipes INSTANCE = new MagneticSeparatorRecipes();

	@Override public int inputItemLimit() { return 2; }
	@Override public int inputFluidLimit() { return 1; }
	@Override public int outputItemLimit() { return 6; }
	@Override public int outputFluidLimit() { return 1; }

	@Override public String getFileName() { return "hbmMagneticSeparator.json"; }
	@Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

	@Override
	public void registerDefaults() {

		this.register(new GenericRecipe("magsep.iron").setup(200, 1_000)
				.inputItems(new ComparableStack(ModItems.powder_iron))
				.inputFluids(new FluidStack(Fluids.WATER, 250))
				.outputItems(new ItemStack(ModItems.powder_iron), new ItemStack(ModItems.dust))
				.outputFluids(new FluidStack(Fluids.SPENTSTEAM, 250))
				.setIconToFirstIngredient());
	}
}
