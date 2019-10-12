package edivad.waterresources.customrecipes;

import net.minecraft.item.ItemStack;

public class GenericRecipe {

	private final ItemStack input;
	private final ItemStack output;

	public GenericRecipe(ItemStack input, ItemStack output)
	{
		this.input = input;
		this.output = output;
	}

	public ItemStack getInput()
	{
		return input;
	}

	public ItemStack getOutput()
	{
		return output;
	}

}
