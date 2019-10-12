package edivad.waterresources.items.upgrades;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;

public class SpeedUpgrade extends Upgrade {

	public SpeedUpgrade()
	{
		super(16);
		setUnlocalizedName(Main.MODID + ".speed_upgrade");
		setRegistryName(new ResourceLocation(Main.MODID, "speed_upgrade"));
	}

}
