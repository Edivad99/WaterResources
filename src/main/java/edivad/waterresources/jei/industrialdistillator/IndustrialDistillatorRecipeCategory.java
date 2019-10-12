package edivad.waterresources.jei.industrialdistillator;

import java.util.List;

import javax.annotation.Nonnull;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillator;
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

public class IndustrialDistillatorRecipeCategory implements IRecipeCategory<IndustrialDistillatorRecipeWrapper> {

	private final IDrawable background;

	public IndustrialDistillatorRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation(Main.MODID + ":textures/gui/industrial_distillator_with_info.png");
		background = guiHelper.createDrawable(location, 8, 4, 154, 77);
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return IndustrialDistillator.machineID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return "Industrial Distillator";
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
	public void setRecipe(IRecipeLayout recipeLayout, IndustrialDistillatorRecipeWrapper recipeWrapper, IIngredients ingredients)
	{

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiFluidStacks.init(0, true, 30, 8, 16, 60, IndustrialDistillator.TANK_CAPACITY, true, null);
		guiFluidStacks.init(1, false, 135, 8, 16, 60, IndustrialDistillator.TANK_CAPACITY, true, null);

		List<FluidStack> inputs = ingredients.getInputs(VanillaTypes.FLUID).get(0);
		List<FluidStack> outputs = ingredients.getOutputs(VanillaTypes.FLUID).get(0);
		guiFluidStacks.set(0, inputs);
		guiFluidStacks.set(1, outputs);
	}

}
