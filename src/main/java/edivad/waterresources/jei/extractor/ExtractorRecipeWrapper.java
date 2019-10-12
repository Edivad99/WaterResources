package edivad.waterresources.jei.extractor;

import java.util.Collections;
import java.util.List;

import edivad.waterresources.ModBlocks;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.config.ExtractorConfig;
import edivad.waterresources.customrecipes.extractor.ExtractorRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ExtractorRecipeWrapper implements ICraftingRecipeWrapper {

	private final List<List<ItemStack>> inputs;
	private final List<List<FluidStack>> inputFluid;
	private final FluidStack outputFluid;

	public ExtractorRecipeWrapper(ExtractorRecipe recipe)
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
		String energyRequired = (PoweredTileEntity.RF_PER_TICK * ExtractorConfig.MAX_PROGRESS) + " FE";
		minecraft.fontRenderer.drawString(energyRequired, 60, 60, 4210752);

		if(ItemStack.areItemsEqual(getInputSlot(), new ItemStack(ModBlocks.GOLDFIELD_ORE)))
		{
			minecraft.fontRenderer.drawString(ExtractorConfig.BASE_PERCENT_OF_PROCESSING_WITH_GOLDFIELD_ORE + "%", 93, 23, 4210752);
		}
		else if(ItemStack.areItemsEqual(getInputSlot(), new ItemStack(Blocks.DIRT)))
		{
			minecraft.fontRenderer.drawString(ExtractorConfig.BASE_PERCENT_OF_PROCESSING + "%", 93, 23, 4210752);
		}

	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}

	private ItemStack getInputSlot()
	{
		return inputs.get(0).get(0);
	}
}
