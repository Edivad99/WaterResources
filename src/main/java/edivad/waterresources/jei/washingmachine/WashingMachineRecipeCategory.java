package edivad.waterresources.jei.washingmachine;

import java.util.List;

import javax.annotation.Nonnull;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachine;
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

public class WashingMachineRecipeCategory implements IRecipeCategory<WashingMachineRecipeWrapper> {

	private final IDrawable background;

	public WashingMachineRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation(Main.MODID + ":textures/gui/washing_machine_with_info.png");
		background = guiHelper.createDrawable(location, 8, 4, 154, 77);
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return WashingMachine.machineID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return "Washing Machine";
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
	public void setRecipe(IRecipeLayout recipeLayout, WashingMachineRecipeWrapper recipeWrapper, IIngredients ingredients)
	{

		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, false, 132, 30);

		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);
		guiItemStacks.set(0, outputs);

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		guiFluidStacks.init(0, true, 30, 8, 16, 60, WashingMachine.TANK_CAPACITY, true, null);
		guiFluidStacks.init(1, true, 58, 8, 16, 60, WashingMachine.TANK_CAPACITY, true, null);

		List<FluidStack> inputsFluid = ingredients.getInputs(VanillaTypes.FLUID).get(0);
		guiFluidStacks.set(0, inputsFluid.get(0));
		guiFluidStacks.set(1, inputsFluid.get(1));

	}

}
