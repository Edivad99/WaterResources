package edivad.waterresources.customrecipes.coolingplate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edivad.waterresources.ModLiquids;
import net.minecraftforge.fluids.FluidStack;

public class CoolingPlateRecipeRegistry {

	private static boolean isInit = false;
	private static List<CoolingPlateRecipe> customRecipeList = new ArrayList<>();

	public static List<CoolingPlateRecipe> getCustomRecipeList()
	{
		if(!isInit)
		{
			init();
			isInit = true;
		}
		return customRecipeList;
	}

	@Nullable
	public boolean isRecipeValid(FluidStack input)
	{
		for(CoolingPlateRecipe recipe : getCustomRecipeList())
			if(recipe.getInput().isFluidEqual(input))
				return true;
		return false;
	}

	private static void init()
	{
		customRecipeList.add(new CoolingPlateRecipe(new FluidStack(ModLiquids.ice_water, 1000)));
	}
}
