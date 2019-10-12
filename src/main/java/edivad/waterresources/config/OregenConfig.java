package edivad.waterresources.config;

import edivad.waterresources.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, category = "oregen")
public class OregenConfig {

	@Config.Comment(value = "Enable retrogen")
	public static boolean RETROGEN = true;

	@Config.Comment(value = "Enable verbose logging for retrogen")
	public static boolean VERBOSE = false;

	@Config.Comment(value = "Generate ore in the overworld")
	public static boolean GENERATE_OVERWORLD = true;

	@Config.Comment(value = "Minimum size of every ore vein")
	public static int MIN_VEIN_SIZE = 10;

	@Config.Comment(value = "Maximum size of every ore vein")
	public static int MAX_VEIN_SIZE = 35;

	@Config.Comment(value = "Maximum veins per chunk")
	public static int CHANCES_TO_SPAWN = 5;

	@Config.Comment(value = "Minimum height for the ore")
	public static int MIN_Y = 25;

	@Config.Comment(value = "Maximum height for the ore")
	public static int MAX_Y = 50;
}
