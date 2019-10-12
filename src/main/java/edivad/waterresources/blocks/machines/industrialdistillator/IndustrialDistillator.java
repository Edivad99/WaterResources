package edivad.waterresources.blocks.machines.industrialdistillator;

import java.util.List;

import org.lwjgl.input.Keyboard;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.common.GenericMachine;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlateState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IndustrialDistillator extends GenericMachine implements ITileEntityProvider {

	public static final PropertyEnum<CoolingPlateState> STATE = PropertyEnum.<CoolingPlateState>create("state", CoolingPlateState.class);

	public static final ResourceLocation resourceLocation = new ResourceLocation(Main.MODID, "industrial_distillator");
	public static final String machineID = Main.MODID + ".industrial_distillator";

	public IndustrialDistillator()
	{
		super("industrial_distillator");
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty [] { FACING, STATE });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityIndustrialDistillator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null)
		{
			int energy = tagCompound.getInteger("energy");

			FluidTank info = new FluidTank(TANK_CAPACITY);

			int water = info.readFromNBT(tagCompound.getCompoundTag("tankIn")).getFluidAmount();
			int iceWater = info.readFromNBT(tagCompound.getCompoundTag("tankOut")).getFluidAmount();

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				addInformationLocalized(tooltip, "message.waterresources.industrial_distillator", energy, water, iceWater);
			else
				addInformationLocalized(tooltip, "message.waterresources.hold_shift");
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
		if(te instanceof TileEntityIndustrialDistillator)
			return state.withProperty(STATE, ((TileEntityIndustrialDistillator) te).getState());
		return super.getActualState(state, worldIn, pos);
	}
}
