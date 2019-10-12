package edivad.waterresources.blocks.machines.extractor;

import javax.annotation.Nonnull;

import edivad.waterresources.ModBlocks;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.blocks.machines.industrialdistillator.IndustrialDistillator;
import edivad.waterresources.config.ExtractorConfig;
import edivad.waterresources.customrecipes.extractor.ExtractorRecipe;
import edivad.waterresources.customrecipes.extractor.ExtractorRecipeRegistry;
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

public class TileEntityExtractor extends PoweredTileEntity implements ITickable, IGuiTile {

	public static final int INPUT_SLOTS = 1;
	public static final int OUTPUT_SLOTS = 0;
	public static final int SIZE = INPUT_SLOTS + OUTPUT_SLOTS;

	// Fluid In
	private FluidTank tankIn = new FluidTank(Extractor.TANK_CAPACITY) {

		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid.getFluid().equals(new FluidStack(ModLiquids.distilled_water, 1000).getFluid());
		}
	};
	private int clientAmountIn = -1;

	// Fluid Out
	private FluidTank tankOut = new FluidTank(Extractor.TANK_CAPACITY) {

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
			TileEntityExtractor.this.markDirty();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			ItemStack input = getResult(stack);
			return (!input.isEmpty());
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
				setProgress(ExtractorConfig.MAX_PROGRESS);
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
				this.markDirty();
			}
			else
			{
				start();
			}
		}
	}

	private boolean checkBeforeStart()
	{
		if(energyStorage.getEnergyStored() < RF_PER_TICK)
			return true;
		if(inputHandler.getStackInSlot(0).isEmpty())
			return true;
		if(getResult(inputHandler.getStackInSlot(0)) == ItemStack.EMPTY)
			return true;
		if(getFluidInAmount() < ExtractorConfig.DISTILLED_WATER_CONSUME)
			return true;
		if(getFluidOutAmount() + liquidOutput(inputHandler.getStackInSlot(0)) > Extractor.TANK_CAPACITY)
			return true;
		return false;
	}

	private void processing()
	{
		if(receiveItemOutput())
		{
			ItemStack input = inputHandler.getStackInSlot(0);
			if(!input.isEmpty())
			{
				tankOut.fillInternal(new FluidStack(ModLiquids.auriferous_water, liquidOutput(inputHandler.getStackInSlot(0))), true);
			}
		}
		tankIn.drain(ExtractorConfig.DISTILLED_WATER_CONSUME, true);
		inputHandler.extractItem(0, 1, false);
	}

	private boolean receiveItemOutput()
	{
		double n = Math.round(Math.random() * 100);// from 0 to 0.99
		int probability = ExtractorConfig.BASE_PERCENT_OF_PROCESSING;

		if(ItemStack.areItemsEqual(inputHandler.getStackInSlot(0), new ItemStack(ModBlocks.GOLDFIELD_ORE)))
			probability = ExtractorConfig.BASE_PERCENT_OF_PROCESSING_WITH_GOLDFIELD_ORE;

		return (n < probability);
	}

	private int liquidOutput(ItemStack input)
	{
		if(ItemStack.areItemsEqual(input, new ItemStack(ModBlocks.GOLDFIELD_ORE)))
			return ExtractorConfig.AURIFEROUS_WATER_GOLDFIELD_ORE;
		return ExtractorConfig.AURIFEROUS_WATER_DIRT;

	}

	private void start()
	{
		ItemStack input = inputHandler.getStackInSlot(0);
		if(!input.isEmpty())
		{
			setProgress(ExtractorConfig.MAX_PROGRESS);
			this.markDirty();
		}
	}

	private ItemStack getResult(ItemStack stackInSlot)
	{
		ExtractorRecipe recipe = ExtractorRecipeRegistry.getRecipe(stackInSlot);
		if(recipe != null)
			return recipe.getInput();
		return ItemStack.EMPTY;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if(facing == EnumFacing.UP)
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
			EnumFacing blockFace = world.getBlockState(pos).getValue(Extractor.FACING);
			if(blockFace == EnumFacing.EAST)
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
			else if(blockFace == EnumFacing.WEST || blockFace == EnumFacing.UP || blockFace == EnumFacing.DOWN)
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
		return new ExtractorContainer(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new ExtractorGUI(this, new ExtractorContainer(player.inventory, this));
	}
}