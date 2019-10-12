package edivad.waterresources.jei.watercooling;

import java.util.Collections;
import java.util.List;

import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.config.WaterCoolingConfig;
import edivad.waterresources.customrecipes.watercooling.WaterCoolingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class WaterCoolingRecipeWrapper implements ICraftingRecipeWrapper {

	private final List<List<ItemStack>> inputs;
	private final List<List<FluidStack>> inputFluid;
	private final FluidStack outputFluid;

	public WaterCoolingRecipeWrapper(WaterCoolingRecipe recipe)
	{
		this.inputs = Collections.singletonList(Collections.singletonList(recipe.getInput().copy()));
		this.inputFluid = Collections.singletonList(Collections.singletonList(recipe.getInputFluid().copy()));
		this.outputFluid = recipe.getOutputFluid().copy();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInputLists(VanillaTypes.FLUID, inputFluid);
		ingredients.setOutput(VanillaTypes.FLUID, outputFluid);

	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		String energyRequired = (PoweredTileEntity.RF_PER_TICK * WaterCoolingConfig.MAX_PROGRESS) + " FE";
		minecraft.fontRenderer.drawString(energyRequired, 60, 60, 4210752);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}
}
