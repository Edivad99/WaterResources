package edivad.waterresources.jei.purifier;

import java.util.List;

import javax.annotation.Nonnull;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.purifier.Purifier;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PurifierRecipeCategory implements IRecipeCategory<PurifierRecipeWrapper> {

	private final IDrawable background;

	public PurifierRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation(Main.MODID + ":textures/gui/purifier_with_info.png");
		background = guiHelper.createDrawable(location, 8, 4, 154, 77);
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return Purifier.machineID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return "Purifier";
	}

	@Override
	public String getModName()
	{
		return Main.MODNAME;
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft)
	{
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PurifierRecipeWrapper recipeWrapper, IIngredients ingredients)
	{

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 61, 30);
		guiItemStacks.init(1, false, 132, 30);

		List<ItemStack> inputs = ingredients.getInputs(VanillaTypes.ITEM).get(0);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		guiItemStacks.set(0, inputs);
		guiItemStacks.set(1, outputs);

	}

}
