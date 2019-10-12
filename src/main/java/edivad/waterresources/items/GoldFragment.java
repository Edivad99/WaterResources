package edivad.waterresources.items;

import edivad.waterresources.Main;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GoldFragment extends Item {

	public GoldFragment()
	{
		setUnlocalizedName(Main.MODID + ".gold_fragment");
		setRegistryName(new ResourceLocation(Main.MODID, "gold_fragment"));
		setCreativeTab(Main.waterResourcesTab);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}