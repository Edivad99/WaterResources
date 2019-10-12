package edivad.waterresources.customrecipes.extractor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ExtractorRecipe {

	private final FluidStack inputFluid, outputFluid;
	private final ItemStack input;

	public ExtractorRecipe(ItemStack input, FluidStack inputFluid, FluidStack outputFluid)
	{
		this.input = input;
		this.inputFluid = inputFluid;
		this.outputFluid = outputFluid;
	}

	public ItemStack getInput()
	{
		return input;
	}

	public FluidStack getInputFluid()
	{
		return inputFluid;
	}

	public FluidStack getOutputFluid()
	{
		return outputFluid;
	}

}
