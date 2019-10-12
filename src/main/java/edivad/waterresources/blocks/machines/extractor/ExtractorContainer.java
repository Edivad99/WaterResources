package edivad.waterresources.blocks.machines.extractor;

import edivad.waterresources.config.ExtractorConfig;
import edivad.waterresources.network.Messages;
import edivad.waterresources.network.PacketSyncMachineState;
import edivad.waterresources.tools.interfaces.IMachineStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ExtractorContainer extends Container implements IMachineStateContainer {

	private final TileEntityExtractor tileEntity;

	public ExtractorContainer(IInventory playerInventory, TileEntityExtractor tileEntity)
	{
		this.tileEntity = tileEntity;

		addOwnSlots();
		addplayerSolots(playerInventory);
	}

	private void addOwnSlots()
	{
		IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 70, 35));
	}

	private void addplayerSolots(IInventory playerInventory)
	{
		// Main Inventory
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		// Hotbar
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileEntity.canInteractWith(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot_clicked = (Slot) this.inventorySlots.get(index);

		if(slot_clicked != null && slot_clicked.getHasStack())
		{
			ItemStack itemstack1 = slot_clicked.getStack();
			itemstack = itemstack1.copy();

			if(index < this.tileEntity.SIZE)// From machine to player
			{
				if(!this.mergeItemStack(itemstack1, this.tileEntity.SIZE, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!this.mergeItemStack(itemstack1, 0, this.tileEntity.SIZE, false))// From player to machine
			{
				return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty())
				slot_clicked.putStack(ItemStack.EMPTY);
			else
				slot_clicked.onSlotChanged();
		}
		return itemstack;
	}

	@Override
	public void detectAndSendChanges()
	{
		// For sync variable from client and server
		super.detectAndSendChanges();
		if(!this.tileEntity.getWorld().isRemote)
		{
			if(tileEntity.getProgress() != tileEntity.getClientProgress() || tileEntity.getEnergy() != tileEntity.getClientEnergy() || tileEntity.getFluidInAmount() != tileEntity.getClientFluidInAmount() || tileEntity.getFluidOutAmount() != tileEntity.getClientFluidOutAmount())
			{
				tileEntity.setClientEnergy(tileEntity.getEnergy());
				tileEntity.setClientProgress(tileEntity.getProgress());
				tileEntity.setClientFluidInAmount(tileEntity.getFluidInAmount());
				tileEntity.setClientFluidOutAmount(tileEntity.getFluidOutAmount());

				for(IContainerListener listener : listeners)
				{
					if(listener instanceof EntityPlayerMP)
					{
						EntityPlayerMP player = (EntityPlayerMP) listener;
						int percent = 100 - tileEntity.getProgress() * 100 / ExtractorConfig.MAX_PROGRESS;
						Messages.INSTANCE.sendTo(new PacketSyncMachineState(this.tileEntity.getEnergy(), percent, tileEntity.getFluidInAmount(), tileEntity.getFluidOutAmount()), player);
					}
				}
			}
		}

	}

	@Override
	public void sync(int energy, int progress, int... fluids)
	{
		this.tileEntity.setClientEnergy(energy);
		this.tileEntity.setClientProgress(progress);
		this.tileEntity.setClientFluidInAmount(fluids[0]);
		this.tileEntity.setClientFluidOutAmount(fluids[1]);
	}
}
