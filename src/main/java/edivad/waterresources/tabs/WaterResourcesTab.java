package edivad.waterresources.tabs;

import edivad.waterresources.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class WaterResourcesTab extends CreativeTabs {

	public WaterResourcesTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModBlocks.EXTRACTOR);
	}
}
