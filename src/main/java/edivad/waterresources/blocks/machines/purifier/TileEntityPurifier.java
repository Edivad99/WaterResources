package edivad.waterresources.blocks.machines.purifier;

import javax.annotation.Nonnull;

import edivad.waterresources.blocks.machines.common.PoweredTileEntity;
import edivad.waterresources.config.PurifierConfig;
import edivad.waterresources.customrecipes.GenericRecipe;
import edivad.waterresources.customrecipes.purifier.PurifierRecipeRegistry;
import edivad.waterresources.tools.interfaces.IGuiTile;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileEntityPurifier extends PoweredTileEntity implements ITickable, IGuiTile {

	public static final int INPUT_SLOTS = 1;
	public static final int OUTPUT_SLOTS = 1;
	public static final int SIZE = INPUT_SLOTS + OUTPUT_SLOTS;

	private ItemStackHandler inputHandler = new ItemStackHandler(INPUT_SLOTS) {

		@Override
		protected void onContentsChanged(int slot)
		{
			TileEntityPurifier.this.markDirty();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{

			GenericRecipe recipe = PurifierRecipeRegistry.getRecipe(stack);
			return (recipe != null);
		}
	};

	private ItemStackHandler outputHandler = new ItemStackHandler(OUTPUT_SLOTS) {

		@Override
		protected void onContentsChanged(int slot)
		{
			TileEntityPurifier.this.markDirty();
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			return false;
		}
	};

	private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(inputHandler, outputHandler);

	@Override
	public void readRestorableFromNBT(NBTTagCompound compound)
	{
		super.readRestorableFromNBT(compound);
		if(compound.hasKey("itemsIn"))
			inputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
		if(compound.hasKey("itemsOut"))
			outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
	}

	@Override
	public void writeRestorableToNBT(NBTTagCompound compound)
	{
		super.writeRestorableToNBT(compound);
		compound.setTag("itemsIn", inputHandler.serializeNBT());
		compound.setTag("itemsOut", outputHandler.serializeNBT());
	}

	@Override
	public void update()
	{

		if(!world.isRemote)
		{
			if(checkBeforeStart())
			{
				setProgress(PurifierConfig.MAX_PROGRESS);
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
		if(outputHandler.getStackInSlot(0).getCount() > 64 - getResult(inputHandler.getStackInSlot(0)).getCount())
			return true;
		return false;
	}

	private void processing()
	{
		ItemStack result = getResult(inputHandler.getStackInSlot(0));
		if(!result.isEmpty())
		{
			insertOutput(result.copy(), false);
			inputHandler.extractItem(0, 1, false);
		}
	}

	private void start()
	{
		ItemStack result = getResult(inputHandler.getStackInSlot(0));
		if(!result.isEmpty())
		{
			setProgress(PurifierConfig.MAX_PROGRESS);
			this.markDirty();
		}
	}

	private ItemStack getResult(ItemStack stackInSlot)
	{
		GenericRecipe recipe = PurifierRecipeRegistry.getRecipe(stackInSlot);
		if(recipe != null)
			return recipe.getOutput();
		return ItemStack.EMPTY;
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
			else if(facing == EnumFacing.DOWN)
			{
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);
			}
			else
			{
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputHandler);
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public Container createContainer(EntityPlayer player)
	{
		return new PurifierContainer(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer createGui(EntityPlayer player)
	{
		return new PurifierGUI(this, new PurifierContainer(player.inventory, this));
	}
}
