package edivad.waterresources.config;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlate;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, category = "extractor")
public class CoolingPlateConfig {

	@Config.Comment(value = "Number of ticks for one processing operation")
	@Config.RangeInt(min = 1)
	public static int MAX_PROGRESS = 200;

	@Config.Comment(value = "How much water consume for working")
	@Config.RangeInt(min = 500, max = CoolingPlate.TANK_CAPACITY)
	public static int WATER_CONSUMED = 500;
}
