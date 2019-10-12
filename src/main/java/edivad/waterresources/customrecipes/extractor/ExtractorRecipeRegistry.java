package edivad.waterresources.customrecipes.extractor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edivad.waterresources.ModBlocks;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.config.ExtractorConfig;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ExtractorRecipeRegistry {

	private static boolean isInit = false;
	private static List<ExtractorRecipe> customRecipeList = new ArrayList<>();

	public static List<ExtractorRecipe> getCustomRecipeList()
	{
		if(!isInit)
		{
			init();
			isInit = true;
		}
		return customRecipeList;
	}

	@Nullable
	public static ExtractorRecipe getRecipe(ItemStack input)
	{
		for(ExtractorRecipe recipe : getCustomRecipeList())
		{
			if(ItemStack.areItemsEqual(input, recipe.getInput()))
				return recipe;
		}
		return null;
	}

	private static void init()
	{
		customRecipeList.add(new ExtractorRecipe(new ItemStack(ModBlocks.GOLDFIELD_ORE), new FluidStack(ModLiquids.distilled_water, ExtractorConfig.DISTILLED_WATER_CONSUME), new FluidStack(ModLiquids.auriferous_water, ExtractorConfig.AURIFEROUS_WATER_GOLDFIELD_ORE)));
		customRecipeList.add(new ExtractorRecipe(new ItemStack(Blocks.DIRT), new FluidStack(ModLiquids.distilled_water, ExtractorConfig.DISTILLED_WATER_CONSUME), new FluidStack(ModLiquids.auriferous_water, ExtractorConfig.AURIFEROUS_WATER_DIRT)));
	}
}
