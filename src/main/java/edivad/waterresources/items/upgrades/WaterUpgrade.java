package edivad.waterresources.items.upgrades;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;

public class WaterUpgrade extends Upgrade {

	public WaterUpgrade()
	{
		super(16);
		setUnlocalizedName(Main.MODID + ".water_upgrade");
		setRegistryName(new ResourceLocation(Main.MODID, "water_upgrade"));
	}

}
