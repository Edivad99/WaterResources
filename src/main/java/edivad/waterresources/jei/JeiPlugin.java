package edivad.waterresources.jei;

import javax.annotation.Nonnull;

import edivad.waterresources.ModBlocks;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlate;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlateContainer;
import edivad.waterresources.blocks.machines.extractor.Extractor;
import edivad.waterresources.blocks.machines.extractor.ExtractorContainer;
import edivad.waterresources.blocks.machines.extractor.TileEntityExtractor;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillator;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillatorContainer;
import edivad.waterresources.blocks.machines.purifier.Purifier;
import edivad.waterresources.blocks.machines.purifier.PurifierContainer;
import edivad.waterresources.blocks.machines.purifier.TileEntityPurifier;
import edivad.waterresources.blocks.machines.washingmachine.TileEntityWashingMachine;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachine;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachineContainer;
import edivad.waterresources.blocks.machines.watercooling.TileEntityWaterCooling;
import edivad.waterresources.blocks.machines.watercooling.WaterCooling;
import edivad.waterresources.blocks.machines.watercooling.WaterCoolingContainer;
import edivad.waterresources.customrecipes.GenericRecipe;
import edivad.waterresources.customrecipes.coolingplate.CoolingPlateRecipe;
import edivad.waterresources.customrecipes.coolingplate.CoolingPlateRecipeRegistry;
import edivad.waterresources.customrecipes.extractor.ExtractorRecipe;
import edivad.waterresources.customrecipes.extractor.ExtractorRecipeRegistry;
import edivad.waterresources.customrecipes.industrialdistillator.IndustrialDistillatorRecipe;
import edivad.waterresources.customrecipes.industrialdistillator.IndustrialDistillatorRecipeRegistry;
import edivad.waterresources.customrecipes.purifier.PurifierRecipeRegistry;
import edivad.waterresources.customrecipes.washingmachine.WashingMachineRecipe;
import edivad.waterresources.customrecipes.washingmachine.WashingMachineRecipeRegistry;
import edivad.waterresources.customrecipes.watercooling.WaterCoolingRecipe;
import edivad.waterresources.customrecipes.watercooling.WaterCoolingRecipeRegistry;
import edivad.waterresources.jei.coolingplate.CoolingPlateRecipeCategory;
import edivad.waterresources.jei.coolingplate.CoolingPlateRecipeWrapper;
import edivad.waterresources.jei.extractor.ExtractorRecipeCategory;
import edivad.waterresources.jei.extractor.ExtractorRecipeWrapper;
import edivad.waterresources.jei.industrialdistillator.IndustrialDistillatorRecipeCategory;
import edivad.waterresources.jei.industrialdistillator.IndustrialDistillatorRecipeWrapper;
import edivad.waterresources.jei.purifier.PurifierRecipeCategory;
import edivad.waterresources.jei.purifier.PurifierRecipeWrapper;
import edivad.waterresources.jei.washingmachine.WashingMachineRecipeCategory;
import edivad.waterresources.jei.washingmachine.WashingMachineRecipeWrapper;
import edivad.waterresources.jei.watercooling.WaterCoolingRecipeCategory;
import edivad.waterresources.jei.watercooling.WaterCoolingRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry)
	{
		registerExtractorHandling(registry);
		registerWaterCoolingHandling(registry);
		registerWashingMachineHandling(registry);
		registerPurifierHandling(registry);
		registerIndustrialDistillatorHandling(registry);
		registerCoolingPlateHandling(registry);
	}

	private void registerExtractorHandling(@Nonnull IModRegistry registry)
	{
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.EXTRACTOR), Extractor.machineID);
		registry.addRecipes(ExtractorRecipeRegistry.getCustomRecipeList(), Extractor.machineID);

		registry.handleRecipes(ExtractorRecipe.class, recipe -> new ExtractorRecipeWrapper(recipe), Extractor.machineID);

		transferRegistry.addRecipeTransferHandler(ExtractorContainer.class, Extractor.machineID, 0, TileEntityExtractor.INPUT_SLOTS, TileEntityExtractor.INPUT_SLOTS + TileEntityExtractor.OUTPUT_SLOTS, 36);
	}

	private void registerWaterCoolingHandling(@Nonnull IModRegistry registry)
	{
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.WATER_COOLING), WaterCooling.machineID);
		registry.addRecipes(WaterCoolingRecipeRegistry.getCustomRecipeList(), WaterCooling.machineID);

		registry.handleRecipes(WaterCoolingRecipe.class, recipe -> new WaterCoolingRecipeWrapper(recipe), WaterCooling.machineID);

		transferRegistry.addRecipeTransferHandler(WaterCoolingContainer.class, WaterCooling.machineID, 0, TileEntityWaterCooling.INPUT_SLOTS, TileEntityWaterCooling.INPUT_SLOTS + TileEntityWaterCooling.OUTPUT_SLOTS, 36);

	}

	private void registerWashingMachineHandling(@Nonnull IModRegistry registry)
	{
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.WASHING_MACHINE), WashingMachine.machineID);
		registry.addRecipes(WashingMachineRecipeRegistry.getCustomRecipeList(), WashingMachine.machineID);

		registry.handleRecipes(WashingMachineRecipe.class, recipe -> new WashingMachineRecipeWrapper(recipe), WashingMachine.machineID);

		transferRegistry.addRecipeTransferHandler(WashingMachineContainer.class, WashingMachine.machineID, 0, TileEntityWashingMachine.INPUT_SLOTS, TileEntityWashingMachine.INPUT_SLOTS + TileEntityWashingMachine.OUTPUT_SLOTS, 36);
	}

	private void registerPurifierHandling(@Nonnull IModRegistry registry)
	{
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.PURIFIER), Purifier.machineID);
		registry.addRecipes(PurifierRecipeRegistry.getCustomRecipeList(), Purifier.machineID);

		registry.handleRecipes(GenericRecipe.class, recipe -> new PurifierRecipeWrapper(recipe), Purifier.machineID);

		transferRegistry.addRecipeTransferHandler(PurifierContainer.class, Purifier.machineID, 0, TileEntityPurifier.INPUT_SLOTS, TileEntityPurifier.INPUT_SLOTS + TileEntityPurifier.OUTPUT_SLOTS, 36);
	}

	private void registerIndustrialDistillatorHandling(@Nonnull IModRegistry registry)
	{
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.INDUSTRIAL_DISTILLATOR), IndustrialDistillator.machineID);
		registry.addRecipes(IndustrialDistillatorRecipeRegistry.getCustomRecipeList(), IndustrialDistillator.machineID);

		registry.handleRecipes(IndustrialDistillatorRecipe.class, recipe -> new IndustrialDistillatorRecipeWrapper(recipe), IndustrialDistillator.machineID);

		transferRegistry.addRecipeTransferHandler(IndustrialDistillatorContainer.class, IndustrialDistillator.machineID, 0, 0, 0, 36);

	}

	private void registerCoolingPlateHandling(@Nonnull IModRegistry registry)
	{
		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.COOLING_PLATE), CoolingPlate.machineID);
		registry.addRecipes(CoolingPlateRecipeRegistry.getCustomRecipeList(), CoolingPlate.machineID);

		registry.handleRecipes(CoolingPlateRecipe.class, recipe -> new CoolingPlateRecipeWrapper(recipe), CoolingPlate.machineID);

		transferRegistry.addRecipeTransferHandler(CoolingPlateContainer.class, CoolingPlate.machineID, 0, 0, 0, 36);

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		IJeiHelpers helpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = helpers.getGuiHelper();

		registry.addRecipeCategories(new ExtractorRecipeCategory(guiHelper));
		registry.addRecipeCategories(new WaterCoolingRecipeCategory(guiHelper));
		registry.addRecipeCategories(new WashingMachineRecipeCategory(guiHelper));
		registry.addRecipeCategories(new PurifierRecipeCategory(guiHelper));
		registry.addRecipeCategories(new IndustrialDistillatorRecipeCategory(guiHelper));
		registry.addRecipeCategories(new CoolingPlateRecipeCategory(guiHelper));
	}

}
