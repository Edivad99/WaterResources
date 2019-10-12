package edivad.waterresources.items.upgrades;

import edivad.waterresources.Main;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Upgrade extends Item {

	public Upgrade(int max_stack)
	{
		setCreativeTab(Main.waterResourcesTab);
		setMaxStackSize(max_stack);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
