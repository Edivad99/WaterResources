package edivad.waterresources.blocks.machines.washingmachine;

import javax.annotation.Nonnull;

import edivad.waterresources.ModItems;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.blocks.machines.extractor.Extractor;
import edivad.waterresources.config.WashingMachineConfig;
import edivad.waterresources.customrecipes.washingmachine.WashingMachineRecipeRegistry;
import edivad.waterresources.tools.interfaces.IGuiTile;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileEntityWashingMachine extends PoweredTileEntity implements ITickable, IGuiTile {

	public static final int INPUT_SLOTS = 0;
	public static final int OUTPUT_SLOTS = 1;
	public static final int SIZE = INPUT_SLOTS + OUTPUT_SLOTS;

	// Fluid
	private FluidTank tankAU = new FluidTank(WashingMachine.TANK_CAPACITY) {

		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid.getFluid().equals(ModLiquids.auriferous_water);
		}
	};
	private int clientAmountAU = -1;

	private FluidTank tankWater = new FluidTank(WashingMachine.TANK_CAPACITY) {

		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid.getFluid().equals(ModLiquids.distilled_water);
		}
	};
	private int clientAmountWater = -1;

	private ItemStackHandler outputHandler = new ItemStackHandler(OUTPUT_SLOTS) {

		@Override
		protected void onContentsChanged(int slot)
		{
			TileEntityWashingMachine.this.markDirty();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			return false;
		}
	};

	private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(outputHandler);

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		super.readRestorableFromNBT(compound);
		if(compound.hasKey("itemsOut"))
			outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
		this.tankAU.readFromNBT(compound.getCompoundTag("tankAU"));
		this.tankWater.readFromNBT(compound.getCompoundTag("tankWater"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		super.writeRestorableToNBT(compound);
		compound.setTag("itemsOut", outputHandler.serializeNBT());

		NBTTagCompound tankNBTAU = new NBTTagCompound();
		tankAU.writeToNBT(tankNBTAU);
		compound.setTag("tankAU", tankNBTAU);

		NBTTagCompound tankNBTWater = new NBTTagCompound();
		tankWater.writeToNBT(tankNBTWater);
		compound.setTag("tankWater", tankNBTWater);
	}

	@Override
	public void update()
	{

		if(!world.isRemote)
		{
			if(checkBeforeStart())
			{
				setProgress(WashingMachineConfig.MAX_PROGRESS);
				this.markDirty();
				return;
			}

			if(getProgress() > 0)
			{
				energyStorage.consumePower(RF_PER_TICK);
				decrementProgress();
				if(getProgress() <= 0)
				{
					processing();
				}
			}
			else
			{
				setProgress(WashingMachineConfig.MAX_PROGRESS);
			}
			this.markDirty();
		}
	}

	private boolean checkBeforeStart()
	{
		if(energyStorage.getEnergyStored() < RF_PER_TICK)
			return true;
		if(tankAU.getFluidAmount() < WashingMachineConfig.WATER_CONSUMED)
			return true;
		if(tankWater.getFluidAmount() < WashingMachineConfig.WATER_CONSUMED)
			return true;
		if(outputHandler.getStackInSlot(0).getCount() > 64 - WashingMachineRecipeRegistry.getRecipe(tankWater.getFluid(), tankAU.getFluid()).getOutput().getCount())
			return true;

		return false;
	}

	private void processing()
	{
		tankWater.drain(WashingMachineConfig.WATER_CONSUMED, true);
		tankAU.drain(WashingMachineConfig.WATER_CONSUMED, true);
		insertOutput(new ItemStack(ModItems.goldFragment, 2), false);
	}

	private boolean insertOutput(ItemStack output, boolean simulate)
	{
		for(int i = 0; i < OUTPUT_SLOTS; i++)
		{
			ItemStack remaining = outputHandler.insertItem(i, output, simulate);
			if(remaining.isEmpty())
				return true;
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
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFace = world.getBlockState(pos).getValue(WashingMachine.FACING);
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);
		}

		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			EnumFacing blockFace = world.getBlockState(pos).getValue(Extractor.FACING);
			if(blockFace == EnumFacing.EAST || blockFace == EnumFacing.DOWN)
			{
				if(facing == EnumFacing.NORTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankAU);
				else if(facing == EnumFacing.SOUTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankWater);
			}
			else if(blockFace == EnumFacing.SOUTH)
			{
				if(facing == EnumFacing.EAST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankAU);
				else if(facing == EnumFacing.WEST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankWater);
			}
			else if(blockFace == EnumFacing.WEST || blockFace == EnumFacing.UP)
			{
				if(facing == EnumFacing.SOUTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankAU);
				else if(facing == EnumFacing.NORTH)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankWater);
			}
			else if(blockFace == EnumFacing.NORTH)
			{
				if(facing == EnumFacing.WEST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankAU);
				else if(facing == EnumFacing.EAST)
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tankWater);
			}
		}

		return super.getCapability(capability, facing);
	}

	// Getter/Setter Client Fluid
	public int getClientFluidWaterAmount()
	{
		return clientAmountWater;
	}

	public int getClientFluidAUAmount()
	{
		return clientAmountAU;
	}

	public void setClientFluidWaterAmount(int clientAmountFluid)
	{
		this.clientAmountWater = clientAmountFluid;
	}

	public void setClientFluidAUAmount(int clientAmountFluid)
	{
		this.clientAmountAU = clientAmountFluid;
	}

	public int getFluidWaterAmount()
	{
		return tankWater.getFluidAmount();
	}

	public int getFluidAUAmount()
	{
		return tankAU.getFluidAmount();
	}

	@Override
	public Container createContainer(EntityPlayer player)
	{
		return new WashingMachineContainer(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new WashingMachineGUI(this, new WashingMachineContainer(player.inventory, this));
	}
}
