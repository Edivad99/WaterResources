package edivad.waterresources.blocks.ore;

import java.util.Random;

import edivad.waterresources.Main;
import edivad.waterresources.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GoldFieldOre extends Block {

	public static final ResourceLocation GOLDFIELD_ORE = new ResourceLocation(Main.MODID, "goldfield_block");

	public GoldFieldOre()
	{

		super(Material.ROCK);
		setSoundType(SoundType.GROUND);
		setHardness(2.0F);
		setResistance(15.0F);
		setHarvestLevel("pickaxe", 1);
		setLightLevel(7.0F);

		setUnlocalizedName(Main.MODID + ".goldfield_block");
		setRegistryName(GOLDFIELD_ORE);
		setCreativeTab(Main.waterResourcesTab);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.GOLDFIELD_ORE);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
