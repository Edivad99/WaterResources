package edivad.waterresources.config;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.extractor.Extractor;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MODID, category = "extractor")
public class ExtractorConfig {

	@Config.Comment(value = "Number of ticks for one processing operation")
	@Config.RangeInt(min = 1)
	public static int MAX_PROGRESS = 200;

	@Config.Comment(value = "How likely is it to receive the finished product (in percentage)")
	@Config.RangeInt(min = 1, max = 99)
	public static int BASE_PERCENT_OF_PROCESSING = 30;

	@Config.Comment(value = "How likely is it to receive the finished product (in percentage) with GoldFieldOre")
	@Config.RangeInt(min = 2, max = 99)
	public static int BASE_PERCENT_OF_PROCESSING_WITH_GOLDFIELD_ORE = 65;

	@Config.Comment(value = "How much distilled water is used to process the items")
	@Config.RangeInt(min = 1000, max = Extractor.TANK_CAPACITY)
	public static int DISTILLED_WATER_CONSUME = 1000;

	@Config.Comment(value = "How much auriferous water is produced by dirt")
	@Config.RangeInt(min = 1000, max = Extractor.TANK_CAPACITY)
	public static int AURIFEROUS_WATER_DIRT = 250;

	@Config.Comment(value = "How much auriferous water is produced by goldfield ore")
	@Config.RangeInt(min = 1000, max = Extractor.TANK_CAPACITY)
	public static int AURIFEROUS_WATER_GOLDFIELD_ORE = 1000;
}
