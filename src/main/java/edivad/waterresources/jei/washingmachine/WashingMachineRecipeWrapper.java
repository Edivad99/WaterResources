package edivad.waterresources.jei.washingmachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.config.WashingMachineConfig;
import edivad.waterresources.customrecipes.washingmachine.WashingMachineRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class WashingMachineRecipeWrapper implements ICraftingRecipeWrapper {

	private final ItemStack output;
	private final List<List<FluidStack>> inputFluid;

	public WashingMachineRecipeWrapper(WashingMachineRecipe recipe)
	{
		this.output = recipe.getOutput().copy();
		ArrayList<FluidStack> supp = new ArrayList<FluidStack>();
		supp.add(recipe.getInput1().copy());
		supp.add(recipe.getInput2().copy());
		this.inputFluid = Collections.singletonList(supp);
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setOutput(VanillaTypes.ITEM, output);
		ingredients.setInputLists(VanillaTypes.FLUID, inputFluid);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		String energyRequired = (PoweredTileEntity.RF_PER_TICK * WashingMachineConfig.MAX_PROGRESS) + " FE";
		minecraft.fontRenderer.drawString(energyRequired, 80, 60, 4210752);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return Collections.emptyList();
	}
}
