package edivad.waterresources;

import edivad.waterresources.blocks.machines.coolingplate.CoolingPlate;
import edivad.waterresources.blocks.machines.extractor.Extractor;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillator;
import edivad.waterresources.blocks.machines.purifier.Purifier;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachine;
import edivad.waterresources.blocks.machines.watercooling.WaterCooling;
import edivad.waterresources.blocks.ore.GoldFieldOre;
import edivad.waterresources.items.GoldFragment;
import edivad.waterresources.items.upgrades.GoldUpgrade;
import edivad.waterresources.items.upgrades.RemoveUpgrade;
import edivad.waterresources.items.upgrades.SpeedUpgrade;
import edivad.waterresources.items.upgrades.WaterUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	// Upgrades
	@GameRegistry.ObjectHolder(Main.MODID + ":gold_upgrade")
	public static GoldUpgrade goldUpgrade;

	@GameRegistry.ObjectHolder(Main.MODID + ":speed_upgrade")
	public static SpeedUpgrade speedUpgrade;

	@GameRegistry.ObjectHolder(Main.MODID + ":water_upgrade")
	public static WaterUpgrade waterUpgrade;

	@GameRegistry.ObjectHolder(Main.MODID + ":remove_upgrade")
	public static RemoveUpgrade removeUpgrade;

	// Items
	@GameRegistry.ObjectHolder(Main.MODID + ":gold_fragment")
	public static GoldFragment goldFragment;

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		// Upgrades
		//goldUpgrade.initModel();
		//speedUpgrade.initModel();
		//waterUpgrade.initModel();
		//removeUpgrade.initModel();

		// Items
		goldFragment.initModel();
	}

	public static void register(IForgeRegistry<Item> registry)
	{
		// My items
		// Upgrades
		//registry.register(new GoldUpgrade());
		//registry.register(new SpeedUpgrade());
		//registry.register(new WaterUpgrade());
		//registry.register(new RemoveUpgrade());

		// Items
		registry.register(new GoldFragment());

		// Blocks
		// Ores
		registry.register(new ItemBlock(ModBlocks.GOLDFIELD_ORE).setRegistryName(GoldFieldOre.GOLDFIELD_ORE));

		// Machines
		registry.register(new ItemBlock(ModBlocks.EXTRACTOR).setRegistryName(Extractor.resourceLocation));
		registry.register(new ItemBlock(ModBlocks.WATER_COOLING).setRegistryName(WaterCooling.resourceLocation));
		registry.register(new ItemBlock(ModBlocks.WASHING_MACHINE).setRegistryName(WashingMachine.resourceLocation));
		registry.register(new ItemBlock(ModBlocks.PURIFIER).setRegistryName(Purifier.resourceLocation));
		registry.register(new ItemBlock(ModBlocks.COOLING_PLATE).setRegistryName(CoolingPlate.resourceLocation));
		registry.register(new ItemBlock(ModBlocks.INDUSTRIAL_DISTILLATOR).setRegistryName(IndustrialDistillator.resourceLocation));
	}

}
