package edivad.waterresources.blocks.machines.coolingplate;

import javax.annotation.Nullable;

import edivad.waterresources.ModBlocks;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.blocks.machines.washingmachine.WashingMachine;
import edivad.waterresources.config.CoolingPlateConfig;
import edivad.waterresources.tools.interfaces.IGuiTile;
import edivad.waterresources.tools.interfaces.IRestorableTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCoolingPlate extends TileEntity implements ITickable, IRestorableTileEntity, IGuiTile {

	// Processing
	private int progress = 0;
	private int clientProgress = -1;

	private CoolingPlateState state = CoolingPlateState.OFF;

	// Fluid
	private FluidTank tank = new FluidTank(CoolingPlate.TANK_CAPACITY) {

		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid.getFluid().equals(new FluidStack(ModLiquids.ice_water, 1000).getFluid());
		}
	};
	private int clientAmount = -1;

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// To save tileEntity
		super.readFromNBT(compound);
		readRestorableFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		writeRestorableToNBT(compound);
		return compound;
	}

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		this.tank.readFromNBT(compound.getCompoundTag("tank"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		NBTTagCompound tankNBT = new NBTTagCompound();
		tank.writeToNBT(tankNBT);
		compound.setTag("tank", tankNBT);
	}

	@Override
	public void update()
	{
		if(!world.isRemote)
		{
			if(checkBeforeStart())
			{
				if(getFluidAmount() < CoolingPlateConfig.WATER_CONSUMED)
					setState(CoolingPlateState.NOLIQUID);
				else
					setState(CoolingPlateState.OFF);

				progress = CoolingPlateConfig.MAX_PROGRESS;
				this.markDirty();
				return;
			}

			if(progress > 0)
			{
				setState(CoolingPlateState.WORKING);
				progress--;
				if(progress <= 0)
				{
					tank.drain(CoolingPlateConfig.WATER_CONSUMED, true);
				}
				this.markDirty();
			}
			else
			{
				setState(CoolingPlateState.OFF);
				progress = CoolingPlateConfig.MAX_PROGRESS;
				this.markDirty();
			}
		}
	}

	private boolean checkBeforeStart()
	{
		if(getFluidAmount() < CoolingPlateConfig.WATER_CONSUMED)
			return true;
		if(!isAboveIndustrialDistillator())
			return true;
		return false;
	}

	public boolean isAboveIndustrialDistillator()
	{
		return world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(ModBlocks.INDUSTRIAL_DISTILLATOR);
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
			EnumFacing blockFace = world.getBlockState(pos).getValue(WashingMachine.FACING);

			if(blockFace == EnumFacing.SOUTH)
			{
				if(facing == EnumFacing.SOUTH)
					return true;
			}
			else if(blockFace == EnumFacing.EAST || blockFace == EnumFacing.DOWN)
			{
				if(facing == EnumFacing.EAST)
					return true;
			}
			else if(blockFace == EnumFacing.WEST || blockFace == EnumFacing.UP)
			{
				if(facing == EnumFacing.WEST)
					return true;
			}
			else if(blockFace == EnumFacing.NORTH)
			{
				if(facing == EnumFacing.NORTH)
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
			EnumFacing blockFace = world.getBlockState(pos).getValue(WashingMachine.FACING);

			if(blockFace == EnumFacing.SOUTH)
			{
				if(facing == EnumFacing.SOUTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
			}
			else if(blockFace == EnumFacing.EAST || blockFace == EnumFacing.DOWN)
			{
				if(facing == EnumFacing.EAST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
			}
			else if(blockFace == EnumFacing.WEST || blockFace == EnumFacing.UP)
			{
				if(facing == EnumFacing.WEST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
			}
			else if(blockFace == EnumFacing.NORTH)
			{
				if(facing == EnumFacing.NORTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
			}
		}

		return super.getCapability(capability, facing);
	}

	// Getter/Setter Server Progress
	public int getProgress()
	{
		return progress;
	}

	public void setProgress(int progress)
	{
		this.progress = progress;
	}

	// Getter/Setter Client Progress
	public int getClientProgress()
	{
		return clientProgress;
	}

	public void setClientProgress(int clientProgress)
	{
		this.clientProgress = clientProgress;
	}

	// Getter/Setter Client Fluid
	public int getClientFluidAmount()
	{
		return clientAmount;
	}

	public void setClientFluidAmount(int clientAmountFluid)
	{
		this.clientAmount = clientAmountFluid;
	}

	public int getFluidAmount()
	{
		return tank.getFluidAmount();
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
		return new CoolingPlateContainer(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new CoolingPlateGUI(this, new CoolingPlateContainer(player.inventory, this));
	}
}