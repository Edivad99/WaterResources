package edivad.waterresources.jei.coolingplate;

import javax.annotation.Nonnull;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlate;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class CoolingPlateRecipeCategory implements IRecipeCategory<CoolingPlateRecipeWrapper> {

	private final IDrawable background;

	public CoolingPlateRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation(Main.MODID + ":textures/gui/cooling_plate.png");
		background = guiHelper.createDrawable(location, 8, 4, 154, 77);
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return CoolingPlate.machineID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return "Cooling plate";
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
		String info = "Use the Ice Water into the cooling plate in order to generate a thermal shock with Industrial distillator, and produce Distilled water";
		minecraft.fontRenderer.drawSplitString(info, 30, 10, 120, 4210752);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CoolingPlateRecipeWrapper recipeWrapper, IIngredients ingredients)
	{

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiFluidStacks.init(0, true, 2, 8, 16, 60, CoolingPlate.TANK_CAPACITY, true, null);

		FluidStack input = ingredients.getInputs(VanillaTypes.FLUID).get(0).get(0);

		guiFluidStacks.set(0, input);
	}

}
