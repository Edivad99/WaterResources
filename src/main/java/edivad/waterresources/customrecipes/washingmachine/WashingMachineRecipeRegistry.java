package edivad.waterresources.customrecipes.washingmachine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edivad.waterresources.ModItems;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.config.WashingMachineConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class WashingMachineRecipeRegistry {

	private static boolean isInit = false;
	private static List<WashingMachineRecipe> customRecipeList = new ArrayList<>();

	public static List<WashingMachineRecipe> getCustomRecipeList()
	{
		if(!isInit)
		{
			init();
			isInit = true;
		}
		return customRecipeList;
	}

	@Nullable
	public static WashingMachineRecipe getRecipe(FluidStack input1, FluidStack input2)
	{
		for(WashingMachineRecipe recipe : getCustomRecipeList())
		{
			if(recipe.getInput1().isFluidEqual(input1) && recipe.getInput2().isFluidEqual(input2))
				return recipe;
		}
		return null;
	}

	private static void init()
	{
		customRecipeList.add(new WashingMachineRecipe(new FluidStack(ModLiquids.distilled_water, WashingMachineConfig.WATER_CONSUMED), new FluidStack(ModLiquids.auriferous_water, WashingMachineConfig.WATER_CONSUMED), new ItemStack(ModItems.goldFragment, 2)));
	}
}
