package edivad.waterresources.items.upgrades;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;

public class GoldUpgrade extends Upgrade {

	public GoldUpgrade()
	{
		super(16);
		setUnlocalizedName(Main.MODID + ".gold_upgrade");
		setRegistryName(new ResourceLocation(Main.MODID, "gold_upgrade"));
	}

}
