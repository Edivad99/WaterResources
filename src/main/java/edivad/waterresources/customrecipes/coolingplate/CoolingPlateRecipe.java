package edivad.waterresources.customrecipes.coolingplate;

import net.minecraftforge.fluids.FluidStack;

public class CoolingPlateRecipe {

	private final FluidStack input;

	public CoolingPlateRecipe(FluidStack input)
	{
		this.input = input;
	}

	public FluidStack getInput()
	{
		return input;
	}
}
