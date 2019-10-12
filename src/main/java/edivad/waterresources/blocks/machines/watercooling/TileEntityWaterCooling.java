package edivad.waterresources.blocks.machines.watercooling;

import javax.annotation.Nonnull;

import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillator;
import edivad.waterresources.config.WaterCoolingConfig;
import edivad.waterresources.customrecipes.watercooling.WaterCoolingRecipe;
import edivad.waterresources.customrecipes.watercooling.WaterCoolingRecipeRegistry;
import edivad.waterresources.tools.interfaces.IGuiTile;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileEntityWaterCooling extends PoweredTileEntity implements ITickable, IGuiTile {

	public static final int INPUT_SLOTS = 1;
	public static final int OUTPUT_SLOTS = 0;
	public static final int SIZE = INPUT_SLOTS + OUTPUT_SLOTS;

	// Fluid
	private FluidTank tankIn = new FluidTank(WaterCooling.TANK_CAPACITY) {

		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid.getFluid().equals(FluidRegistry.WATER);
		}
	};
	private int clientAmountIn = -1;

	private FluidTank tankOut = new FluidTank(WaterCooling.TANK_CAPACITY) {

		@Override
		public boolean canFill()
		{
			return false;
		}
	};
	private int clientAmountOut = -1;

	private ItemStackHandler inputHandler = new ItemStackHandler(INPUT_SLOTS) {

		@Override
		protected void onContentsChanged(int slot)
		{
			TileEntityWaterCooling.this.markDirty();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			WaterCoolingRecipe recipe = WaterCoolingRecipeRegistry.getRecipe(stack);
			return (recipe != null);
		}
	};

	private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(inputHandler);

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		super.readRestorableFromNBT(compound);
		if(compound.hasKey("itemsIn"))
			inputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
		this.tankIn.readFromNBT(compound.getCompoundTag("tankIn"));
		this.tankOut.readFromNBT(compound.getCompoundTag("tankOut"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		super.writeRestorableToNBT(compound);
		compound.setTag("itemsIn", inputHandler.serializeNBT());

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
				setProgress(WaterCoolingConfig.MAX_PROGRESS);
				this.markDirty();
				return;
			}

			if(getProgress() > 0)
			{
				energyStorage.consumePower(RF_PER_TICK);
				decrementProgress();
				if(getProgress() <= 0)
				{
					attemptSmelt();
				}
				this.markDirty();
			}
			else
			{
				startSmelt();
			}
		}
	}

	private void attemptSmelt()
	{
		ItemStack input = inputHandler.getStackInSlot(0);
		if(!input.isEmpty())
		{
			tankOut.fillInternal(WaterCoolingRecipeRegistry.getRecipe(input).getOutputFluid(), true);
			tankIn.drain(WaterCoolingConfig.WATER_CONSUMED, true);
			inputHandler.extractItem(0, 1, false);
		}
	}

	private void startSmelt()
	{
		ItemStack input = inputHandler.getStackInSlot(0);
		if(!input.isEmpty())
		{
			setProgress(WaterCoolingConfig.MAX_PROGRESS);
			this.markDirty();
		}
	}

	private boolean checkBeforeStart()
	{
		if(energyStorage.getEnergyStored() < RF_PER_TICK)
			return true;
		if(inputHandler.getStackInSlot(0).isEmpty())
			return true;
		if(getFluidInAmount() < WaterCoolingConfig.WATER_CONSUMED)
			return true;
		if(getFluidOutAmount() >= WaterCooling.TANK_CAPACITY)
			return true;
		if(getFluidOutAmount() + WaterCoolingRecipeRegistry.getRecipe(inputHandler.getStackInSlot(0)).getOutputFluid().amount > WaterCooling.TANK_CAPACITY)
			return true;
		return false;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
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
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if(facing == null)
			{
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedHandler);
			}
			else if(facing == EnumFacing.UP)
			{
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputHandler);
			}
		}

		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFace = world.getBlockState(pos).getValue(WaterCooling.FACING);
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
	public Container createContainer(EntityPlayer player)
	{
		return new WaterCoolingContainer(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new WaterCoolingGUI(this, new WaterCoolingContainer(player.inventory, this));
	}
}
