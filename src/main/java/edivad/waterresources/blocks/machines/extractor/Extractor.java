package edivad.waterresources.blocks.machines.extractor;

import java.util.List;

import org.lwjgl.input.Keyboard;

import edivad.waterresources.Main;
import edivad.waterresources.blocks.machines.common.GenericMachine;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Extractor extends GenericMachine implements ITileEntityProvider {

	public static final ResourceLocation resourceLocation = new ResourceLocation(Main.MODID, "extractor");
	public static final String machineID = Main.MODID + ".extractor";

	public Extractor()
	{
		super("extractor");
		//this.setTickRandomly(true);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityExtractor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null)
		{
			int energy = tagCompound.getInteger("energy");
			int sizeIn = getItemCount(tagCompound, "itemsIn");

			FluidTank info = new FluidTank(TANK_CAPACITY);

			int water = info.readFromNBT(tagCompound.getCompoundTag("tankIn")).getFluidAmount();
			int auriferousWater = info.readFromNBT(tagCompound.getCompoundTag("tankOut")).getFluidAmount();

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				addInformationLocalized(tooltip, "message.waterresources.extractor", energy, sizeIn, water, auriferousWater);
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

	/*@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		TileEntityExtractor te = (TileEntityExtractor)worldIn.getTileEntity(pos);
		if(0 < te.getClientProgress() && te.getClientProgress() < ExtractorConfig.MAX_PROGRESS)
		{
			for (int i = 0; i < 10; i++) 
			{
	            worldIn.spawnParticle(EnumParticleTypes.WATER_DROP, (double) pos.getX() + 0.7F, (double) pos.getY() + 0.9F, (double) pos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
	            worldIn.spawnParticle(EnumParticleTypes.WATER_DROP, (double) pos.getX() + 0.4F, (double) pos.getY() + 0.9F, (double) pos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
	        }
		}
	}*/
}
