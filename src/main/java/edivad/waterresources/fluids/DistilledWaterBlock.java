package edivad.waterresources.fluids;

import edivad.waterresources.Main;
import edivad.waterresources.ModLiquids;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DistilledWaterBlock extends BlockFluidClassic {

	public static final ResourceLocation DISTILLEDWATER = new ResourceLocation(Main.MODID, "distilled_water");

	public DistilledWaterBlock()
	{
		super(ModLiquids.distilled_water, Material.WATER);
		setCreativeTab(Main.waterResourcesTab);
		setUnlocalizedName(Main.MODID + ".distilled_water");
		setRegistryName(DISTILLEDWATER);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelResourceLocation fluidLocation = new ModelResourceLocation(DISTILLEDWATER, "fluid");

		StateMapperBase customState = new StateMapperBase() {

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState)
			{
				return fluidLocation;
			}
		};
		ModelLoader.setCustomStateMapper(this, customState);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(DISTILLEDWATER, "inventory"));
	}
}
