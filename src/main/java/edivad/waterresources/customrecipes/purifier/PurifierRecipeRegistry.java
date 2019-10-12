package edivad.waterresources.customrecipes.purifier;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cofh.thermalfoundation.item.ItemMaterial;
import edivad.waterresources.ModItems;
import edivad.waterresources.customrecipes.GenericRecipe;
import net.minecraft.item.ItemStack;

public class PurifierRecipeRegistry {

	private static boolean isInit = false;
	private static List<GenericRecipe> customRecipeList = new ArrayList<>();

	public static List<GenericRecipe> getCustomRecipeList()
	{
		if(!isInit)
		{
			init();
			isInit = true;
		}
		return customRecipeList;
	}

	@Nullable
	public static GenericRecipe getRecipe(ItemStack input)
	{
		for(GenericRecipe recipe : getCustomRecipeList())
		{
			if(ItemStack.areItemsEqual(input, recipe.getInput()))
				return recipe;
		}
		return null;
	}

	private static void init()
	{
		customRecipeList.add(new GenericRecipe(new ItemStack(ModItems.goldFragment), new ItemStack(ItemMaterial.dustGold.getItem(), 3, 1)));
	}
}
