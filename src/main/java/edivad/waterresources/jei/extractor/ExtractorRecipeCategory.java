package edivad.waterresources.jei.extractor;

import java.util.List;

import javax.annotation.Nonnull;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.extractor.Extractor;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class ExtractorRecipeCategory implements IRecipeCategory<ExtractorRecipeWrapper> {

	private final IDrawable background;

	public ExtractorRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation(Main.MODID + ":textures/gui/extractor_with_info.png");
		background = guiHelper.createDrawable(location, 8, 4, 154, 77);
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return Extractor.machineID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return "Extractor";
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
	public void setRecipe(IRecipeLayout recipeLayout, ExtractorRecipeWrapper recipeWrapper, IIngredients ingredients)
	{

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(0, true, 61, 30);
		List<ItemStack> inputs = ingredients.getInputs(VanillaTypes.ITEM).get(0);
		guiItemStacks.set(0, inputs);

		List<FluidStack> inputFluid = ingredients.getInputs(VanillaTypes.FLUID).get(0);
		List<FluidStack> outputFluid = ingredients.getOutputs(VanillaTypes.FLUID).get(0);

		guiFluidStacks.init(0, true, 30, 8, 16, 60, Extractor.TANK_CAPACITY, true, null);
		guiFluidStacks.init(1, false, 135, 8, 16, 60, Extractor.TANK_CAPACITY, true, null);

		guiFluidStacks.set(0, inputFluid);
		guiFluidStacks.set(1, outputFluid);
	}

}
