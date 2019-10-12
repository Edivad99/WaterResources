package edivad.waterresources.jei.industrialdistillator;

import java.util.Collections;
import java.util.List;

import edivad.waterresources.ModLiquids;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.config.IndustrialDistillatorConfig;
import edivad.waterresources.customrecipes.industrialdistillator.IndustrialDistillatorRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

public class IndustrialDistillatorRecipeWrapper implements ICraftingRecipeWrapper {

	private final List<List<FluidStack>> inputs;

	public IndustrialDistillatorRecipeWrapper(IndustrialDistillatorRecipe recipe)
	{
		this.inputs = Collections.singletonList(Collections.singletonList(recipe.getInputFluid().copy()));
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.FLUID, inputs);
		ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(ModLiquids.distilled_water, IndustrialDistillatorConfig.WATER_CONSUMED));
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		String energyRequired = (PoweredTileEntity.RF_PER_TICK * IndustrialDistillatorConfig.MAX_PROGRESS) + " FE";
		minecraft.fontRenderer.drawString(energyRequired, 60, 60, 4210752);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}
}