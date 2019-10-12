package edivad.waterresources.items.upgrades;

import edivad.waterresources.Main;
import net.minecraft.util.ResourceLocation;

public class RemoveUpgrade extends Upgrade {

	public RemoveUpgrade()
	{
		super(1);
		setUnlocalizedName(Main.MODID + ".remove_upgrade");
		setRegistryName(new ResourceLocation(Main.MODID, "remove_upgrade"));
	}

}
