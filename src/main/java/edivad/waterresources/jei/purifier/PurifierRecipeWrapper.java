package edivad.waterresources.jei.purifier;

import java.util.Collections;
import java.util.List;

import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.config.PurifierConfig;
import edivad.waterresources.customrecipes.GenericRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class PurifierRecipeWrapper implements ICraftingRecipeWrapper {

	private final List<List<ItemStack>> inputs;
	private final ItemStack output;

	public PurifierRecipeWrapper(GenericRecipe recipe)
	{
		this.inputs = Collections.singletonList(Collections.singletonList(recipe.getInput().copy()));
		this.output = recipe.getOutput().copy();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);

	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{

		String energyRequired = (PoweredTileEntity.RF_PER_TICK * PurifierConfig.MAX_PROGRESS) + " FE";
		minecraft.fontRenderer.drawString(energyRequired, 60, 68, 4210752);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}
}
