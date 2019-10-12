package edivad.waterresources.customrecipes.watercooling;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edivad.waterresources.ModLiquids;
import edivad.waterresources.config.WaterCoolingConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class WaterCoolingRecipeRegistry {

	private static boolean isInit = false;
	private static List<WaterCoolingRecipe> customRecipeList = new ArrayList<>();

	public static List<WaterCoolingRecipe> getCustomRecipeList()
	{
		if(!isInit)
		{
			init();
			isInit = true;
		}
		return customRecipeList;
	}

	@Nullable
	public static WaterCoolingRecipe getRecipe(ItemStack input)
	{
		for(WaterCoolingRecipe recipe : getCustomRecipeList())
		{
			if(ItemStack.areItemsEqual(input, recipe.getInput()))
				return recipe;
		}
		return null;
	}

	private static void init()
	{
		customRecipeList.add(new WaterCoolingRecipe(new ItemStack(Blocks.ICE), new FluidStack(ModLiquids.ice_water, WaterCoolingConfig.WATER_PRODUCED_ICE)));
		customRecipeList.add(new WaterCoolingRecipe(new ItemStack(Blocks.PACKED_ICE), new FluidStack(ModLiquids.ice_water, WaterCoolingConfig.WATER_PRODUCED_PACKED_ICE)));
		customRecipeList.add(new WaterCoolingRecipe(new ItemStack(Items.SNOWBALL), new FluidStack(ModLiquids.ice_water, WaterCoolingConfig.WATER_PRODUCED_SNOWBALL)));
	}
}
