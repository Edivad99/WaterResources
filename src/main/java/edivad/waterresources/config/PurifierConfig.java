package edivad.waterresources.config;

import edivad.waterresources.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, category = "purifier")
public class PurifierConfig {

	@Config.Comment(value = "Number of ticks for one processing operation")
	@Config.RangeInt(min = 1)
	public static int MAX_PROGRESS = 200;
}