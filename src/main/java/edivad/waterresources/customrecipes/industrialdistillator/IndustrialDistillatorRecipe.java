package edivad.waterresources.customrecipes.industrialdistillator;

import net.minecraftforge.fluids.FluidStack;

public class IndustrialDistillatorRecipe {

	private final FluidStack input;

	public IndustrialDistillatorRecipe(FluidStack input)
	{
		this.input = input;
	}

	public FluidStack getInputFluid()
	{
		return input;
	}
}
