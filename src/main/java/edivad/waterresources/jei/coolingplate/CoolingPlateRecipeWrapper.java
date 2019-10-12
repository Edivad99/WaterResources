package edivad.waterresources.jei.coolingplate;

import java.util.Collections;
import java.util.List;

import edivad.waterresources.customrecipes.coolingplate.CoolingPlateRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class CoolingPlateRecipeWrapper implements ICraftingRecipeWrapper {

	private final FluidStack input;

	public CoolingPlateRecipeWrapper(CoolingPlateRecipe recipe)
	{
		this.input = recipe.getInput().copy();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(VanillaTypes.FLUID, input);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{

	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}
}