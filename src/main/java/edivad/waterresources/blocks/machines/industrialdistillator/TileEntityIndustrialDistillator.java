package edivad.waterresources.blocks.machines.industrialdistillator;

import javax.annotation.Nullable;

import edivad.waterresources.ModBlocks;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlate;
import edivad.waterresources.blocks.machines.coolingplate.CoolingPlateState;
import edivad.waterresources.config.IndustrialDistillatorConfig;
import edivad.waterresources.tools.interfaces.IGuiTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityIndustrialDistillator extends PoweredTileEntity implements ITickable, IGuiTile {

	private CoolingPlateState state = CoolingPlateState.OFF;

	// Fluid
	private FluidTank tankIn = new FluidTank(IndustrialDistillator.TANK_CAPACITY) {

		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid.getFluid().equals(FluidRegistry.WATER);
		}
	};
	private int clientAmountIn = -1;

	private FluidTank tankOut = new FluidTank(IndustrialDistillator.TANK_CAPACITY) {

		@Override
		public boolean canFill()
		{
			return false;
		}
	};
	private int clientAmountOut = -1;

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		super.readRestorableFromNBT(compound);
		this.tankIn.readFromNBT(compound.getCompoundTag("tankIn"));
		this.tankOut.readFromNBT(compound.getCompoundTag("tankOut"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		super.writeRestorableToNBT(compound);

		NBTTagCompound tankNBTIn = new NBTTagCompound();
		tankIn.writeToNBT(tankNBTIn);
		compound.setTag("tankIn", tankNBTIn);

		NBTTagCompound tankNBTOut = new NBTTagCompound();
		tankOut.writeToNBT(tankNBTOut);
		compound.setTag("tankOut", tankNBTOut);
	}

	@Override
	public void update()
	{

		if(!world.isRemote)
		{
			if(checkBeforeStart())
			{
				if(!coolingPlateIsWorking())
					setState(CoolingPlateState.NOLIQUID);
				else
					setState(CoolingPlateState.OFF);
				setProgress(IndustrialDistillatorConfig.MAX_PROGRESS);
				this.markDirty();
				return;
			}

			if(getProgress() > 0)
			{
				setState(CoolingPlateState.WORKING);
				energyStorage.consumePower(RF_PER_TICK);
				decrementProgress();
				if(getProgress() <= 0)
				{
					tankOut.fillInternal(new FluidStack(ModLiquids.distilled_water, IndustrialDistillatorConfig.WATER_CONSUMED), true);
					tankIn.drain(IndustrialDistillatorConfig.WATER_CONSUMED, true);
				}
				this.markDirty();
			}
			else
			{
				setState(CoolingPlateState.OFF);
				setProgress(IndustrialDistillatorConfig.MAX_PROGRESS);
				this.markDirty();
			}
		}
	}

	public boolean checkBeforeStart()
	{
		if(energyStorage.getEnergyStored() < RF_PER_TICK)
			return true;
		if(getFluidInAmount() < IndustrialDistillatorConfig.WATER_CONSUMED)
			return true;
		if(getFluidOutAmount() + IndustrialDistillatorConfig.WATER_CONSUMED > IndustrialDistillator.TANK_CAPACITY)
			return true;
		if(!coolingPlateIsWorking())
			return true;
		return false;
	}

	public boolean coolingPlateIsWorking()
	{
		if(world.getBlockState(pos.add(0, +1, 0)).getBlock().equals(ModBlocks.COOLING_PLATE))
		{
			IBlockState blockState = world.getBlockState(pos.add(0, 1, 0)).getActualState(world, pos.add(0, 1, 0));
			if(blockState.getValue(CoolingPlate.STATE).equals(CoolingPlateState.WORKING))
			{
				setState(CoolingPlateState.NOLIQUID);
				return true;
			}
		}
		return false;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFace = world.getBlockState(pos).getValue(IndustrialDistillator.FACING);
			if(blockFace == EnumFacing.SOUTH || blockFace == EnumFacing.NORTH)
			{
				if(facing == EnumFacing.WEST || facing == EnumFacing.EAST)
					return true;
			}
			else
			{
				if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH)
					return true;
			}

		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFace = world.getBlockState(pos).getValue(IndustrialDistillator.FACING);
			if(blockFace == EnumFacing.EAST || blockFace == EnumFacing.DOWN)
			{
				if(facing == EnumFacing.NORTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankIn);
				else if(facing == EnumFacing.SOUTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankOut);
			}
			else if(blockFace == EnumFacing.SOUTH)
			{
				if(facing == EnumFacing.EAST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankIn);
				else if(facing == EnumFacing.WEST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankOut);
			}
			else if(blockFace == EnumFacing.WEST || blockFace == EnumFacing.UP)
			{
				if(facing == EnumFacing.SOUTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankIn);
				else if(facing == EnumFacing.NORTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankOut);
			}
			else if(blockFace == EnumFacing.NORTH)
			{
				if(facing == EnumFacing.WEST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankIn);
				else if(facing == EnumFacing.EAST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankOut);
			}
		}

		return super.getCapability(capability, facing);
	}

	// Getter/Setter Client Fluid
	public int getClientFluidInAmount()
	{
		return clientAmountIn;
	}

	public void setClientFluidInAmount(int clientAmountFluid)
	{
		this.clientAmountIn = clientAmountFluid;
	}

	public int getFluidInAmount()
	{
		return tankIn.getFluidAmount();
	}

	public int getClientFluidOutAmount()
	{
		return clientAmountOut;
	}

	public void setClientFluidOutAmount(int clientAmountFluid)
	{
		this.clientAmountOut = clientAmountFluid;
	}

	public int getFluidOutAmount()
	{
		return tankOut.getFluidAmount();
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbtTagCompound = super.getUpdateTag();
		nbtTagCompound.setInteger("state", state.ordinal());
		return nbtTagCompound;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		int stateIndex = pkt.getNbtCompound().getInteger("state");

		if(world.isRemote && stateIndex != state.ordinal())
		{
			state = CoolingPlateState.VALUES[stateIndex];
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	public void setState(CoolingPlateState state)
	{
		if(this.state != state)
		{
			this.state = state;
			this.markDirty();
			IBlockState blockState = world.getBlockState(pos);
			getWorld().notifyBlockUpdate(pos, blockState, blockState, 3);
		}
	}

	public CoolingPlateState getState()
	{
		return state;
	}

	@Override
	public Container createContainer(EntityPlayer player)
	{
		return new IndustrialDistillatorContainer(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new IndustrialDistillatorGUI(this, new IndustrialDistillatorContainer(player.inventory, this));
	}
}
