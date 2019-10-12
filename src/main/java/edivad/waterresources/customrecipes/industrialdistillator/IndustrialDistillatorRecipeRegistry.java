package edivad.waterresources.customrecipes.industrialdistillator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edivad.waterresources.config.IndustrialDistillatorConfig;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class IndustrialDistillatorRecipeRegistry {

	private static boolean isInit = false;
	private static List<IndustrialDistillatorRecipe> customRecipeList = new ArrayList<>();

	public static List<IndustrialDistillatorRecipe> getCustomRecipeList()
	{
		if(!isInit)
		{
			init();
			isInit = true;
		}
		return customRecipeList;
	}

	@Nullable
	public static IndustrialDistillatorRecipe getRecipe(FluidStack input)
	{
		for(IndustrialDistillatorRecipe recipe : getCustomRecipeList())
		{
			if(recipe.getInputFluid().isFluidEqual(input))
				return recipe;
		}
		return null;
	}

	private static void init()
	{
		customRecipeList.add(new IndustrialDistillatorRecipe(new FluidStack(FluidRegistry.WATER, IndustrialDistillatorConfig.WATER_CONSUMED)));
	}
}
