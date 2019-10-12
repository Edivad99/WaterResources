package edivad.waterresources.customrecipes.washingmachine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class WashingMachineRecipe {

	private final FluidStack input1, input2;
	private final ItemStack output;

	public WashingMachineRecipe(FluidStack input1, FluidStack input2, ItemStack output)
	{
		this.input1 = input1;
		this.input2 = input2;
		this.output = output;
	}

	public FluidStack getInput1()
	{
		return input1;
	}

	public FluidStack getInput2()
	{
		return input2;
	}

	public ItemStack getOutput()
	{
		return output;
	}

}
