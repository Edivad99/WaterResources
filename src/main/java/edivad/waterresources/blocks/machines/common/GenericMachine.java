package edivad.waterresources.blocks.machines.common;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;

import cofh.thermalfoundation.item.ItemWrench;
import edivad.waterresources.Main;
import edivad.waterresources.tools.interfaces.IGuiTile;
import edivad.waterresources.tools.interfaces.IRestorableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenericMachine extends Block {

	private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public static final int TANK_CAPACITY = 20000;

	public GenericMachine(String id)
	{
		super(Material.IRON);

		setSoundType(SoundType.METAL);
		setHarvestLevel("pickaxe", 2);
		setHardness(5F);
		setResistance(30F);
		setCreativeTab(Main.waterResourcesTab);

		setRegistryName(new ResourceLocation(Main.MODID, id));
		setUnlocalizedName(Main.MODID + "." + id);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;

		if(playerIn.isSneaking())
		{
			if(ItemStack.areItemsEqual(playerIn.getHeldItemMainhand(), ItemWrench.wrenchBasic))
			{
				dismantleBlock(worldIn, pos);
				return true;
			}

		}

		TileEntity te = worldIn.getTileEntity(pos);
		if(!(te instanceof IGuiTile))
			return false;

		playerIn.openGui(Main.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;

	}

	private void dismantleBlock(World worldIn, BlockPos pos)
	{

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		ItemStack itemStack = new ItemStack(Item.getItemFromBlock(this));
		// Always check this!!
		if(tileEntity instanceof IRestorableTileEntity)
		{

			NBTTagCompound tagCompound = new NBTTagCompound();
			((IRestorableTileEntity) tileEntity).writeRestorableToNBT(tagCompound);

			itemStack.setTagCompound(tagCompound);
		}

		worldIn.setBlockToAir(pos);

		EntityItem entityItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
		entityItem.motionX = 0;
		entityItem.motionZ = 0;
		worldIn.spawnEntity(entityItem);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> result, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		// Always check this!!
		if(tileEntity instanceof IRestorableTileEntity)
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			NBTTagCompound tagCompound = new NBTTagCompound();
			((IRestorableTileEntity) tileEntity).writeRestorableToNBT(tagCompound);

			stack.setTagCompound(tagCompound);
			result.add(stack);
		}
		else
		{
			super.getDrops(result, world, pos, state, fortune);
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if(willHarvest)
			return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// Always check this!!
		if(tileEntity instanceof IRestorableTileEntity)
		{
			NBTTagCompound tagCompound = stack.getTagCompound();
			if(tagCompound != null)
			{
				((IRestorableTileEntity) tileEntity).readRestorableFromNBT(tagCompound);
			}
		}
	}

	protected void addInformationLocalized(List<String> tooltip, String key, Object... parameters)
	{
		String translated = I18n.format(key, parameters);
		translated = COMPILE.matcher(translated).replaceAll("\u00a7");
		Collections.addAll(tooltip, StringUtils.split(translated, "\n"));
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty [] { FACING });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this.getItemDropped(state, RANDOM, 0));
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		return false;
	}

	protected int getItemCount(NBTTagCompound tagCompound, String itemTag)
	{
		int sizeIn = 0;
		NBTTagCompound compoundIn = (NBTTagCompound) tagCompound.getTag(itemTag);
		NBTTagList itemsIn = compoundIn.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < itemsIn.tagCount(); i++)
		{
			sizeIn = new ItemStack(itemsIn.getCompoundTagAt(i)).getCount();
		}
		return sizeIn;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
