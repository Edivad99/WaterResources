package edivad.waterresources;

import edivad.waterresources.blocks.machines.coolingplate.CoolingPlate;
import edivad.waterresources.blocks.machines.coolingplate.TileEntityCoolingPlate;
import edivad.waterresources.blocks.machines.extractor.Extractor;
import edivad.waterresources.blocks.machines.extractor.TileEntityExtractor;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillator;
import edivad.waterresources.blocks.machines.industrialdistillator.TileEntityIndustrialDistillator;
import edivad.waterresources.blocks.machines.purifier.Purifier;
import edivad.waterresources.blocks.machines.purifier.TileEntityPurifier;
import edivad.waterresources.blocks.machines.washingmachine.TileEntityWashingMachine;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachine;
import edivad.waterresources.blocks.machines.watercooling.TileEntityWaterCooling;
import edivad.waterresources.blocks.machines.watercooling.WaterCooling;
import edivad.waterresources.blocks.ore.GoldFieldOre;
import edivad.waterresources.fluids.AuriferousWaterBlock;
import edivad.waterresources.fluids.DistilledWaterBlock;
import edivad.waterresources.fluids.IceWaterBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	// Machines
	@GameRegistry.ObjectHolder(Main.MODID + ":extractor")
	public static Extractor EXTRACTOR;

	@GameRegistry.ObjectHolder(Main.MODID + ":water_cooling")
	public static WaterCooling WATER_COOLING;

	@GameRegistry.ObjectHolder(Main.MODID + ":washing_machine")
	public static WashingMachine WASHING_MACHINE;

	@GameRegistry.ObjectHolder(Main.MODID + ":purifier")
	public static Purifier PURIFIER;

	@GameRegistry.ObjectHolder(Main.MODID + ":cooling_plate")
	public static CoolingPlate COOLING_PLATE;

	@GameRegistry.ObjectHolder(Main.MODID + ":industrial_distillator")
	public static IndustrialDistillator INDUSTRIAL_DISTILLATOR;

	// Ores
	@GameRegistry.ObjectHolder(Main.MODID + ":goldfield_block")
	public static GoldFieldOre GOLDFIELD_ORE;

	// Liquids
	@GameRegistry.ObjectHolder(Main.MODID + ":distilled_water")
	public static DistilledWaterBlock DISTILLED_WATER_BLOCK;

	@GameRegistry.ObjectHolder(Main.MODID + ":ice_water")
	public static IceWaterBlock ICE_WATER_BLOCK;

	@GameRegistry.ObjectHolder(Main.MODID + ":auriferous_water")
	public static AuriferousWaterBlock AURIFEROUS_WATER_BLOCK;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		EXTRACTOR.initModel();
		WATER_COOLING.initModel();
		WASHING_MACHINE.initModel();
		PURIFIER.initModel();
		COOLING_PLATE.initModel();
		INDUSTRIAL_DISTILLATOR.initModel();

		GOLDFIELD_ORE.initModel();

		DISTILLED_WATER_BLOCK.initModel();
		ICE_WATER_BLOCK.initModel();
		AURIFEROUS_WATER_BLOCK.initModel();
	}

	public static void register(IForgeRegistry<Block> registry)
	{

		registry.register(new Extractor());
		GameRegistry.registerTileEntity(TileEntityExtractor.class, Main.MODID + ":extractor");

		registry.register(new WaterCooling());
		GameRegistry.registerTileEntity(TileEntityWaterCooling.class, Main.MODID + ":water_cooling");

		registry.register(new WashingMachine());
		GameRegistry.registerTileEntity(TileEntityWashingMachine.class, Main.MODID + ":washing_machine");

		registry.register(new Purifier());
		GameRegistry.registerTileEntity(TileEntityPurifier.class, Main.MODID + ":purifier");

		registry.register(new CoolingPlate());
		GameRegistry.registerTileEntity(TileEntityCoolingPlate.class, Main.MODID + ":cooling_plate");

		registry.register(new IndustrialDistillator());
		GameRegistry.registerTileEntity(TileEntityIndustrialDistillator.class, Main.MODID + ":industrial_distillator");

		registry.register(new GoldFieldOre());

		registry.register(new DistilledWaterBlock());
		registry.register(new IceWaterBlock());
		registry.register(new AuriferousWaterBlock());
	}

}
