package edivad.waterresources.customrecipes.watercooling;

import edivad.waterresources.config.WaterCoolingConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class WaterCoolingRecipe {

	private final ItemStack input;
	private final FluidStack inputFluid, outputFluid;

	public WaterCoolingRecipe(ItemStack input, FluidStack outputFluid)
	{
		this.input = input;
		this.inputFluid = new FluidStack(FluidRegistry.WATER, WaterCoolingConfig.WATER_CONSUMED);
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
