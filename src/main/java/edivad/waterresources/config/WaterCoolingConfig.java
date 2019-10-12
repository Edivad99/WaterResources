package edivad.waterresources.config;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.watercooling.WaterCooling;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, category = "water_cooling")
public class WaterCoolingConfig {

	@Config.Comment(value = "Number of ticks for one processing operation")
	@Config.RangeInt(min = 1)
	public static int MAX_PROGRESS = 200;

	@Config.Comment(value = "Water consumed from processing")
	@Config.RangeInt(min = 250, max = WaterCooling.TANK_CAPACITY)
	public static int WATER_CONSUMED = 500;

	@Config.Comment(value = "Water produced from Ice")
	@Config.RangeInt(min = 250, max = WaterCooling.TANK_CAPACITY)
	public static int WATER_PRODUCED_ICE = 750;

	@Config.Comment(value = "Water produced from Packed Ice")
	@Config.RangeInt(min = 250, max = WaterCooling.TANK_CAPACITY)
	public static int WATER_PRODUCED_PACKED_ICE = 1500;

	@Config.Comment(value = "Water produced from Snowball")
	@Config.RangeInt(min = 250, max = WaterCooling.TANK_CAPACITY)
	public static int WATER_PRODUCED_SNOWBALL = 500;
}