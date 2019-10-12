package edivad.waterresources.fluids;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class AuriferousWater extends Fluid {

	public AuriferousWater()
	{
		super("auriferous_water", new ResourceLocation(Main.MODID, "blocks/auriferous_water_still"), new ResourceLocation(Main.MODID, "blocks/auriferous_water_flow"));
		setViscosity(2000);
		setLuminosity(7);
	}
}
