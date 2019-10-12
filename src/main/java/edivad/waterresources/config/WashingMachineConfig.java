package edivad.waterresources.config;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachine;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, category = "washing_machine")
public class WashingMachineConfig {

	@Config.Comment(value = "Number of ticks for one processing operation")
	@Config.RangeInt(min = 1)
	public static int MAX_PROGRESS = 200;

	@Config.Comment(value = "Water consumed for the process")
	@Config.RangeInt(min = 250, max = WashingMachine.TANK_CAPACITY)
	public static int WATER_CONSUMED = 1000;
}