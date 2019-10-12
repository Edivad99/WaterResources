package edivad.waterresources.fluids;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class IceWater extends Fluid {

	public IceWater()
	{
		super("ice_water", new ResourceLocation(Main.MODID, "blocks/ice_water_still"), new ResourceLocation(Main.MODID, "blocks/ice_water_flow"));
	}
}
