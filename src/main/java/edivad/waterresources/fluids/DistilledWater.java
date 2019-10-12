package edivad.waterresources.fluids;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class DistilledWater extends Fluid {

	public DistilledWater()
	{
		super("distilled_water", new ResourceLocation(Main.MODID, "blocks/distilled_water_still"), new ResourceLocation(Main.MODID, "blocks/distilled_water_flow"));
	}
}
