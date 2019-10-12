package edivad.waterresources.blocks.machines.coolingplate;

import java.util.List;

import org.lwjgl.input.Keyboard;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.common.GenericMachine;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoolingPlate extends GenericMachine implements ITileEntityProvider {

	public static final PropertyEnum<CoolingPlateState> STATE = PropertyEnum.<CoolingPlateState>create("state", CoolingPlateState.class);

	public static final ResourceLocation resourceLocation = new ResourceLocation(Main.MODID, "cooling_plate");
	public static final String machineID = Main.MODID + ".cooling_plate";

	private static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

	public CoolingPlate()
	{
		super("cooling_plate");
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty [] { FACING, STATE });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCoolingPlate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null)
		{
			FluidTank info = new FluidTank(TANK_CAPACITY);

			int water = info.readFromNBT(tagCompound.getCompoundTag("tank")).getFluidAmount();

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				addInformationLocalized(tooltip, "message.waterresources.cooling_plate", water);
			else
				addInformationLocalized(tooltip, "message.waterresources.hold_shift");
		}

	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB_TOP_HALF;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
		if(te instanceof TileEntityCoolingPlate)
			return state.withProperty(STATE, ((TileEntityCoolingPlate) te).getState());
		return super.getActualState(state, worldIn, pos);
	}
}
